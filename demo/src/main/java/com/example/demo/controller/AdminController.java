package com.example.demo.controller;

import com.example.demo.entity.*;
import com.example.demo.service.HotelService;
import com.example.demo.service.RoomService;
import com.example.demo.service.ReportService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired private HotelService hotelService;
    @Autowired private RoomService roomService;
    @Autowired private ReportService reportService;

    private void checkAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.ADMIN)
            throw new RuntimeException("Admin access required");
    }

    // Hotel management
    @GetMapping("/hotels")
    public List<Hotel> getAllHotels(HttpSession session) {
        checkAdmin(session);
        return hotelService.getAllHotels();
    }

    @PostMapping("/hotels")
    public Hotel createHotel(@RequestBody Hotel hotel, HttpSession session) {
        checkAdmin(session);
        return hotelService.createHotel(hotel);
    }

    @DeleteMapping("/hotels/{id}")
    public void deleteHotel(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        hotelService.deleteHotel(id);
    }

    // Room management
    @GetMapping("/rooms")
    public List<Room> getAllRooms(HttpSession session) {
        checkAdmin(session);
        return roomService.getAllRooms();
    }

    @PostMapping("/rooms")
    public Room createRoom(@RequestBody Room room, HttpSession session) {
        checkAdmin(session);
        return roomService.createRoom(room);
    }

    @DeleteMapping("/rooms/{id}")
    public void deleteRoom(@PathVariable Long id, HttpSession session) {
        checkAdmin(session);
        roomService.deleteRoom(id);
    }

    // Report
    @GetMapping("/reports/occupancy-revenue")
    public List<Object[]> getOccupancyReport(@RequestParam String start, @RequestParam String end, HttpSession session) {
        checkAdmin(session);
        return reportService.getHotelOccupancyAndRevenuePerRoomType(LocalDate.parse(start), LocalDate.parse(end));
    }
}