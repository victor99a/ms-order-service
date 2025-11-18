package com.victor.order.ms_order_service.dto;

public record DetallePedidoDTO(
        Long idProducto,
        String nombreProducto,
        double precioUnitario,
        int cantidad,
        double subtotal,
        double iva,
        double totalDetalle
) {
}
