package com.level.up.proyecto.spring.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroPedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> items = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    private Double subtotal;
    private Double descuento;
    private Double total;

    @Column(name = "direccion_envio", length = 500)
    private String direccionEnvio;

    @Column(length = 500)
    private String notas;

    @Builder.Default
    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido = LocalDateTime.now();

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    public enum EstadoPedido {
        PENDIENTE,
        CONFIRMADO,
        EN_PREPARACION,
        ENVIADO,
        ENTREGADO,
        CANCELADO
    }

    // Métodos de utilidad
    public void agregarItem(ItemPedido item) {
        items.add(item);
        item.setPedido(this);
    }

    public void calcularTotales() {
        this.subtotal = items.stream()
                .mapToDouble(item -> item.getPrecioUnitario() * item.getCantidad())
                .sum();
        if (this.descuento == null) {
            this.descuento = 0.0;
        }
        this.total = this.subtotal - this.descuento;
    }
}
