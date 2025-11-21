package com.level.up.proyecto.spring.demo.controller;

import com.level.up.proyecto.spring.demo.model.Producto;
import com.level.up.proyecto.spring.demo.service.ProductoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET: Todos los productos
    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.getAll();
    }

    // GET: Producto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getById(id);
        return producto.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Crear nuevo producto completo
    @PostMapping
    public Producto createProducto(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    // PUT: Actualizar todos los datos de un producto (excepto id)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable Long id, @RequestBody Producto nuevoProducto) {
        Optional<Producto> productoActual = productoService.getById(id);
        if (productoActual.isPresent()) {
            Producto producto = productoActual.get();
            producto.setCodigo(nuevoProducto.getCodigo());
            producto.setCategoria(nuevoProducto.getCategoria());
            producto.setNombre(nuevoProducto.getNombre());
            producto.setPrecio(nuevoProducto.getPrecio());
            producto.setPrecioOriginal(nuevoProducto.getPrecioOriginal());
            producto.setImagen(nuevoProducto.getImagen());
            producto.setDescripcion(nuevoProducto.getDescripcion());
            producto.setDescripcionProducto(nuevoProducto.getDescripcionProducto());
            producto.setOferta(nuevoProducto.getOferta());
            producto.setEspecificaciones(nuevoProducto.getEspecificaciones());

            Producto actualizado = productoService.save(producto);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE: Eliminar producto por id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
        Optional<Producto> producto = productoService.getById(id);
        if (producto.isPresent()) {
            productoService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
