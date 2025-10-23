package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Tarifa;
import com.udb.aerovia_api.dto.TarifaDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {ClaseMapper.class})
public interface TarifaMapper {

    TarifaDto toDto(Tarifa tarifa);
}