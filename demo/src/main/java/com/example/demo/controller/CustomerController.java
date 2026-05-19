package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BookingService;
import com.example.demo.service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired private BookingService bookingService;
    @Autowired private RoomService roomService;

    private User getLoggedInCustomer(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.CUSTOMER)
            throw new RuntimeException("Customer access required");
        return user;
    }

    @GetMapping("/rooms/available")
    public List<Room> searchAvailableRooms(@RequestParam String city,
                                           @RequestParam String checkIn,
                                           @RequestParam String checkOut,
                                           @RequestParam Integer capacity) {
        return roomService.findAvailableRooms(city, LocalDate.parse(checkIn), LocalDate.parse(checkOut), capacity);
    }

    // Create booking
    @PostMapping("/bookings")
    public Booking createBooking(@RequestParam Long roomId,
                                 @RequestParam String checkIn,
                                 @RequestParam String checkOut,
                                 HttpSession session) {
        User customer = getLoggedInCustomer(session);
        return bookingService.createBooking(roomId, customer.getUserId(), LocalDate.parse(checkIn), LocalDate.parse(checkOut));
    }

  
    @PostMapping("/bookings/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId, HttpSession session) {
        User customer = getLoggedInCustomer(session);
        Booking booking = bookingService.cancelBooking(bookingId, LocalDate.now());
        if (!booking.getCustomer().getUserId().equals(customer.getUserId()))
            throw new RuntimeException("You can only cancel your own bookings");
        return booking;
    }

   
    @GetMapping("/bookings")
    public List<Booking> getMyBookings(HttpSession session) {
        User customer = getLoggedInCustomer(session);
        return bookingService.getBookingsByUser(customer.getUserId());
    }
}