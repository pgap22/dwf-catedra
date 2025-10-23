package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Clase;
import com.udb.aerovia_api.dto.ClaseDto;
import com.udb.aerovia_api.dto.CreateClaseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClaseMapper {

    ClaseDto toDto(Clase clase);

    @Mapping(target = "id", ignore = true)
    Clase toEntity(CreateClaseDto createDto);
}