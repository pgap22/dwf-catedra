package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.ReservaAsiento;
import com.udb.aerovia_api.dto.ReservaAsientoDetalleDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PasajeroMapper.class, AsientoAvionMapper.class, TarifaMapper.class})
public interface ReservaAsientoMapper {

    // Mapea la tarifa desde la TarifaOperacion asociada
    @Mapping(source = "tarifaOperacion.tarifa", target = "tarifa")
    // Mapea el asiento desde el AsientoAvion asociado
    @Mapping(source = "asientoAvion", target = "asiento")
    ReservaAsientoDetalleDto toDto(ReservaAsiento reservaAsiento);
}