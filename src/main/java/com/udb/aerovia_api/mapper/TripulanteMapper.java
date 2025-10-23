package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Tripulante;
import com.udb.aerovia_api.dto.CreateTripulanteDto;
import com.udb.aerovia_api.dto.TripulanteDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TripulanteMapper {

    @Mapping(target = "id", ignore = true)
    Tripulante toEntity(CreateTripulanteDto createDto);

    TripulanteDto toDto(Tripulante tripulante);
}