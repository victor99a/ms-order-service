package com.victor.order.ms_order_service.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "factura")
@Data
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_factura")
    private Long id;

    @Column(name = "numero_factura", nullable = false, unique = true)
    private String numeroFactura;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido", nullable = false)
    private Pedido pedido;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDateTime fechaEmision;

    @Column(name = "subtotal", nullable = false)
    private BigDecimal subtotal;

    @Column(name = "iva", nullable = false)
    private BigDecimal iva;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "estado", nullable = false)
    private String estado; // EJ: "EMITIDA"
}
