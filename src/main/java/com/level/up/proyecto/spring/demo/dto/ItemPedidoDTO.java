package com.level.up.proyecto.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedidoDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoCodigo;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
