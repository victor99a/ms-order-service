package com.victor.order.ms_order_service.dto;


import java.time.LocalDateTime;

public record EnvioDTO(
        Long id,
        Long idFactura,
        Long idPedido,
        LocalDateTime fechaEnvio,
        String estado,
        String comentario
) {}
