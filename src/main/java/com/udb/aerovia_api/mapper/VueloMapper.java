package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Vuelo;
import com.udb.aerovia_api.dto.VueloDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AerolineaMapper.class, RutaMapper.class})
public interface VueloMapper {

    VueloDto toDto(Vuelo vuelo);
}