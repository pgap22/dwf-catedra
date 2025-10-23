package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.OperacionVuelo;
import com.udb.aerovia_api.dto.OperacionVueloDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {VueloMapper.class, AvionMapper.class})
public interface OperacionVueloMapper {
    OperacionVueloDto toDto(OperacionVuelo operacionVuelo);
}