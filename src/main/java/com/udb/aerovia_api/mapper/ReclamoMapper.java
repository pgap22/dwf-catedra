package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Reclamo;
import com.udb.aerovia_api.dto.ReclamoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PasajeroMapper.class})
public interface ReclamoMapper {

    @Mapping(source = "reserva.id", target = "reservaId")
    ReclamoDto toDto(Reclamo reclamo);
}