package com.udb.aerovia_api.service;

import com.udb.aerovia_api.domain.Usuario;
import com.udb.aerovia_api.domain.enums.Rol;
import com.udb.aerovia_api.dto.CreateUserAdminDto;
import com.udb.aerovia_api.dto.RegisterRequestDto;
import com.udb.aerovia_api.dto.UpdateUserAdminDto;
import com.udb.aerovia_api.dto.UserAdminDto;
import com.udb.aerovia_api.dto.UserDto;
import com.udb.aerovia_api.exception.DuplicateResourceException;
import com.udb.aerovia_api.exception.ResourceNotFoundException;
import com.udb.aerovia_api.mapper.UserMapper;
import com.udb.aerovia_api.repository.UsuarioRepository;
import com.udb.aerovia_api.repository.specification.UsuarioSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    public UserDto registerUser(RegisterRequestDto registerDto) {
        if (usuarioRepository.findByCorreo(registerDto.correo()).isPresent()) {
            throw new DuplicateResourceException("El correo electronico ya esta en uso.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombre(registerDto.nombre());
        nuevoUsuario.setCorreo(registerDto.correo());
        nuevoUsuario.setClaveHash(passwordEncoder.encode(registerDto.password()));
        nuevoUsuario.setRol(Rol.CLIENTE);
        nuevoUsuario.setActivo(true);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        return userMapper.toDto(usuarioGuardado);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(userMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "correo", correo));
    }

    @Transactional(readOnly = true)
    public Page<UserAdminDto> getUsuarios(Pageable pageable, Optional<Rol> rol, Optional<Boolean> activo) {
        Specification<Usuario> spec = null;
        if (rol.isPresent()) {
            spec = combine(spec, UsuarioSpecification.hasRol(rol.get()));
        }
        if (activo.isPresent()) {
            spec = combine(spec, UsuarioSpecification.hasEstado(activo.get()));
        }
        Page<Usuario> pagina = spec == null ? usuarioRepository.findAll(pageable) : usuarioRepository.findAll(spec, pageable);
        return pagina.map(userMapper::toAdminDto);
    }

    @Transactional(readOnly = true)
    public UserAdminDto getUsuario(Long id) {
        return userMapper.toAdminDto(buscarUsuarioPorId(id));
    }

    @Transactional
    public UserAdminDto crearUsuarioAdmin(CreateUserAdminDto dto) {
        verificarCorreoDisponible(dto.correo());

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.nombre());
        usuario.setCorreo(dto.correo());
        usuario.setClaveHash(passwordEncoder.encode(dto.password()));
        usuario.setRol(dto.rol());
        usuario.setActivo(Boolean.TRUE.equals(dto.activo()));

        return userMapper.toAdminDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UserAdminDto actualizarUsuario(Long id, UpdateUserAdminDto dto) {
        Usuario usuario = buscarUsuarioPorId(id);

        if (StringUtils.hasText(dto.nombre())) {
            usuario.setNombre(dto.nombre());
        }
        if (StringUtils.hasText(dto.correo()) && !dto.correo().equalsIgnoreCase(usuario.getCorreo())) {
            verificarCorreoDisponible(dto.correo());
            usuario.setCorreo(dto.correo());
        }
        if (StringUtils.hasText(dto.password())) {
            usuario.setClaveHash(passwordEncoder.encode(dto.password()));
        }
        if (dto.rol() != null) {
            usuario.setRol(dto.rol());
        }

        return userMapper.toAdminDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UserAdminDto activarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setActivo(true);
        return userMapper.toAdminDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public UserAdminDto desactivarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuario.setActivo(false);
        return userMapper.toAdminDto(usuarioRepository.save(usuario));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario", "id", id));
    }

    private void verificarCorreoDisponible(String correo) {
        if (usuarioRepository.findByCorreo(correo).isPresent()) {
            throw new DuplicateResourceException("El correo electronico ya esta en uso.");
        }
    }

    private Specification<Usuario> combine(Specification<Usuario> base, Specification<Usuario> other) {
        if (other == null) {
            return base;
        }
        return base == null ? Specification.where(other) : base.and(other);
    }
}
