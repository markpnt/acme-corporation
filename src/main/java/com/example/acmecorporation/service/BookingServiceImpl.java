package com.example.acmecorporation.service;

import com.example.acmecorporation.domain.Booking;
import com.example.acmecorporation.domain.Room;
import com.example.acmecorporation.exception.BookingException;
import com.example.acmecorporation.model.BookingDTO;
import com.example.acmecorporation.model.BookingRequest;
import com.example.acmecorporation.repository.BookingRepository;
import com.example.acmecorporation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    @Override
    public List<BookingDTO> getBookingsByRoomAndDate(Long roomId, LocalDate date) {
        List<Booking> bookings = bookingRepository.findByRoom_IdAndDate(roomId, date);
        return bookings.stream()
                .map(booking -> BookingDTO.builder()
                        .id(booking.getId())
                        .employeeEmail(booking.getEmployeeEmail())
                        .startTime(booking.getStartTime())
                        .endTime(booking.getEndTime()).build())
                .toList();
    }

    @Override
    public void createBooking(BookingRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));
        validateBookingRequest(request);

        Booking booking = Booking.builder()
                .room(room)
                .employeeEmail(request.getEmployeeEmail())
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        bookingRepository.save(booking);
    }

    /**
     * Validate the booking request.
     */
    private void validateBookingRequest(BookingRequest request) throws BookingException {
        // Check duration
        long hours = Duration.between(request.getStartTime(), request.getEndTime()).toHours();
        if (hours < 1 || (request.getEndTime().getMinute() != 0 || request.getStartTime().getMinute() != 0)) {
            throw new BookingException("Booking must be for at least 1 hour in whole-hour increments");
        }

        // Check overlapping bookings
        boolean hasConflict = bookingRepository.existsByRoomAndDateAndTimeOverlap(
                request.getRoomId(),
                request.getDate(),
                request.getStartTime(),
                request.getEndTime());

        if (hasConflict) {
            throw new BookingException("Booking conflicts with an existing reservation");
        }
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        // Validate the booking is not in the past
        if (booking.getDate().isBefore(LocalDate.now())) {
            throw new IllegalStateException("Cannot cancel past bookings");
        }

        bookingRepository.delete(booking);
    }
}
