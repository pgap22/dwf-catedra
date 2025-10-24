package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Pasajero;
import com.udb.aerovia_api.domain.Usuario;
import com.udb.aerovia_api.dto.CreatePasajeroDto;
import com.udb.aerovia_api.dto.PasajeroDto;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.PasajeroMapper;
import com.udb.aerovia_api.repository.PasajeroRepository;
import com.udb.aerovia_api.repository.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PasajeroService {

    private final PasajeroRepository pasajeroRepository;
    private final PasajeroMapper pasajeroMapper;
    private final UsuarioRepository usuarioRepository;

    public PasajeroService(PasajeroRepository pasajeroRepository,
                           PasajeroMapper pasajeroMapper,
                           UsuarioRepository usuarioRepository) {
        this.pasajeroRepository = pasajeroRepository;
        this.pasajeroMapper = pasajeroMapper;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public PasajeroDto crearPasajero(CreatePasajeroDto createDto) {
        Usuario usuario = obtenerUsuarioAutenticado();
        Pasajero nuevoPasajero = pasajeroMapper.toEntity(createDto);
        nuevoPasajero.setUsuario(usuario);
        return pasajeroMapper.toDto(pasajeroRepository.save(nuevoPasajero));
    }

    @Transactional(readOnly = true)
    public List<PasajeroDto> obtenerTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (esAdminOAgente(authentication)) {
            return pasajeroRepository.findAll().stream()
                    .map(pasajeroMapper::toDto)
                    .collect(Collectors.toList());
        }

        Usuario usuario = obtenerUsuarioAutenticado(authentication);
        return pasajeroRepository.findByUsuarioId(usuario.getId()).stream()
                .map(pasajeroMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PasajeroDto obtenerPorId(Long id) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!esAdminOAgente(authentication) && !esPropietario(pasajero, authentication)) {
            throw new AccessDeniedException("No tiene permisos para consultar este pasajero.");
        }

        return pasajeroMapper.toDto(pasajero);
    }

    @Transactional
    public PasajeroDto actualizarPasajero(Long id, CreatePasajeroDto updateDto) {
        Pasajero pasajeroExistente = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!esAdminOAgente(authentication) && !esPropietario(pasajeroExistente, authentication)) {
            throw new AccessDeniedException("No tiene permisos para modificar este pasajero.");
        }

        pasajeroExistente.setNombreCompleto(updateDto.nombreCompleto());
        pasajeroExistente.setFechaNacimiento(updateDto.fechaNacimiento());
        pasajeroExistente.setNroPasaporte(updateDto.nroPasaporte());

        return pasajeroMapper.toDto(pasajeroRepository.save(pasajeroExistente));
    }

    @Transactional
    public void eliminarPasajero(Long id) {
        Pasajero pasajero = pasajeroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pasajero", "id", id));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!esAdminOAgente(authentication) && !esPropietario(pasajero, authentication)) {
            throw new AccessDeniedException("No tiene permisos para eliminar este pasajero.");
        }

        pasajeroRepository.deleteById(id);
    }

    private Usuario obtenerUsuarioAutenticado() {
        return obtenerUsuarioAutenticado(SecurityContextHolder.getContext().getAuthentication());
    }

    private Usuario obtenerUsuarioAutenticado(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("No existe un usuario autenticado en el contexto.");
        }
        String correo = authentication.getName();
        return usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));
    }

    private boolean esAdminOAgente(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority) || "ROLE_AGENTE".equals(authority));
    }

    private boolean esPropietario(Pasajero pasajero, Authentication authentication) {
        if (pasajero == null || authentication == null) {
            return false;
        }
        Usuario usuarioActual = obtenerUsuarioAutenticado(authentication);
        return pasajero.getUsuario() != null && pasajero.getUsuario().getId().equals(usuarioActual.getId());
    }
}
