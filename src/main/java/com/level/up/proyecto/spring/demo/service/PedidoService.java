package com.level.up.proyecto.spring.demo.service;

import com.level.up.proyecto.spring.demo.dto.CrearPedidoDTO;
import com.level.up.proyecto.spring.demo.dto.ItemPedidoDTO;
import com.level.up.proyecto.spring.demo.dto.PedidoDTO;
import com.level.up.proyecto.spring.demo.exception.BadRequestException;
import com.level.up.proyecto.spring.demo.exception.ResourceNotFoundException;
import com.level.up.proyecto.spring.demo.model.*;
import com.level.up.proyecto.spring.demo.repository.CarritoRepository;
import com.level.up.proyecto.spring.demo.repository.PedidoRepository;
import com.level.up.proyecto.spring.demo.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Crear pedido desde el carrito
    public PedidoDTO crearPedido(String nombreUsuario, CrearPedidoDTO crearPedidoDTO) {
        Carrito carrito = carritoRepository.findByUsuarioNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", "usuario", nombreUsuario));

        if (carrito.getItems().isEmpty()) {
            throw new BadRequestException("El carrito está vacío");
        }

        // Crear pedido
        Pedido pedido = Pedido.builder()
                .numeroPedido(generarNumeroPedido())
                .usuario(carrito.getUsuario())
                .direccionEnvio(crearPedidoDTO.getDireccionEnvio())
                .notas(crearPedidoDTO.getNotas())
                .estado(Pedido.EstadoPedido.PENDIENTE)
                .build();

        // Convertir items del carrito a items del pedido
        for (ItemCarrito itemCarrito : carrito.getItems()) {
            Producto producto = itemCarrito.getProducto();

            // Verificar stock
            if (producto.getStock() != null && producto.getStock() < itemCarrito.getCantidad()) {
                throw new BadRequestException(
                        "Stock insuficiente para " + producto.getNombre() +
                                ". Disponible: " + producto.getStock());
            }

            ItemPedido itemPedido = ItemPedido.builder()
                    .producto(producto)
                    .productoNombre(producto.getNombre())
                    .productoCodigo(producto.getCodigo())
                    .cantidad(itemCarrito.getCantidad())
                    .precioUnitario(itemCarrito.getPrecioUnitario())
                    .build();

            pedido.agregarItem(itemPedido);

            // Actualizar stock
            if (producto.getStock() != null) {
                producto.setStock(producto.getStock() - itemCarrito.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.calcularTotales();
        Pedido pedidoGuardado = pedidoRepository.save(pedido);

        // Vaciar carrito
        carrito.getItems().clear();
        carritoRepository.save(carrito);

        return convertirADTO(pedidoGuardado);
    }

    // Obtener pedidos del usuario
    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerPedidosUsuario(String nombreUsuario) {
        return pedidoRepository.findByUsuarioNombreUsuarioOrderByFechaPedidoDesc(nombreUsuario)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener pedido por ID
    @Transactional(readOnly = true)
    public PedidoDTO obtenerPedidoPorId(String nombreUsuario, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        // Verificar que el pedido pertenezca al usuario
        if (!pedido.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new BadRequestException("No tienes permiso para ver este pedido");
        }

        return convertirADTO(pedido);
    }

    // Obtener pedido por número
    @Transactional(readOnly = true)
    public PedidoDTO obtenerPorNumeroPedido(String nombreUsuario, String numeroPedido) {
        Pedido pedido = pedidoRepository.findByNumeroPedido(numeroPedido)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "numeroPedido", numeroPedido));

        if (!pedido.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new BadRequestException("No tienes permiso para ver este pedido");
        }

        return convertirADTO(pedido);
    }

    // Cancelar pedido
    public PedidoDTO cancelarPedido(String nombreUsuario, Long pedidoId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        if (!pedido.getUsuario().getNombreUsuario().equals(nombreUsuario)) {
            throw new BadRequestException("No tienes permiso para cancelar este pedido");
        }

        if (pedido.getEstado() == Pedido.EstadoPedido.ENVIADO ||
                pedido.getEstado() == Pedido.EstadoPedido.ENTREGADO) {
            throw new BadRequestException("No se puede cancelar un pedido que ya fue enviado");
        }

        // Restaurar stock
        for (ItemPedido item : pedido.getItems()) {
            Producto producto = item.getProducto();
            if (producto.getStock() != null) {
                producto.setStock(producto.getStock() + item.getCantidad());
                productoRepository.save(producto);
            }
        }

        pedido.setEstado(Pedido.EstadoPedido.CANCELADO);
        return convertirADTO(pedidoRepository.save(pedido));
    }

    // Actualizar estado del pedido (solo admin)
    public PedidoDTO actualizarEstado(Long pedidoId, Pedido.EstadoPedido nuevoEstado) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido", "id", pedidoId));

        pedido.setEstado(nuevoEstado);

        if (nuevoEstado == Pedido.EstadoPedido.ENVIADO) {
            pedido.setFechaEnvio(LocalDateTime.now());
        } else if (nuevoEstado == Pedido.EstadoPedido.ENTREGADO) {
            pedido.setFechaEntrega(LocalDateTime.now());
        }

        return convertirADTO(pedidoRepository.save(pedido));
    }

    // Obtener todos los pedidos (admin)
    @Transactional(readOnly = true)
    public List<PedidoDTO> obtenerTodosPedidos() {
        return pedidoRepository.findAll().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Métodos privados
    private String generarNumeroPedido() {
        return "PED-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private PedidoDTO convertirADTO(Pedido pedido) {
        List<ItemPedidoDTO> itemsDTO = pedido.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());

        return PedidoDTO.builder()
                .id(pedido.getId())
                .numeroPedido(pedido.getNumeroPedido())
                .usuarioId(pedido.getUsuario().getId())
                .nombreUsuario(pedido.getUsuario().getNombreUsuario())
                .items(itemsDTO)
                .estado(pedido.getEstado())
                .subtotal(pedido.getSubtotal())
                .descuento(pedido.getDescuento())
                .total(pedido.getTotal())
                .direccionEnvio(pedido.getDireccionEnvio())
                .notas(pedido.getNotas())
                .fechaPedido(pedido.getFechaPedido())
                .fechaEnvio(pedido.getFechaEnvio())
                .fechaEntrega(pedido.getFechaEntrega())
                .build();
    }

    private ItemPedidoDTO convertirItemADTO(ItemPedido item) {
        return ItemPedidoDTO.builder()
                .id(item.getId())
                .productoId(item.getProducto().getId())
                .productoNombre(item.getProductoNombre())
                .productoCodigo(item.getProductoCodigo())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.calcularSubtotal())
                .build();
    }
}
