package com.acmeair.api.mapper;

import com.acmeair.api.model.Flight;
import com.acmeair.api.dto.flight.FlightResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    @Mapping(source = "id", target = "flightId")
    FlightResponseDto toDto(Flight flight);
}
