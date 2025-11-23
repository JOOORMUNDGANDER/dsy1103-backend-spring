package com.level.up.proyecto.spring.demo.dto;

import com.level.up.proyecto.spring.demo.model.Pedido;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDTO {

    private Long id;
    private String numeroPedido;
    private Long usuarioId;
    private String nombreUsuario;
    private List<ItemPedidoDTO> items;
    private Pedido.EstadoPedido estado;
    private Double subtotal;
    private Double descuento;
    private Double total;
    private String direccionEnvio;
    private String notas;
    private LocalDateTime fechaPedido;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;
}
