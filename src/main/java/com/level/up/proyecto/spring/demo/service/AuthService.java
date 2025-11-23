package com.level.up.proyecto.spring.demo.service;

import com.level.up.proyecto.spring.demo.dto.JwtRespuestaDTO;
import com.level.up.proyecto.spring.demo.dto.LoginDTO;
import com.level.up.proyecto.spring.demo.dto.RegistroDTO;
import com.level.up.proyecto.spring.demo.exception.BadRequestException;
import com.level.up.proyecto.spring.demo.model.Rol;
import com.level.up.proyecto.spring.demo.model.Usuario;
import com.level.up.proyecto.spring.demo.repository.RolRepository;
import com.level.up.proyecto.spring.demo.repository.UsuarioRepository;
import com.level.up.proyecto.spring.demo.security.jwt.JwtUtils;
import com.level.up.proyecto.spring.demo.security.services.UsuarioDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    // Iniciar sesión
    public JwtRespuestaDTO iniciarSesion(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getNombreUsuario(),
                        loginDTO.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generarToken(authentication);

        UsuarioDetailsImpl userDetails = (UsuarioDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return new JwtRespuestaDTO(
                jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles);
    }

    // Registrar nuevo usuario
    public Usuario registrar(RegistroDTO registroDTO) {
        // Verificar si el nombre de usuario ya existe
        if (usuarioRepository.existsByNombreUsuario(registroDTO.getNombreUsuario())) {
            throw new BadRequestException("El nombre de usuario ya está en uso");
        }

        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Crear nuevo usuario
        Usuario usuario = Usuario.builder()
                .nombreUsuario(registroDTO.getNombreUsuario())
                .email(registroDTO.getEmail())
                .password(passwordEncoder.encode(registroDTO.getPassword()))
                .nombre(registroDTO.getNombre())
                .apellido(registroDTO.getApellido())
                .telefono(registroDTO.getTelefono())
                .direccion(registroDTO.getDireccion())
                .build();

        // Asignar roles
        Set<String> strRoles = registroDTO.getRoles();
        Set<Rol> roles = new HashSet<>();

        if (strRoles == null || strRoles.isEmpty()) {
            // Por defecto asignar rol de usuario
            Rol userRol = rolRepository.findByNombre(Rol.NombreRol.ROL_USUARIO)
                    .orElseThrow(() -> new BadRequestException("Rol no encontrado"));
            roles.add(userRol);
        } else {
            strRoles.forEach(rol -> {
                switch (rol.toLowerCase()) {
                    case "admin":
                        Rol adminRol = rolRepository.findByNombre(Rol.NombreRol.ROL_ADMIN)
                                .orElseThrow(() -> new BadRequestException("Rol ADMIN no encontrado"));
                        roles.add(adminRol);
                        break;
                    case "moderador":
                        Rol modRol = rolRepository.findByNombre(Rol.NombreRol.ROL_MODERADOR)
                                .orElseThrow(() -> new BadRequestException("Rol MODERADOR no encontrado"));
                        roles.add(modRol);
                        break;
                    default:
                        Rol userRol = rolRepository.findByNombre(Rol.NombreRol.ROL_USUARIO)
                                .orElseThrow(() -> new BadRequestException("Rol USUARIO no encontrado"));
                        roles.add(userRol);
                }
            });
        }

        usuario.setRoles(roles);
        return usuarioRepository.save(usuario);
    }
}
