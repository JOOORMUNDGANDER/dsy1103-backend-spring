package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Buscar usuario por nombre de usuario
    Optional<Usuario> findByNombreUsuario(String nombreUsuario);

    // Buscar usuario por email
    Optional<Usuario> findByEmail(String email);

    // Verificar si existe por nombre de usuario
    Boolean existsByNombreUsuario(String nombreUsuario);

    // Verificar si existe por email
    Boolean existsByEmail(String email);
}
