package com.victor.order.ms_order_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FacturaDTO(
    Long id,
    String numeroFactura,
    Long idPedido,
    LocalDateTime fechaEmision,
    BigDecimal subtotal,
    BigDecimal iva,
    BigDecimal total,
    String estado) {
}
