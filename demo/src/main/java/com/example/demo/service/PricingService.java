package com.example.demo.service;

import com.example.demo.entity.Room;
import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class PricingService {
    @Autowired private BookingRepository bookingRepository;
    @Value("${pricing.occupancy.threshold:0.8}") private double occupancyThreshold;
    @Value("${pricing.surcharge.multiplier:1.2}") private double surchargeMultiplier;

    public double calculateTotalPrice(Room room, LocalDate checkIn, LocalDate checkOut) {
        long days = ChronoUnit.DAYS.between(checkIn, checkOut);
        double baseTotal = room.getBasePrice() * days;
        double occupancy = getOccupancyRateForRoomType(room, checkIn, checkOut);
        if (occupancy > occupancyThreshold) {
            return baseTotal * surchargeMultiplier;
        }
        return baseTotal;
    }

    private double getOccupancyRateForRoomType(Room room, LocalDate checkIn, LocalDate checkOut) {
        long totalRoomsOfType = room.getHotel().getRooms().stream()
                .filter(r -> r.getRoomType().equals(room.getRoomType()))
                .count();
        if (totalRoomsOfType == 0) return 0;
        long bookedCount = bookingRepository.countOverlappingBookings(room.getHotel().getHotelId(),
                room.getRoomType(), checkIn, checkOut);
        return (double) bookedCount / totalRoomsOfType;
    }
}
