package com.example.acmecorporation.converter;

import com.example.acmecorporation.domain.Booking;
import com.example.acmecorporation.model.BookingDto;
import org.springframework.stereotype.Component;

@Component
public class BookingToBookingDto {
    private BookingToBookingDto() {
    }

    public static BookingDto convert(Booking booking) {
        BookingDto bookingDTO = new BookingDto();
        bookingDTO.setId(booking.getId());
        bookingDTO.setEmployeeEmail(booking.getEmployeeEmail());
        bookingDTO.setStartTime(booking.getStartTime());
        bookingDTO.setEndTime(booking.getEndTime());

        return bookingDTO;
    }
}
