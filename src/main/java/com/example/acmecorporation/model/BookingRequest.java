package com.example.acmecorporation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    private Long roomId;
    private String employeeEmail;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
}
