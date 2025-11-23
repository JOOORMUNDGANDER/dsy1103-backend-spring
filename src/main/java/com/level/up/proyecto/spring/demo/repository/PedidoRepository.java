package com.level.up.proyecto.spring.demo.repository;

import com.level.up.proyecto.spring.demo.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar pedidos por usuario
    List<Pedido> findByUsuarioIdOrderByFechaPedidoDesc(Long usuarioId);

    // Buscar pedidos por nombre de usuario
    List<Pedido> findByUsuarioNombreUsuarioOrderByFechaPedidoDesc(String nombreUsuario);

    // Buscar por número de pedido
    Optional<Pedido> findByNumeroPedido(String numeroPedido);

    // Buscar pedidos por estado
    List<Pedido> findByEstado(Pedido.EstadoPedido estado);

    // Contar pedidos de un usuario
    long countByUsuarioId(Long usuarioId);
}
