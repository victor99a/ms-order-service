package com.victor.order.ms_order_service.dto;

import java.time.LocalDateTime;
import java.util.List;

public record PedidoDTO(
        Long idPedido,
        Long idUsuario,
        LocalDateTime fechaPedido,
        double totalPedido,
        List<DetallePedidoDTO> detalles
) {
}
