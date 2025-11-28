package com.victor.order.ms_order_service.controller;

import com.victor.order.ms_order_service.dto.EnvioDTO;
import com.victor.order.ms_order_service.service.EnvioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/envios")
@RequiredArgsConstructor
public class EnvioController {

    private final EnvioService envioService;

    // RF5: marcar como despachado
    @PostMapping("/{idFactura}/despachar")
    public ResponseEntity<EnvioDTO> despachar(
            @PathVariable Long idFactura,
            @RequestParam(required = false, defaultValue = "Pedido despachado al cliente")
            String comentario
    ) {
        EnvioDTO dto = envioService.despachar(idFactura, comentario);
        return ResponseEntity.ok(dto);
    }

    // Listar envíos (todos o por estado: DESPACHADO / PENDIENTE si más adelante lo usas)
    @GetMapping
    public ResponseEntity<List<EnvioDTO>> listar(
            @RequestParam(required = false) String estado
    ) {
        return ResponseEntity.ok(envioService.listar(estado));
    }
}
