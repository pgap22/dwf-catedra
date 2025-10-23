package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.AsientoAvion;
import com.udb.aerovia_api.dto.AsientoAvionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ClaseMapper.class})
public interface AsientoAvionMapper {

    @Mapping(source = "avion.id", target = "avionId")
    AsientoAvionDto toDto(AsientoAvion asientoAvion);
}