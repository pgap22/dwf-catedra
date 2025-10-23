package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Aeropuerto;
import com.udb.aerovia_api.dto.AeropuertoDto;
import com.udb.aerovia_api.dto.CreateAeropuertoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AeropuertoMapper {

    AeropuertoDto toDto(Aeropuerto aeropuerto);

    @Mapping(target = "id", ignore = true)
    Aeropuerto toEntity(CreateAeropuertoDto createDto);
}