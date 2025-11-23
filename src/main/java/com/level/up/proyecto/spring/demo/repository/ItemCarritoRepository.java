package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.ItemCarrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {

    // Buscar item por carrito y producto
    Optional<ItemCarrito> findByCarritoIdAndProductoId(Long carritoId, Long productoId);
}
