package com.example.demo.service;

import com.example.demo.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {
    @Autowired private BookingRepository bookingRepository;

    public List<Object[]> getHotelOccupancyAndRevenuePerRoomType(LocalDate start, LocalDate end) {
        return bookingRepository.findHotelOccupancyAndRevenuePerRoomType(start, end);
    }
}