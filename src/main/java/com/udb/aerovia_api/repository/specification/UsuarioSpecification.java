package com.udb.aerovia_api.repository.specification;

import com.udb.aerovia_api.domain.Usuario;
import com.udb.aerovia_api.domain.enums.Rol;
import org.springframework.data.jpa.domain.Specification;

public final class UsuarioSpecification {

    private UsuarioSpecification() {
    }

    public static Specification<Usuario> hasRol(Rol rol) {
        return (root, query, builder) -> rol == null ? null : builder.equal(root.get("rol"), rol);
    }

    public static Specification<Usuario> hasEstado(Boolean activo) {
        return (root, query, builder) -> activo == null ? null : builder.equal(root.get("activo"), activo);
    }
}
