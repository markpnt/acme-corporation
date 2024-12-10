package com.example.acmecorporation.service;

import com.example.acmecorporation.model.BookingDTO;
import com.example.acmecorporation.model.BookingRequest;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<BookingDTO> getBookingsByRoomAndDate(Long roomId, LocalDate date);
    void createBooking(BookingRequest request);
    void cancelBooking(Long bookingId);
}
