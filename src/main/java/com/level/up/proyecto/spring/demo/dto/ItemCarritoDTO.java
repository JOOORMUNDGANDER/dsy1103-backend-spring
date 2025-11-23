package com.level.up.proyecto.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarritoDTO {

    private Long id;
    private Long productoId;
    private String productoNombre;
    private String productoImagen;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
}
