package com.example.acmecorporation.controller;

import com.example.acmecorporation.model.BookingDTO;
import com.example.acmecorporation.model.BookingRequest;
import com.example.acmecorporation.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<BookingDTO>> getBookings(
            @RequestParam Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<BookingDTO> bookings = bookingService.getBookingsByRoomAndDate(roomId, date);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping()
    public ResponseEntity<Void> createBooking(@RequestBody BookingRequest request) {
        bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
        return ResponseEntity.ok().build();
    }
}
