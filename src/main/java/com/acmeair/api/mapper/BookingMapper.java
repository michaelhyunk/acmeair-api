package com.acmeair.api.mapper;

import com.acmeair.api.dto.booking.BookingRequestDto;
import com.acmeair.api.dto.booking.BookingResponseDto;
import com.acmeair.api.model.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "CONFIRMED")
    Booking toModel(BookingRequestDto dto);

    @Mapping(source = "id", target = "bookingId")
    BookingResponseDto toDto(Booking booking);
}
