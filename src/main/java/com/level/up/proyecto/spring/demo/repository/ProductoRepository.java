package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    // Buscar productos por categoría exacta
    List<Producto> findByCategoria(String categoria);

    // Buscar productos donde el nombre contiene un texto (opcional)
    List<Producto> findByNombreContaining(String texto);

    // Buscar productos en oferta
    List<Producto> findByOferta(Boolean oferta);
}
