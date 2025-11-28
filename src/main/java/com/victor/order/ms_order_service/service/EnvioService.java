package com.victor.order.ms_order_service.service;


import com.victor.order.ms_order_service.dto.EnvioDTO;
import com.victor.order.ms_order_service.model.Envio;
import com.victor.order.ms_order_service.model.Factura;
import com.victor.order.ms_order_service.repository.EnvioRepository;
import com.victor.order.ms_order_service.repository.FacturaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnvioService {

    private final EnvioRepository envioRepository;
    private final FacturaRepository facturaRepository;

    // RF5: marcar productos como despachados (crear registro de envío)
    public EnvioDTO despachar(Long idFactura, String comentario) {

        Factura factura = facturaRepository.findById(idFactura)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada"));

        Envio envio = new Envio();
        envio.setFactura(factura);
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setEstado("DESPACHADO");
        envio.setComentario(comentario);

        Envio guardado = envioRepository.save(envio);

        return new EnvioDTO(
                guardado.getId(),
                factura.getId(),
                factura.getPedido().getIdPedido(),
                guardado.getFechaEnvio(),
                guardado.getEstado(),
                guardado.getComentario()
        );
    }

    // Listar todos los envíos
    public List<EnvioDTO> listar(String estado) {

        List<Envio> envios;

        if (estado != null && !estado.isBlank()) {
            envios = envioRepository.findByEstado(estado);
        } else {
            envios = envioRepository.findAll();
        }

        return envios.stream()
                .map(e -> new EnvioDTO(
                        e.getId(),
                        e.getFactura().getId(),
                        e.getFactura().getPedido().getIdPedido(),
                        e.getFechaEnvio(),
                        e.getEstado(),
                        e.getComentario()
                ))
                .toList();
    }
}
