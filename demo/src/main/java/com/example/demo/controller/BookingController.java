package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // Helper method to get logged-in customer (must have CUSTOMER role)
    private User getLoggedInCustomer(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.CUSTOMER) {
            throw new RuntimeException("Customer access required");
        }
        return user;
    }

 
    private User getLoggedInAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Admin access required");
        }
        return user;
    }

  
    @PostMapping
    public Booking createBooking(@RequestParam Long roomId,
                                 @RequestParam String checkIn,
                                 @RequestParam String checkOut,
                                 HttpSession session) {
        User customer = getLoggedInCustomer(session);
        return bookingService.createBooking(roomId, customer.getUserId(),
                LocalDate.parse(checkIn), LocalDate.parse(checkOut));
    }


    @PostMapping("/{bookingId}/cancel")
    public Booking cancelBooking(@PathVariable Long bookingId, HttpSession session) {
        User customer = getLoggedInCustomer(session);
        Booking booking = bookingService.cancelBooking(bookingId, LocalDate.now());
        if (!booking.getCustomer().getUserId().equals(customer.getUserId())) {
            throw new RuntimeException("You can only cancel your own bookings");
        }
        return booking;
    }

    
    @GetMapping("/my")
    public List<Booking> getMyBookings(HttpSession session) {
        User customer = getLoggedInCustomer(session);
        return bookingService.getBookingsByUser(customer.getUserId());
    }

    // ==================== Admin Endpoints ====================

  
    @GetMapping("/all")
    public List<Booking> getAllBookings(HttpSession session) {
        getLoggedInAdmin(session);
        return bookingService.getAllBookings();
    }

  
    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId, HttpSession session) {
        getLoggedInAdmin(session);
        return bookingService.getBookingById(bookingId);
    }
}