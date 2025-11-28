package com.victor.order.ms_order_service.service;

import com.victor.order.ms_order_service.dto.FacturaDTO;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.EstadoPedido;
import com.victor.order.ms_order_service.model.Factura;
import com.victor.order.ms_order_service.model.Pedido;
import com.victor.order.ms_order_service.repository.FacturaRepository;
import com.victor.order.ms_order_service.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FacturaService {

    private final PedidoRepository pedidoRepository;
    private final FacturaRepository facturaRepository;

    public FacturaDTO emitirFactura(Long idPedido) {

        //Busca el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));

        //Validar estado
        if (EstadoPedido.FACTURADO.name().equals(pedido.getEstadoPedido())) {
            throw new RuntimeException("El pedido ya está facturado");
        }

        // 3) Calcular subtotal e IVA desde los detalles
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;

        for (DetallePedido det : pedido.getDetalles()) {
            if (det.getSubtotal() != null) {
                subtotal = subtotal.add(det.getSubtotal());
            }
            if (det.getIva() != null) {
                iva = iva.add(det.getIva());
            }
        }

        BigDecimal total = pedido.getTotalPedido();

        // Crear la entidad Factura
        Factura factura = new Factura();
        factura.setNumeroFactura("F-" + idPedido + "-" + System.currentTimeMillis());
        factura.setPedido(pedido);
        factura.setFechaEmision(LocalDateTime.now());
        factura.setSubtotal(subtotal);
        factura.setIva(iva);
        factura.setTotal(total);
        factura.setEstado("EMITIDA");

        // Cambiar estado del pedido
        pedido.setEstadoPedido(EstadoPedido.FACTURADO.name());
        pedidoRepository.save(pedido);

        // Guardar factura
        Factura guardada = facturaRepository.save(factura);

        // Devolver record directamente
        return new FacturaDTO(
                guardada.getId(),
                guardada.getNumeroFactura(),
                pedido.getIdPedido(),
                guardada.getFechaEmision(),
                guardada.getSubtotal(),
                guardada.getIva(),
                guardada.getTotal(),
                guardada.getEstado()
        );
    }
}

