package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    @Autowired private RoomRepository roomRepository;
    @Autowired private AvailabilityCache availabilityCache;

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));
    }

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public List<Room> findAvailableRooms(String city, LocalDate checkIn, LocalDate checkOut, Integer capacity) {
        return roomRepository.findByHotel_CityAndCapacityGreaterThanEqual(city, capacity).stream()
                .filter(room -> availabilityCache.isRoomAvailable(room.getRoomId(), checkIn, checkOut))
                .collect(Collectors.toList());
    }

    public Room updateRoom(Room room) {
    if (!roomRepository.existsById(room.getRoomId())) {
        throw new RuntimeException("Room not found");
    }
    return roomRepository.save(room);
    }

}