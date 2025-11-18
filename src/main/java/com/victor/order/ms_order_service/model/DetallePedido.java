package com.victor.order.ms_order_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "detalle_pedido")
@Data
public class DetallePedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    //guardamos datos necesarios de ms-catalog
    @Column(name = "id_producto",nullable = false)
    private Long idProducto;

    @Column(name = "nombre_producto",nullable = false)
    private String nombreProducto;

    @Column(name = "precio_unitario",nullable = false)
    private Double precioUnitario;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    //sin iva
    @Column(name = "subtotal", nullable = false)
    private Double subtotal;

    @Column(name = "iva", nullable = false)
    private Double iva;

    //subtotal + iva
    @Column(name = "total_detalle", nullable = false)
    private Double totalDetalle;
}
