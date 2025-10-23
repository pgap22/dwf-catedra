package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Avion;
import com.udb.aerovia_api.dto.AvionDto;
import com.udb.aerovia_api.dto.CreateAvionDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AvionMapper {

    AvionDto toDto(Avion avion);

    @Mapping(target = "id", ignore = true)
    Avion toEntity(CreateAvionDto createDto);
}