package com.udb.aerovia_api.web;

import com.udb.aerovia_api.domain.enums.Rol;
import com.udb.aerovia_api.dto.CreateUserAdminDto;
import com.udb.aerovia_api.dto.ErrorResponseDto;
import com.udb.aerovia_api.dto.UpdateUserAdminDto;
import com.udb.aerovia_api.dto.UserAdminDto;
import com.udb.aerovia_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/usuarios")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Usuarios", description = "Administracion de usuarios del sistema (Rol requerido: ADMIN)")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Crear usuario", description = "Crea un nuevo usuario con rol y estado seleccionados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado correctamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAdminDto.class),
                            examples = @ExampleObject(name = "UsuarioAdminCreado", value = """
                                    {
                                      "id": 7,
                                      "nombre": "Ana Morales",
                                      "correo": "ana.morales@aerovia.test",
                                      "rol": "ADMIN",
                                      "activo": true
                                    }
                                    """))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Correo en uso",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CreateUserAdminDto.class),
                    examples = @ExampleObject(name = "CrearUsuarioAdmin", value = """
                            {
                              "nombre": "Ana Morales",
                              "correo": "ana.morales@aerovia.test",
                              "password": "Admin123!",
                              "rol": "ADMIN",
                              "activo": true
                            }
                            """)))
    @PostMapping
    public ResponseEntity<UserAdminDto> createUser(@Valid @RequestBody CreateUserAdminDto dto) {
        UserAdminDto created = userService.crearUsuarioAdmin(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar usuarios", description = "Obtiene usuarios paginados con filtros opcionales de rol y estado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Listado recuperado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserAdminDto.class),
                            examples = @ExampleObject(name = "PaginaUsuariosAdmin", value = """
                                    {
                                      "content": [
                                        {
                                          "id": 1,
                                          "nombre": "Admin API",
                                          "correo": "admin@aerovia.test",
                                          "rol": "ADMIN",
                                          "activo": true
                                        }
                                      ],
                                      "pageable": {
                                        "pageNumber": 0,
                                        "pageSize": 20
                                      },
                                      "totalElements": 1,
                                      "totalPages": 1,
                                      "last": true,
                                      "first": true,
                                      "size": 20,
                                      "number": 0,
                                      "sort": {
                                        "empty": false,
                                        "sorted": true,
                                        "unsorted": false
                                      },
                                      "numberOfElements": 1,
                                      "empty": false
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<UserAdminDto>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "nombre") String sort,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction,
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean activo) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort));
        Optional<Rol> rolEnum = Optional.ofNullable(parseRol(rol));
        Page<UserAdminDto> result = userService.getUsuarios(pageable, rolEnum, Optional.ofNullable(activo));
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Obtener usuario", description = "Devuelve la informacion de un usuario por su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado",
                    content = @Content(schema = @Schema(implementation = UserAdminDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserAdminDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUsuario(id));
    }

    @Operation(summary = "Actualizar usuario", description = "Modifica los datos generales, rol o contrasena de un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario actualizado",
                    content = @Content(schema = @Schema(implementation = UserAdminDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Correo en uso",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateUserAdminDto.class),
                    examples = @ExampleObject(name = "ActualizarUsuarioAdmin", value = """
                            {
                              "nombre": "Ana Morales",
                              "correo": "ana.morales@aerovia.test",
                              "rol": "AGENTE"
                            }
                            """)))
    @PutMapping("/{id}")
    public ResponseEntity<UserAdminDto> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserAdminDto dto) {
        return ResponseEntity.ok(userService.actualizarUsuario(id, dto));
    }

    @Operation(summary = "Activar usuario", description = "Marca al usuario como activo para permitir su acceso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario activado",
                    content = @Content(schema = @Schema(implementation = UserAdminDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{id}/activar")
    public ResponseEntity<UserAdminDto> activateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.activarUsuario(id));
    }

    @Operation(summary = "Desactivar usuario", description = "Marca al usuario como inactivo para impedir su acceso.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario desactivado",
                    content = @Content(schema = @Schema(implementation = UserAdminDto.class))),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<UserAdminDto> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.desactivarUsuario(id));
    }

    @Operation(summary = "Eliminar usuario", description = "Elimina definitivamente un usuario.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "401", description = "No autenticado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "No autorizado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    private Rol parseRol(String rol) {
        if (!StringUtils.hasText(rol)) {
            return null;
        }
        try {
            return Rol.valueOf(rol.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol invalido: " + rol);
        }
    }
}


