package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Cancelacion;
import com.udb.aerovia_api.dto.CancelacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CancelacionMapper {

    @Mapping(source = "reserva.id", target = "reservaId")
    CancelacionDto toDto(Cancelacion cancelacion);
}