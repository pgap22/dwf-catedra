package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Ruta;
import com.udb.aerovia_api.dto.RutaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {AeropuertoMapper.class})
public interface RutaMapper {

    RutaDto toDto(Ruta ruta);
}