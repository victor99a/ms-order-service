package com.victor.order.ms_order_service.service;

import com.victor.order.ms_order_service.dto.CrearPedidoDTO;
import com.victor.order.ms_order_service.dto.DetallePedidoDTO;
import com.victor.order.ms_order_service.dto.PedidoDTO;
import com.victor.order.ms_order_service.mapper.PedidoMapper;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.EstadoPedido;
import com.victor.order.ms_order_service.model.Pedido;
import com.victor.order.ms_order_service.repository.PedidoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitario simple de PedidoService.crear:
 * - usa el mapper y el repository simulados
 * - verifica que se calculen montos y que el estado sea CREADO
 */
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    PedidoRepository pedidoRepository;

    @Mock
    PedidoMapper pedidoMapper;

    @InjectMocks
    PedidoService pedidoService;

    @Test
    void crear_deberiaCalcularMontosYDejarEstadoCreado() {
        // GIVEN: entidad Pedido que devolverá el mapper.toEntity(dto)
        Pedido pedidoEntidad = new Pedido();
        pedidoEntidad.setIdUsuario(1L);

        DetallePedido detalle = new DetallePedido();
        detalle.setCantidad(2);              // 2 unidades
        detalle.setPrecioUnitario(10_000.0); // de 10.000 cada una
        pedidoEntidad.setDetalles(Collections.singletonList(detalle));

        when(pedidoMapper.toEntity(any(CrearPedidoDTO.class)))
                .thenReturn(pedidoEntidad);

        // el repo devuelve el mismo pedido (simulando que se guardó)
        when(pedidoRepository.save(any(Pedido.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // el mapper a DTO puede devolver algo sencillo, solo para no ser null
        PedidoDTO dtoRespuesta = new PedidoDTO(
                1L,
                1L,
                LocalDateTime.now(),
                0.0,
                EstadoPedido.CREADO.name(),
                List.<DetallePedidoDTO>of()
        );
        when(pedidoMapper.toDTO(any(Pedido.class)))
                .thenReturn(dtoRespuesta);

        CrearPedidoDTO crearDto = mock(CrearPedidoDTO.class);

        // WHEN
        PedidoDTO resultado = pedidoService.crear(crearDto);

        // THEN: verificamos qué se guardó
        verify(pedidoRepository, times(1)).save(any(Pedido.class));

        // usamos un captor simple para revisar el último argumento
        var captor = org.mockito.ArgumentCaptor.forClass(Pedido.class);
        verify(pedidoRepository).save(captor.capture());
        Pedido guardado = captor.getValue();

        // 1) estado correcto
        assertEquals(EstadoPedido.CREADO.name(), guardado.getEstadoPedido());

        // 2) fecha seteada
        assertNotNull(guardado.getFechaPedido());

        // 3) totalPedido calculado y > 0
        assertNotNull(guardado.getTotalPedido());
        assertTrue(guardado.getTotalPedido().compareTo(BigDecimal.ZERO) > 0);

        // 4) detalle con montos > 0
        assertNotNull(guardado.getDetalles());
        assertFalse(guardado.getDetalles().isEmpty());
        DetallePedido detGuardado = guardado.getDetalles().get(0);

        assertNotNull(detGuardado.getSubtotal());
        assertTrue(detGuardado.getSubtotal().compareTo(BigDecimal.ZERO) > 0);

        assertNotNull(detGuardado.getIva());
        assertTrue(detGuardado.getIva().compareTo(BigDecimal.ZERO) > 0);

        assertNotNull(detGuardado.getTotalDetalle());
        assertTrue(detGuardado.getTotalDetalle().compareTo(BigDecimal.ZERO) > 0);

        // 5) el método devuelve un DTO no nulo
        assertNotNull(resultado);
    }
}
