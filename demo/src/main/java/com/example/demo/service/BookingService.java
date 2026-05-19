package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.*;
import java.time.LocalDateTime;

@Service
public class BookingService {
    @Autowired private BookingRepository bookingRepository;
    @Autowired private RoomRepository roomRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private PaymentRepository paymentRepository;
    @Autowired private PricingService pricingService;
    @Autowired private AvailabilityCache availabilityCache;

    @Transactional
    public Booking createBooking(Long roomId, Long userId, LocalDate checkIn, LocalDate checkOut) {
        // validation
        if (checkIn.isBefore(LocalDate.now()) || !checkOut.isAfter(checkIn))
            throw new RuntimeException("Invalid date range");

        Room room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        User customer = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        // double booking check
        if (!availabilityCache.isRoomAvailable(roomId, checkIn, checkOut))
            throw new RuntimeException("Room already booked for these dates");

        
        double totalAmount = pricingService.calculateTotalPrice(room, checkIn, checkOut);

       
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setRoom(room);
        booking.setCheckInDate(checkIn);
        booking.setCheckOutDate(checkOut);
        booking.setBookingDate(LocalDate.now());
        booking.setTotalAmount(totalAmount);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking = bookingRepository.save(booking);

      
        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod("CREDIT_CARD");
        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setTransactionDate(LocalDateTime.now());
        paymentRepository.save(payment);
        booking.setPayment(payment);

        
        availabilityCache.addBooking(roomId, checkIn, checkOut);
        return booking;
    }

    @Transactional
    public Booking cancelBooking(Long bookingId, LocalDate cancellationDate) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new RuntimeException("Booking not found"));
        if (booking.getBookingStatus() == BookingStatus.CANCELLED)
            throw new RuntimeException("Booking already cancelled");

        long daysBeforeCheckIn = java.time.temporal.ChronoUnit.DAYS.between(cancellationDate, booking.getCheckInDate());
        double refundPercent = (daysBeforeCheckIn >= 2) ? 1.0 : 0.5;
        double refundAmount = booking.getTotalAmount() * refundPercent;

        Payment payment = booking.getPayment();
        payment.setPaymentStatus(PaymentStatus.REFUNDED);
        payment.setAmount(payment.getAmount() - refundAmount);
        paymentRepository.save(payment);

        booking.setBookingStatus(BookingStatus.CANCELLED);
        booking = bookingRepository.save(booking);

        availabilityCache.removeBooking(booking.getRoom().getRoomId(), booking.getCheckInDate(), booking.getCheckOutDate());
        return booking;
    }

    public List<Booking> getBookingsByUser(Long userId) {
        return bookingRepository.findByCustomer_UserIdAndBookingStatus(userId, BookingStatus.CONFIRMED);
    }


    public List<Booking> getAllBookings() {
    return bookingRepository.findAll();
}

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
    }
}