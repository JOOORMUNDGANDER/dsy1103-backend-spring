package com.level.up.proyecto.spring.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long id;
    private String codigo;
    private String categoria;
    private String nombre;
    private Double precio;
    private Double precioOriginal;
    private String imagen;
    private String descripcion;
    private String descripcionProducto;
    private Boolean oferta;
    private String especificaciones;
    private Integer stock;
    private Boolean activo;

    // Campo calculado para el porcentaje de descuento
    private Integer porcentajeDescuento;
}
