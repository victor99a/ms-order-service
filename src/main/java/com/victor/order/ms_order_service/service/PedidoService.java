package com.victor.order.ms_order_service.service;


import com.victor.order.ms_order_service.dto.CrearPedidoDTO;
import com.victor.order.ms_order_service.dto.PedidoDTO;
import com.victor.order.ms_order_service.mapper.PedidoMapper;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.EstadoPedido;
import com.victor.order.ms_order_service.model.Pedido;
import com.victor.order.ms_order_service.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private static final BigDecimal IVA = new BigDecimal("0.19");

    private final PedidoRepository pedidoRepository;
    private final PedidoMapper mapper;

    // Listar
    public List<PedidoDTO> listar() {
        return pedidoRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    // Buscar por id
    public PedidoDTO buscarPorId(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado"));
        return mapper.toDTO(pedidoRepository.findById(id).get());
    }

    // Crear
    public PedidoDTO crear(CrearPedidoDTO dto) {
        // 1) DTO -> entidad
        Pedido pedido = mapper.toEntity(dto);

        // 2) completar datos
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setIdPedido(null); // si viene algo en el JSON
        pedido.setFechaPedido(LocalDateTime.now());
        pedido.setEstadoPedido(String.valueOf(EstadoPedido.CREADO));

        // 3) calcular IVA y totales en cada detalle
        BigDecimal totalPedido = BigDecimal.ZERO;

        if (pedido.getDetalles() != null) {
            for (DetallePedido det : pedido.getDetalles()) {

                BigDecimal precio = BigDecimal.valueOf(det.getPrecioUnitario());
                BigDecimal cantidad = BigDecimal.valueOf(det.getCantidad());

                BigDecimal subtotal = precio.multiply(cantidad);
                BigDecimal iva = subtotal.multiply(IVA);
                BigDecimal totalDetalle = subtotal.add(iva);

                det.setSubtotal(subtotal);
                det.setIva(iva);
                det.setTotalDetalle(totalDetalle);

                // para la relación bidireccional
                det.setPedido(pedido);

                totalPedido = totalPedido.add(totalDetalle);
            }
        }

        pedido.setTotalPedido(totalPedido);

        // 4) guardar y devolver DTO
        Pedido guardado = pedidoRepository.save(pedido);
        return mapper.toDTO(guardado);
    }

    // ACTUALIZAR
    public PedidoDTO actualizar(Long id, PedidoDTO dto) {
        Pedido existente = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + id));

        // limpiamos detalles anteriores y reconstruimos según el DTO
        existente.getDetalles().clear();
        Pedido pedidoNuevo = mapper.toEntity(dto);

        existente.setIdUsuario(pedidoNuevo.getIdUsuario());

        BigDecimal totalPedido = BigDecimal.ZERO;

        if (pedidoNuevo.getDetalles() != null) {
            for (DetallePedido det : pedidoNuevo.getDetalles()) {

                BigDecimal precio = BigDecimal.valueOf(det.getPrecioUnitario());
                BigDecimal cantidad = BigDecimal.valueOf(det.getCantidad());

                BigDecimal subtotal = precio.multiply(cantidad);
                BigDecimal iva = subtotal.multiply(IVA);
                BigDecimal totalDetalle = subtotal.add(iva);

                det.setSubtotal(subtotal);
                det.setIva(iva);
                det.setTotalDetalle(totalDetalle);
                det.setPedido(existente);

                existente.getDetalles().add(det);
                totalPedido = totalPedido.add(totalDetalle);
            }
        }

        existente.setTotalPedido(totalPedido);

        Pedido guardado = pedidoRepository.save(existente);
        return mapper.toDTO(guardado);
    }

    //ELIMINAR
    public void eliminar(Long id) {
        if (!pedidoRepository.existsById(id)) {
            throw new EntityNotFoundException("Pedido no encontrado: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    // Trabajador pastelero
    // 1) LISTAR PEDIDOS “ACTIVOS” PARA EL TRABAJADOR
    public List<PedidoDTO> listarActivos() {
        List<String> estadosActivos = Arrays.asList(
                EstadoPedido.CREADO.name(),
                EstadoPedido.FACTURADO.name()
        );

        return pedidoRepository.findByEstadoPedidoIn(estadosActivos)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    public PedidoDTO marcarEnviado(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido no encontrado: " + id));

        // ahora la regla es: FACTURADO → ENVIADO
        if (!pedido.getEstadoPedido().equals(EstadoPedido.FACTURADO.name())) {
            throw new RuntimeException("Sólo se pueden marcar como ENVIADO los pedidos FACTURADO");
        }

        pedido.setEstadoPedido(EstadoPedido.ENVIADO.name());
        Pedido guardado = pedidoRepository.save(pedido);
        return mapper.toDTO(guardado);
    }
}

