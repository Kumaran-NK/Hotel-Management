package com.example.demo.controller;

import com.example.demo.entity.Role;
import com.example.demo.entity.Room;
import com.example.demo.entity.User;
import com.example.demo.service.RoomService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

   
    private User getLoggedInAdmin(HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null || user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Admin access required");
        }
        return user;
    }

    
    @GetMapping("/available")
    public List<Room> searchAvailableRooms(@RequestParam String city,
                                           @RequestParam String checkIn,
                                           @RequestParam String checkOut,
                                           @RequestParam Integer capacity,
                                           HttpSession session) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            throw new RuntimeException("Please login to search rooms");
        }
        return roomService.findAvailableRooms(city,
                LocalDate.parse(checkIn), LocalDate.parse(checkOut), capacity);
    }


    @GetMapping("/{roomId}")
    public Room getRoomById(@PathVariable Long roomId, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            throw new RuntimeException("Please login to view room details");
        }
        return roomService.getRoomById(roomId);
    }

    @GetMapping("/all")
    public List<Room> getAllRooms(HttpSession session) {
        getLoggedInAdmin(session);
        return roomService.getAllRooms();
    }


    @PostMapping
    public Room createRoom(@RequestBody Room room, HttpSession session) {
        getLoggedInAdmin(session);
        return roomService.createRoom(room);
    }

    @PutMapping("/{roomId}")
    public Room updateRoom(@PathVariable Long roomId, @RequestBody Room room, HttpSession session) {
        getLoggedInAdmin(session);
        room.setRoomId(roomId);
        return roomService.updateRoom(room);
    }
    @DeleteMapping("/{roomId}")
    public void deleteRoom(@PathVariable Long roomId, HttpSession session) {
        getLoggedInAdmin(session);
        roomService.deleteRoom(roomId);
    }
}