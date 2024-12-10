package com.example.acmecorporation.common;

import com.example.acmecorporation.domain.Booking;
import com.example.acmecorporation.domain.Room;
import com.example.acmecorporation.model.BookingRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestHelperGenerator {
    public static List<Booking> generateBookingList(LocalDate date) {
        return Arrays.asList(
                new Booking(1L,
                        Room.builder().id(1L).name("Conference Room A").build(),
                        "alice@example.com",
                        date,
                        LocalTime.of(9, 0),
                        LocalTime.of(11, 0)),
                new Booking(2L, Room.builder().id(1L).name("Conference Room A").build(),
                        "bob@example.com",
                        date,
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0))
        );
    }

    public static BookingRequest generateBookingRequest(Long roomId) {
        return BookingRequest.builder()
                .roomId(roomId)
                .employeeEmail("alice@example.com")
                .date(LocalDate.of(2024, 12, 8))
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(11, 0))
                .build();
    }
}
