package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Reserva;
import com.udb.aerovia_api.dto.ReservaDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OperacionVueloMapper.class, ReservaAsientoMapper.class})
public interface ReservaMapper {

    @Mapping(target = "asientos", ignore = true) // Se completan en el servicio
    @Mapping(target = "operacionVuelo", ignore = true) // Se adjunta luego de mapear los asientos
    ReservaDetalleDto toDto(Reserva reserva);
}
