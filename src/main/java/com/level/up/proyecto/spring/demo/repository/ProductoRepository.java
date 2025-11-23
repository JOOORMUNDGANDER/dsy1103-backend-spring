package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long>, JpaSpecificationExecutor<Producto> {

    // Buscar productos por categoría
    List<Producto> findByCategoria(String categoria);

    // Buscar productos donde el nombre contiene un texto (ignorando mayúsculas)
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Buscar productos en oferta
    List<Producto> findByOferta(Boolean oferta);

    // Buscar productos activos
    List<Producto> findByActivo(Boolean activo);

    // Buscar por código único
    Optional<Producto> findByCodigo(String codigo);

    // Verificar si existe por código
    boolean existsByCodigo(String codigo);

    // Buscar por rango de precios
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);

    // Buscar por categoría y activo
    List<Producto> findByCategoriaAndActivo(String categoria, Boolean activo);

    // Buscar por nombre y categoría
    List<Producto> findByNombreContainingIgnoreCaseAndCategoria(String nombre, String categoria);
}
