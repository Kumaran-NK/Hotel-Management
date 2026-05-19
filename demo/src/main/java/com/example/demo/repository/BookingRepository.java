package com.example.demo.repository;

import com.example.demo.entity.Booking;
import com.example.demo.entity.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCustomer_UserIdAndBookingStatus(Long userId, BookingStatus status);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.roomId = :roomId " +
           "AND b.bookingStatus = 'CONFIRMED' " +
           "AND b.checkInDate < :checkOut AND b.checkOutDate > :checkIn")
    long countConflictingBookings(@Param("roomId") Long roomId,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.room.hotel.hotelId = :hotelId " +
           "AND b.room.roomType = :roomType AND b.bookingStatus = 'CONFIRMED' " +
           "AND b.checkInDate < :checkOut AND b.checkOutDate > :checkIn")
    long countOverlappingBookings(@Param("hotelId") Long hotelId,
                                  @Param("roomType") String roomType,
                                  @Param("checkIn") LocalDate checkIn,
                                  @Param("checkOut") LocalDate checkOut);

    @Query(value = "SELECT h.hotel_name, r.room_type, " +
           "COUNT(DISTINCT b.booking_id) * 1.0 / NULLIF((SELECT COUNT(*) FROM room r2 WHERE r2.hotel_id = h.hotel_id AND r2.room_type = r.room_type), 0) AS occupancy_rate, " +
           "SUM(b.total_amount) AS revenue " +
           "FROM booking b JOIN room r ON b.room_id = r.room_id JOIN hotel h ON r.hotel_id = h.hotel_id " +
           "WHERE b.booking_status = 'CONFIRMED' AND b.check_in_date < :end AND b.check_out_date > :start " +
           "GROUP BY h.hotel_id, r.room_type " +
           "ORDER BY occupancy_rate DESC", nativeQuery = true)
    List<Object[]> findHotelOccupancyAndRevenuePerRoomType(@Param("start") LocalDate start,
                                                           @Param("end") LocalDate end);
}