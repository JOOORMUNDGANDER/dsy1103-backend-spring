package com.level.up.proyecto.spring.demo.controller;

import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET: Obtener todos los productos
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        return ResponseEntity.ok(productoService.obtenerTodos());
    }

    // GET: Obtener producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(productoService.obtenerPorId(id));
    }

    // GET: Obtener productos por categoría
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<Producto>> obtenerPorCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(productoService.obtenerPorCategoria(categoria));
    }

    // GET: Obtener productos en oferta
    @GetMapping("/ofertas")
    public ResponseEntity<List<Producto>> obtenerEnOferta() {
        return ResponseEntity.ok(productoService.obtenerEnOferta());
    }

    // GET: Buscar productos por nombre
    @GetMapping("/buscar")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarPorNombre(nombre));
    }

    // GET: Buscar productos por rango de precios
    @GetMapping("/precio")
    public ResponseEntity<List<Producto>> buscarPorRangoPrecio(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(productoService.buscarPorRangoPrecio(min, max));
    }

    // POST: Crear nuevo producto
    @PostMapping
    public ResponseEntity<Producto> crear(@Valid @RequestBody Producto producto) {
        Producto nuevoProducto = productoService.crear(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // PUT: Actualizar producto completo
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizar(id, producto));
    }

    // PATCH: Actualizar producto parcialmente
    @PatchMapping("/{id}")
    public ResponseEntity<Producto> actualizarParcial(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarParcial(id, producto));
    }

    // DELETE: Eliminar producto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
