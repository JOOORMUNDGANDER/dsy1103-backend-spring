package com.level.up.proyecto.spring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_pedido")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    private Producto producto;

    // Guardamos los datos del producto al momento de la compra
    private String productoNombre;
    private String productoCodigo;

    private Integer cantidad;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    // Calcular subtotal
    public Double calcularSubtotal() {
        return precioUnitario * cantidad;
    }
}
