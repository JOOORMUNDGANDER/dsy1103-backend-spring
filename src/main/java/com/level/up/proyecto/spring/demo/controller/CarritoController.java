package com.level.up.proyecto.spring.demo.controller;

import com.level.up.proyecto.spring.demo.dto.AgregarItemDTO;
import com.level.up.proyecto.spring.demo.dto.CarritoDTO;
import com.level.up.proyecto.spring.demo.service.CarritoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    // GET: Obtener carrito del usuario actual
    @GetMapping
    public ResponseEntity<CarritoDTO> obtenerCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(carritoService.obtenerCarrito(userDetails.getUsername()));
    }

    // POST: Agregar item al carrito
    @PostMapping("/items")
    public ResponseEntity<CarritoDTO> agregarItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AgregarItemDTO itemDTO) {
        return ResponseEntity.ok(carritoService.agregarItem(userDetails.getUsername(), itemDTO));
    }

    // PUT: Actualizar cantidad de un item
    @PutMapping("/items/{itemId}")
    public ResponseEntity<CarritoDTO> actualizarCantidad(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @RequestParam Integer cantidad) {
        return ResponseEntity.ok(carritoService.actualizarCantidad(userDetails.getUsername(), itemId, cantidad));
    }

    // DELETE: Eliminar item del carrito
    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CarritoDTO> eliminarItem(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        return ResponseEntity.ok(carritoService.eliminarItem(userDetails.getUsername(), itemId));
    }

    // DELETE: Vaciar carrito
    @DeleteMapping
    public ResponseEntity<CarritoDTO> vaciarCarrito(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(carritoService.vaciarCarrito(userDetails.getUsername()));
    }
}
