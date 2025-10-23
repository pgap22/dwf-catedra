package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.TarifaOperacion;
import com.udb.aerovia_api.dto.TarifaOperacionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {TarifaMapper.class})
public interface TarifaOperacionMapper {

    @Mapping(source = "operacionVuelo.id", target = "operacionId")
    TarifaOperacionDto toDto(TarifaOperacion tarifaOperacion);
}