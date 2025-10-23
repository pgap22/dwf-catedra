package com.udb.aerovia_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.udb.aerovia_api.domain.OperacionTripulacion;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = { TripulanteMapper.class })
public interface TripulatesOperacionMapper {
    @Mapping(source = "operacionVuelo.id", target = "operacionId")
    OperacionTripulacionDto toDto(OperacionTripulacion entity);
}
