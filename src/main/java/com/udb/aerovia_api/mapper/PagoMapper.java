package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Pago;
import com.udb.aerovia_api.dto.PagoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagoMapper {

    @Mapping(source = "reserva.id", target = "reservaId")
    PagoDto toDto(Pago pago);
}