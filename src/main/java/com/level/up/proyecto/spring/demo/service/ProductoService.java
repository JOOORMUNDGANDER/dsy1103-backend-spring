package com.level.up.proyecto.spring.demo.service;

import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.repository.ProductoRepository;
import com.level.up.proyecto.spring.demo.exception.ResourceNotFoundException;
import com.level.up.proyecto.spring.demo.exception.DuplicateResourceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos
    @Transactional(readOnly = true)
    public List<Producto> obtenerTodos() {
        return productoRepository.findAll();
    }

    // Obtener producto por ID
    @Transactional(readOnly = true)
    public Producto obtenerPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto", "id", id));
    }

    // Verificar si existe un producto
    @Transactional(readOnly = true)
    public boolean existePorId(Long id) {
        return productoRepository.existsById(id);
    }

    // Crear nuevo producto
    public Producto crear(Producto producto) {
        // Verificar si ya existe un producto con el mismo código
        if (productoRepository.existsByCodigo(producto.getCodigo())) {
            throw new DuplicateResourceException("Producto", "codigo", producto.getCodigo());
        }
        return productoRepository.save(producto);
    }

    // Actualizar producto completo
    public Producto actualizar(Long id, Producto nuevosDatos) {
        Producto productoExistente = obtenerPorId(id);

        // Verificar si el nuevo código ya existe en otro producto
        if (!productoExistente.getCodigo().equals(nuevosDatos.getCodigo())
                && productoRepository.existsByCodigo(nuevosDatos.getCodigo())) {
            throw new DuplicateResourceException("Producto", "codigo", nuevosDatos.getCodigo());
        }

        productoExistente.setCodigo(nuevosDatos.getCodigo());
        productoExistente.setCategoria(nuevosDatos.getCategoria());
        productoExistente.setNombre(nuevosDatos.getNombre());
        productoExistente.setPrecio(nuevosDatos.getPrecio());
        productoExistente.setPrecioOriginal(nuevosDatos.getPrecioOriginal());
        productoExistente.setImagen(nuevosDatos.getImagen());
        productoExistente.setDescripcion(nuevosDatos.getDescripcion());
        productoExistente.setDescripcionProducto(nuevosDatos.getDescripcionProducto());
        productoExistente.setOferta(nuevosDatos.getOferta());
        productoExistente.setEspecificaciones(nuevosDatos.getEspecificaciones());
        productoExistente.setStock(nuevosDatos.getStock());
        productoExistente.setActivo(nuevosDatos.getActivo());

        return productoRepository.save(productoExistente);
    }

    // Actualizar producto parcialmente
    public Producto actualizarParcial(Long id, Producto nuevosDatos) {
        Producto productoExistente = obtenerPorId(id);

        // Verificar código duplicado si se está actualizando
        if (nuevosDatos.getCodigo() != null
                && !productoExistente.getCodigo().equals(nuevosDatos.getCodigo())
                && productoRepository.existsByCodigo(nuevosDatos.getCodigo())) {
            throw new DuplicateResourceException("Producto", "codigo", nuevosDatos.getCodigo());
        }

        if (nuevosDatos.getCodigo() != null) {
            productoExistente.setCodigo(nuevosDatos.getCodigo());
        }
        if (nuevosDatos.getCategoria() != null) {
            productoExistente.setCategoria(nuevosDatos.getCategoria());
        }
        if (nuevosDatos.getNombre() != null) {
            productoExistente.setNombre(nuevosDatos.getNombre());
        }
        if (nuevosDatos.getPrecio() != null) {
            productoExistente.setPrecio(nuevosDatos.getPrecio());
        }
        if (nuevosDatos.getPrecioOriginal() != null) {
            productoExistente.setPrecioOriginal(nuevosDatos.getPrecioOriginal());
        }
        if (nuevosDatos.getImagen() != null) {
            productoExistente.setImagen(nuevosDatos.getImagen());
        }
        if (nuevosDatos.getDescripcion() != null) {
            productoExistente.setDescripcion(nuevosDatos.getDescripcion());
        }
        if (nuevosDatos.getDescripcionProducto() != null) {
            productoExistente.setDescripcionProducto(nuevosDatos.getDescripcionProducto());
        }
        if (nuevosDatos.getOferta() != null) {
            productoExistente.setOferta(nuevosDatos.getOferta());
        }
        if (nuevosDatos.getEspecificaciones() != null) {
            productoExistente.setEspecificaciones(nuevosDatos.getEspecificaciones());
        }
        if (nuevosDatos.getStock() != null) {
            productoExistente.setStock(nuevosDatos.getStock());
        }
        if (nuevosDatos.getActivo() != null) {
            productoExistente.setActivo(nuevosDatos.getActivo());
        }

        return productoRepository.save(productoExistente);
    }

    // Eliminar producto
    public void eliminar(Long id) {
        if (!existePorId(id)) {
            throw new ResourceNotFoundException("Producto", "id", id);
        }
        productoRepository.deleteById(id);
    }

    // Buscar por categoría
    @Transactional(readOnly = true)
    public List<Producto> obtenerPorCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Buscar por nombre (contiene)
    @Transactional(readOnly = true)
    public List<Producto> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Obtener productos en oferta
    @Transactional(readOnly = true)
    public List<Producto> obtenerEnOferta() {
        return productoRepository.findByOferta(true);
    }

    // Obtener productos activos
    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivo(true);
    }

    // Buscar por rango de precios
    @Transactional(readOnly = true)
    public List<Producto> buscarPorRangoPrecio(Double precioMin, Double precioMax) {
        return productoRepository.findByPrecioBetween(precioMin, precioMax);
    }
}
