package com.example.demo.service;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class AvailabilityCache {
    private final Map<Long, TreeMap<LocalDate, LocalDate>> bookedIntervals = new ConcurrentHashMap<>();

    public synchronized boolean isRoomAvailable(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        TreeMap<LocalDate, LocalDate> intervals = bookedIntervals.get(roomId);
        if (intervals == null) return true;
        for (Map.Entry<LocalDate, LocalDate> entry : intervals.entrySet()) {
            LocalDate existingStart = entry.getKey();
            LocalDate existingEnd = entry.getValue();
            if (!(checkOut.isBefore(existingStart) || checkIn.isAfter(existingEnd))) {
                return false;
            }
        }
        return true;
    }

    public synchronized void addBooking(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        bookedIntervals.computeIfAbsent(roomId, k -> new TreeMap<>()).put(checkIn, checkOut);
    }

    public synchronized void removeBooking(Long roomId, LocalDate checkIn, LocalDate checkOut) {
        TreeMap<LocalDate, LocalDate> intervals = bookedIntervals.get(roomId);
        if (intervals != null) {
            intervals.remove(checkIn, checkOut);
        }
    }
}