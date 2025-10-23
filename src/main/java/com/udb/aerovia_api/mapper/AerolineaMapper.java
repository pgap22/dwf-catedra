package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Aerolinea;
import com.udb.aerovia_api.dto.AerolineaDto;
import com.udb.aerovia_api.dto.CreateAerolineaDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AerolineaMapper {

    AerolineaDto toDto(Aerolinea aerolinea);

    @Mapping(target = "id", ignore = true)
    Aerolinea toEntity(CreateAerolineaDto createAerolineaDto);
}