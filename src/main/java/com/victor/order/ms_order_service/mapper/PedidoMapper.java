package com.victor.order.ms_order_service.mapper;

import com.victor.order.ms_order_service.dto.CrearPedidoDTO;
import com.victor.order.ms_order_service.dto.DetallePedidoDTO;
import com.victor.order.ms_order_service.dto.PedidoDTO;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.Pedido;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    // ---------- Entity -> DTO ----------
    PedidoDTO toDTO(Pedido entity);
    DetallePedidoDTO toDTO(DetallePedido entity);
    List<PedidoDTO> toDTO(List<Pedido> entities);

    // ---------- DTO completo -> Entity (para PUT, etc.) ----------
    // Ojo: normalmente tampoco deberías permitir cambiar id/fecha/total por DTO,
    // por eso los ignoramos aquí también.
    @Mapping(target = "idPedido", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "estadoPedido", ignore = true)
    @Mapping(target = "totalPedido", ignore = true)
    Pedido toEntity(PedidoDTO dto);

    DetallePedido toEntity(DetallePedidoDTO dto);

    // ---------- DTO de creación -> Entity (para POST) ----------
    @Mapping(target = "idPedido", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "estadoPedido", ignore = true)
    @Mapping(target = "totalPedido", ignore = true)
    Pedido toEntity(CrearPedidoDTO dto);

    // ---------- Updates parciales ----------
    @Mapping(target = "idPedido", ignore = true)
    @Mapping(target = "fechaPedido", ignore = true)
    @Mapping(target = "estadoPedido", ignore = true)
    @Mapping(target = "totalPedido", ignore = true)
    void updatePedidoFromDTO(PedidoDTO dto, @MappingTarget Pedido entity);

    // Ajusta estos ignores según tu entidad DetallePedido
    @Mapping(target = "idDetalle", ignore = true)   // si tienes idDetalle
    @Mapping(target = "pedido", ignore = true)      // para no pisar la relación
    void updateDetalleFromDTO(DetallePedidoDTO dto, @MappingTarget DetallePedido entity);
}
