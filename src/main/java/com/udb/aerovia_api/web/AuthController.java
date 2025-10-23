package com.udb.aerovia_api.web;

import com.udb.aerovia_api.dto.ErrorResponseDto;
import com.udb.aerovia_api.dto.JwtAuthResponseDto;
import com.udb.aerovia_api.dto.LoginRequestDto;
import com.udb.aerovia_api.dto.RegisterRequestDto;
import com.udb.aerovia_api.dto.UserDto;
import com.udb.aerovia_api.security.JwtTokenProvider;
import com.udb.aerovia_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Operaciones de ingreso y registro de usuarios")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider,
            UserService userService) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Operation(
            summary = "Autenticar usuario",
            description = "Valida las credenciales de un usuario y devuelve un token JWT."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticacion exitosa",
                    content = @Content(schema = @Schema(implementation = JwtAuthResponseDto.class),
                            examples = @ExampleObject(name = "LoginExitoso", value = """
                                    {
                                      "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
                                      "user": {
                                        "id": 42,
                                        "nombre": "Cliente Demo",
                                        "correo": "cliente@aerovia.test",
                                        "rol": "CLIENTE"
                                      }
                                    }
                                    """))),
            @ApiResponse(responseCode = "401", description = "Credenciales invalidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoginRequestDto.class),
                    examples = @ExampleObject(name = "LoginRequest", value = """
                            {
                              "correo": "cliente@aerovia.test",
                              "password": "Cliente123!"
                            }
                            """)))
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.correo(),
                        loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = tokenProvider.generateToken(authentication);
        UserDto user = userService.getUserByEmail(authentication.getName());

        return ResponseEntity.ok(new JwtAuthResponseDto(token, user));
    }

    @Operation(
            summary = "Registrar usuario",
            description = "Crea un nuevo usuario con rol de cliente."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario registrado",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "409", description = "Correo en uso",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Datos invalidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequestDto.class),
                    examples = @ExampleObject(name = "RegistroCliente", value = """
                            {
                              "nombre": "Cliente Demo",
                              "correo": "cliente@aerovia.test",
                              "password": "Cliente123!"
                            }
                            """)))
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest) {
        UserDto registeredUser = userService.registerUser(registerRequest);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Usuario autenticado",
            description = "Devuelve la informacion del usuario actualmente autenticado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Perfil recuperado",
                    content = @Content(schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "401", description = "Sesion invalida",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserDto> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }
}



