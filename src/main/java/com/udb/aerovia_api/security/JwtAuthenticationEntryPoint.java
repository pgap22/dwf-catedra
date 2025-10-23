package com.udb.aerovia_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.udb.aerovia_api.dto.ErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // Inyectamos el ObjectMapper global que acabamos de configurar
    private final ObjectMapper mapper;

    public JwtAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        ErrorResponseDto errorResponse = new ErrorResponseDto(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Se requiere autenticación para acceder a este recurso.",
                request.getRequestURI(),
                null
        );

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8"); 
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // Usamos el mapper inyectado para escribir la respuesta
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}