package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Pasajero;
import com.udb.aerovia_api.dto.CreatePasajeroDto;
import com.udb.aerovia_api.dto.PasajeroDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasajeroMapper {

    PasajeroDto toDto(Pasajero pasajero);

    @Mapping(target = "id", ignore = true)
    Pasajero toEntity(CreatePasajeroDto createDto);
}