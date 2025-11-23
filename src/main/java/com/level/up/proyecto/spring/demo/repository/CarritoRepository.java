package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarritoRepository extends JpaRepository<Carrito, Long> {

    // Buscar carrito por usuario
    Optional<Carrito> findByUsuarioId(Long usuarioId);

    // Buscar carrito por nombre de usuario
    Optional<Carrito> findByUsuarioNombreUsuario(String nombreUsuario);
}
