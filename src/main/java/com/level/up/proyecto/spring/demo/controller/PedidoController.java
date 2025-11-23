package com.level.up.proyecto.spring.demo.controller;

import com.level.up.proyecto.spring.demo.dto.CrearPedidoDTO;
import com.level.up.proyecto.spring.demo.dto.PedidoDTO;
import com.level.up.proyecto.spring.demo.model.Pedido;
import com.level.up.proyecto.spring.demo.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    // POST: Crear pedido desde carrito
    @PostMapping
    public ResponseEntity<PedidoDTO> crearPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CrearPedidoDTO crearPedidoDTO) {
        PedidoDTO pedido = pedidoService.crearPedido(userDetails.getUsername(), crearPedidoDTO);
        return new ResponseEntity<>(pedido, HttpStatus.CREATED);
    }

    // GET: Obtener mis pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> obtenerMisPedidos(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(pedidoService.obtenerPedidosUsuario(userDetails.getUsername()));
    }

    // GET: Obtener pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> obtenerPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(userDetails.getUsername(), id));
    }

    // GET: Obtener pedido por número
    @GetMapping("/numero/{numeroPedido}")
    public ResponseEntity<PedidoDTO> obtenerPorNumero(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String numeroPedido) {
        return ResponseEntity.ok(pedidoService.obtenerPorNumeroPedido(userDetails.getUsername(), numeroPedido));
    }

    // PUT: Cancelar pedido
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<PedidoDTO> cancelarPedido(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(userDetails.getUsername(), id));
    }

    // ADMIN: Obtener todos los pedidos
    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PedidoDTO>> obtenerTodosPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerTodosPedidos());
    }

    // ADMIN: Actualizar estado del pedido
    @PutMapping("/admin/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PedidoDTO> actualizarEstado(
            @PathVariable Long id,
            @RequestParam Pedido.EstadoPedido estado) {
        return ResponseEntity.ok(pedidoService.actualizarEstado(id, estado));
    }
}
