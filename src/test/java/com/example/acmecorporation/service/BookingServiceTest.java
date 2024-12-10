package com.example.acmecorporation.service;

import com.example.acmecorporation.domain.Booking;
import com.example.acmecorporation.domain.Room;
import com.example.acmecorporation.model.BookingDto;
import com.example.acmecorporation.model.BookingRequest;
import com.example.acmecorporation.repository.BookingRepository;
import com.example.acmecorporation.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.example.acmecorporation.common.TestHelperGenerator.generateBookingList;
import static com.example.acmecorporation.common.TestHelperGenerator.generateBookingRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private RoomRepository roomRepository;
    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Test
    void getBookingsByRoomAndDate_shouldReturnListOfBookingDTOs() {
        // Arrange
        Long roomId = 1L;
        LocalDate date = LocalDate.of(2024, 12, 8);

        // Mock the repository response
        List<Booking> mockBookings = generateBookingList(date);
        when(bookingRepository.findByRoom_IdAndDate(roomId, date)).thenReturn(mockBookings);

        // Act
        List<BookingDto> result = bookingService.getBookingsByRoomAndDate(roomId, date);

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("alice@example.com", result.get(0).getEmployeeEmail());
        assertEquals(LocalTime.of(9, 0), result.get(0).getStartTime());
        assertEquals(LocalTime.of(11, 0), result.get(0).getEndTime());

        assertEquals(2L, result.get(1).getId());
        assertEquals("bob@example.com", result.get(1).getEmployeeEmail());
        assertEquals(LocalTime.of(12, 0), result.get(1).getStartTime());
        assertEquals(LocalTime.of(13, 0), result.get(1).getEndTime());

        verify(bookingRepository, times(1)).findByRoom_IdAndDate(roomId, date);
    }

    @Test
    void createBooking_shouldSaveBookingWhenRequestIsValid() {
        // Arrange
        Room mockRoom = new Room();
        mockRoom.setId(1L);
        mockRoom.setName("Conference Room A");

        BookingRequest request = generateBookingRequest(1L);

        Booking mockBooking = Booking.builder()
                .room(mockRoom)
                .employeeEmail("alice@example.com")
                .date(request.getDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .build();

        when(roomRepository.findById(1L)).thenReturn(Optional.of(mockRoom));
        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        // Act
        bookingService.createBooking(request);

        // Assert
        verify(roomRepository, times(1)).findById(1L);
        verify(bookingRepository, times(1)).save(argThat(booking ->
                booking.getRoom().equals(mockRoom) &&
                        booking.getEmployeeEmail().equals("alice@example.com") &&
                        booking.getDate().equals(request.getDate()) &&
                        booking.getStartTime().equals(request.getStartTime()) &&
                        booking.getEndTime().equals(request.getEndTime())
        ));
    }

    @Test
    void createBooking_shouldThrowExceptionWhenRoomNotFound() {
        // Arrange
        BookingRequest request = generateBookingRequest(999L);

        when(roomRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.createBooking(request));
        verify(roomRepository, times(1)).findById(999L);
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void cancelBooking_shouldDeleteBookingWhenValid() {
        // Arrange
        Long bookingId = 1L;
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setDate(LocalDate.now().plusDays(1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        // Act
        bookingService.cancelBooking(bookingId);

        // Assert
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, times(1)).delete(mockBooking);
    }

    @Test
    void cancelBooking_shouldThrowExceptionWhenBookingNotFound() {
        // Arrange
        Long bookingId = 999L;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> bookingService.cancelBooking(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, never()).delete(any(Booking.class));
    }

    @Test
    void cancelBooking_shouldThrowExceptionWhenBookingIsInThePast() {
        // Arrange
        Long bookingId = 1L;
        Booking mockBooking = new Booking();
        mockBooking.setId(bookingId);
        mockBooking.setDate(LocalDate.now().minusDays(1));

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(mockBooking));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> bookingService.cancelBooking(bookingId));
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, never()).delete(any(Booking.class));
    }
}
