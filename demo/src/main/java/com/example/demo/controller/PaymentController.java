package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

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

   
    @GetMapping("/booking/{bookingId}")
    public Payment getPaymentByBooking(@PathVariable Long bookingId, HttpSession session) {
        User customer = getLoggedInCustomer(session);
        Payment payment = paymentService.getPaymentByBookingId(bookingId);
        if (!payment.getBooking().getCustomer().getUserId().equals(customer.getUserId())) {
            throw new RuntimeException("You can only view payments for your own bookings");
        }
        return payment;
    }

  
    @GetMapping("/my")
    public List<Payment> getMyPayments(HttpSession session) {
        User customer = getLoggedInCustomer(session);
        return paymentService.getPaymentsByCustomerId(customer.getUserId());
    }

    @GetMapping("/all")
    public List<Payment> getAllPayments(HttpSession session) {
        getLoggedInAdmin(session);
        return paymentService.getAllPayments();
    }

  
    @GetMapping("/{paymentId}")
    public Payment getPaymentById(@PathVariable Long paymentId, HttpSession session) {
        getLoggedInAdmin(session);
        return paymentService.getPaymentById(paymentId);
    }
}