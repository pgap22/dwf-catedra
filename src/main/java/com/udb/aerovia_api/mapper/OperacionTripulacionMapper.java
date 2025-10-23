package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.OperacionTripulacion;
import com.udb.aerovia_api.dto.OperacionTripulacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TripulanteMapper.class})
public interface OperacionTripulacionMapper {

    @Mapping(source = "operacionVuelo.id", target = "operacionId")
    OperacionTripulacionDto toDto(OperacionTripulacion operacionTripulacion);
}