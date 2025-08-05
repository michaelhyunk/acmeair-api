package com.acmeair.api.mapper;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.dto.booking.BookingResponseDto;
import com.acmeair.api.dto.booking.BookingUpdateDto;
import com.acmeair.api.model.Booking;
import com.acmeair.api.model.Passenger;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "CONFIRMED")
    Booking toModel(BookingRequestDto dto);

    @Mapping(source = "id", target = "bookingId")
    BookingResponseDto toDto(Booking booking);

    default void updateBooking(@MappingTarget Booking booking, BookingUpdateDto dto) {
        Passenger existing = booking.getPassenger();
        Passenger incoming = dto.getPassenger();

        existing.setEmail(incoming.getEmail());
        existing.setPassengerNote(incoming.getPassengerNote());
    }
}
