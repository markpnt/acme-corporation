package com.example.acmecorporation.service;

import com.example.acmecorporation.model.BookingDto;
import com.example.acmecorporation.model.BookingRequest;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<BookingDto> getBookingsByRoomAndDate(Long roomId, LocalDate date);
    BookingDto createBooking(BookingRequest request);
    void cancelBooking(Long bookingId);
}
