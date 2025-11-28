package com.victor.order.ms_order_service.controller;


import com.victor.order.ms_order_service.dto.FacturaDTO;
import com.victor.order.ms_order_service.model.Factura;
import com.victor.order.ms_order_service.repository.FacturaRepository;
import com.victor.order.ms_order_service.service.FacturaPdfService;
import com.victor.order.ms_order_service.service.FacturaService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {

    private final FacturaService facturaService;
    private final FacturaRepository facturaRepository;
    private final FacturaPdfService facturaPdfService;

    @PostMapping("/emitir/{idPedido}")
    public ResponseEntity<FacturaDTO> emitir(@PathVariable Long idPedido) {
        FacturaDTO factura = facturaService.emitirFactura(idPedido);
        return ResponseEntity.ok(factura);
    }

    @GetMapping("/{idFactura}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long idFactura) {

        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));

        byte[] pdfBytes = facturaPdfService.generarPdf(factura);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.attachment()
                        .filename("factura-" + idFactura + ".pdf")
                        .build()
        );

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<FacturaDTO>> listar() {
        List<Factura> facturas = facturaRepository.findAll();
        List<FacturaDTO> dtos = facturas.stream()
                .map(f -> new FacturaDTO(
                        f.getId(),
                        f.getNumeroFactura(),
                        f.getPedido().getIdPedido(),
                        f.getFechaEmision(),
                        f.getSubtotal(),
                        f.getIva(),
                        f.getTotal(),
                        f.getEstado()
                ))
                .toList();
        return ResponseEntity.ok(dtos);
    }

}

