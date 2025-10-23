package com.udb.aerovia_api.mapper;

import com.udb.aerovia_api.domain.Usuario;
import com.udb.aerovia_api.dto.UserAdminDto;
import com.udb.aerovia_api.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "rol", expression = "java(usuario.getRol().name())")
    UserDto toDto(Usuario usuario);

    @Mapping(target = "rol", expression = "java(usuario.getRol().name())")
    UserAdminDto toAdminDto(Usuario usuario);
}
