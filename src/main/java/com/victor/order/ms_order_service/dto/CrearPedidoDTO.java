package com.victor.order.ms_order_service.dto;

import java.util.List;

public record CrearPedidoDTO(
        Long idUsuario,
        List<DetallePedidoDTO> detalles
) {}