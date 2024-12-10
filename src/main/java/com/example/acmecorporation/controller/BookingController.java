package com.example.acmecorporation.controller;

import com.example.acmecorporation.model.BookingDto;
import com.example.acmecorporation.model.BookingRequest;
import com.example.acmecorporation.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping(value = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @GetMapping()
    public ResponseEntity<List<BookingDto>> getBookings(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingDto> bookings = bookingService.getBookingsByRoomAndDate(roomId, date);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping()
    public ResponseEntity<BookingDto> createBooking(@RequestBody @Validated BookingRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }
}
