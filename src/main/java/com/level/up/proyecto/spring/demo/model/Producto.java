package com.level.up.proyecto.spring.demo.model;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String codigo;
    private String categoria;
    private String nombre;
    private double precio;
    private Double precioOriginal;           // Usa Double para campos opcionales
    private String imagen;
    private String descripcion;
    private String descripcionProducto;
    private Boolean oferta;
    @Column(length = 2000)                   // Para textos largos tipo JSON (opcional)
    private String especificaciones;
}
