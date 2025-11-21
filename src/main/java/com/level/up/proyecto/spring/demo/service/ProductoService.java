package com.level.up.proyecto.spring.demo.service;

import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Obtener todos los productos
    public List<Producto> getAll() {
        return productoRepository.findAll();
    }

    // Crear/guardar producto
    public Producto save(Producto producto) {
        return productoRepository.save(producto);
    }

    // Obtener producto por id
    public Optional<Producto> getById(Long id) {
        return productoRepository.findById(id);
    }

    // Eliminar producto
    public void delete(Long id) {
        productoRepository.deleteById(id);
    }

    // Ejemplo de búsqueda por categoría (opcional)
    public List<Producto> getByCategoria(String categoria) {
        return productoRepository.findByCategoria(categoria);
    }

    // Ejemplo de lógica extra: validación o reglas (puedes agregar aquí)
}
