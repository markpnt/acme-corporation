package com.example.acmecorporation.repository;

import com.example.acmecorporation.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoom_IdAndDate(Long room, LocalDate date);

    /**
     * Check for overlapping bookings for the same room and date.
     */
    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.room.id = :room AND b.date = :date " +
            "AND ((b.startTime < :endTime AND b.endTime > :startTime))")
    boolean existsByRoomAndDateAndTimeOverlap(
            @Param("room") Long room,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime);
}
