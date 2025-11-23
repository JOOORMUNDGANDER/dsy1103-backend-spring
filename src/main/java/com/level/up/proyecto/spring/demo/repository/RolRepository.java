package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    // Buscar rol por nombre
    Optional<Rol> findByNombre(Rol.NombreRol nombre);
}
