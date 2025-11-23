package com.level.up.proyecto.spring.demo.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductoCrearDTO {

    @NotBlank(message = "El código del producto es requerido")
    @Size(max = 50, message = "El código no puede exceder 50 caracteres")
    private String codigo;

    @NotBlank(message = "La categoría es requerida")
    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    private String categoria;

    @NotBlank(message = "El nombre del producto es requerido")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;

    @NotNull(message = "El precio es requerido")
    @Positive(message = "El precio debe ser mayor a 0")
    private Double precio;

    @PositiveOrZero(message = "El precio original debe ser mayor o igual a 0")
    private Double precioOriginal;

    @Size(max = 500, message = "La URL de imagen no puede exceder 500 caracteres")
    private String imagen;

    @Size(max = 500, message = "La descripción corta no puede exceder 500 caracteres")
    private String descripcion;

    @Size(max = 2000, message = "La descripción del producto no puede exceder 2000 caracteres")
    private String descripcionProducto;

    private Boolean oferta;

    @Size(max = 2000, message = "Las especificaciones no pueden exceder 2000 caracteres")
    private String especificaciones;

    @PositiveOrZero(message = "El stock debe ser mayor o igual a 0")
    private Integer stock;

    private Boolean activo;
}
