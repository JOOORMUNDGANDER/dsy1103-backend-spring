package com.level.up.proyecto.spring.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "items_carrito")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carrito_id")
    private Carrito carrito;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id")
    @NotNull(message = "El producto es requerido")
    private Producto producto;

    @NotNull(message = "La cantidad es requerida")
    @Min(value = 1, message = "La cantidad mínima es 1")
    @Builder.Default
    private Integer cantidad = 1;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    // Calcular subtotal
    public Double calcularSubtotal() {
        return precioUnitario * cantidad;
    }

    @PrePersist
    @PreUpdate
    public void actualizarPrecio() {
        if (producto != null && precioUnitario == null) {
            this.precioUnitario = producto.getPrecio();
        }
    }
}
