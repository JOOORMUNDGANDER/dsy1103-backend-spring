package com.level.up.proyecto.spring.demo.service;

import com.level.up.proyecto.spring.demo.dto.AgregarItemDTO;
import com.level.up.proyecto.spring.demo.dto.CarritoDTO;
import com.level.up.proyecto.spring.demo.dto.ItemCarritoDTO;
import com.level.up.proyecto.spring.demo.exception.BadRequestException;
import com.level.up.proyecto.spring.demo.exception.ResourceNotFoundException;
import com.level.up.proyecto.spring.demo.model.Carrito;
import com.level.up.proyecto.spring.demo.model.ItemCarrito;
import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.model.Usuario;
import com.level.up.proyecto.spring.demo.repository.CarritoRepository;
import com.level.up.proyecto.spring.demo.repository.ItemCarritoRepository;
import com.level.up.proyecto.spring.demo.repository.ProductoRepository;
import com.level.up.proyecto.spring.demo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private ItemCarritoRepository itemCarritoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Obtener carrito del usuario (lo crea si no existe)
    public CarritoDTO obtenerCarrito(String nombreUsuario) {
        Carrito carrito = obtenerOCrearCarrito(nombreUsuario);
        return convertirADTO(carrito);
    }

    // Agregar item al carrito
    public CarritoDTO agregarItem(String nombreUsuario, AgregarItemDTO itemDTO) {
        Carrito carrito = obtenerOCrearCarrito(nombreUsuario);

        Producto producto = productoRepository.findById(itemDTO.getProductoId())
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", itemDTO.getProductoId()));

        // Verificar stock
        if (producto.getStock() != null && producto.getStock() < itemDTO.getCantidad()) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + producto.getStock());
        }

        // Buscar si ya existe el item en el carrito
        ItemCarrito itemExistente = itemCarritoRepository
                .findByCarritoIdAndProductoId(carrito.getId(), producto.getId())
                .orElse(null);

        if (itemExistente != null) {
            // Actualizar cantidad
            int nuevaCantidad = itemExistente.getCantidad() + itemDTO.getCantidad();
            if (producto.getStock() != null && producto.getStock() < nuevaCantidad) {
                throw new BadRequestException("Stock insuficiente. Disponible: " + producto.getStock());
            }
            itemExistente.setCantidad(nuevaCantidad);
            itemCarritoRepository.save(itemExistente);
        } else {
            // Crear nuevo item
            ItemCarrito nuevoItem = ItemCarrito.builder()
                    .carrito(carrito)
                    .producto(producto)
                    .cantidad(itemDTO.getCantidad())
                    .precioUnitario(producto.getPrecio())
                    .build();
            carrito.agregarItem(nuevoItem);
        }

        carritoRepository.save(carrito);
        return convertirADTO(carrito);
    }

    // Actualizar cantidad de un item
    public CarritoDTO actualizarCantidad(String nombreUsuario, Long itemId, Integer cantidad) {
        if (cantidad < 1) {
            throw new BadRequestException("La cantidad mínima es 1");
        }

        Carrito carrito = obtenerCarritoPorUsuario(nombreUsuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        // Verificar stock
        if (item.getProducto().getStock() != null && item.getProducto().getStock() < cantidad) {
            throw new BadRequestException("Stock insuficiente. Disponible: " + item.getProducto().getStock());
        }

        item.setCantidad(cantidad);
        carritoRepository.save(carrito);
        return convertirADTO(carrito);
    }

    // Eliminar item del carrito
    public CarritoDTO eliminarItem(String nombreUsuario, Long itemId) {
        Carrito carrito = obtenerCarritoPorUsuario(nombreUsuario);

        ItemCarrito item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));

        carrito.eliminarItem(item);
        carritoRepository.save(carrito);
        return convertirADTO(carrito);
    }

    // Vaciar carrito
    public CarritoDTO vaciarCarrito(String nombreUsuario) {
        Carrito carrito = obtenerCarritoPorUsuario(nombreUsuario);
        carrito.getItems().clear();
        carritoRepository.save(carrito);
        return convertirADTO(carrito);
    }

    // Métodos privados de utilidad
    private Carrito obtenerOCrearCarrito(String nombreUsuario) {
        return carritoRepository.findByUsuarioNombreUsuario(nombreUsuario)
                .orElseGet(() -> {
                    Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario", "nombreUsuario", nombreUsuario));
                    Carrito nuevoCarrito = Carrito.builder()
                            .usuario(usuario)
                            .build();
                    return carritoRepository.save(nuevoCarrito);
                });
    }

    private Carrito obtenerCarritoPorUsuario(String nombreUsuario) {
        return carritoRepository.findByUsuarioNombreUsuario(nombreUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Carrito", "usuario", nombreUsuario));
    }

    private CarritoDTO convertirADTO(Carrito carrito) {
        List<ItemCarritoDTO> itemsDTO = carrito.getItems().stream()
                .map(this::convertirItemADTO)
                .collect(Collectors.toList());

        return CarritoDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuario().getId())
                .nombreUsuario(carrito.getUsuario().getNombreUsuario())
                .items(itemsDTO)
                .totalItems(carrito.contarItems())
                .total(carrito.calcularTotal())
                .build();
    }

    private ItemCarritoDTO convertirItemADTO(ItemCarrito item) {
        return ItemCarritoDTO.builder()
                .id(item.getId())
                .productoId(item.getProducto().getId())
                .productoNombre(item.getProducto().getNombre())
                .productoImagen(item.getProducto().getImagen())
                .cantidad(item.getCantidad())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.calcularSubtotal())
                .build();
    }
}
