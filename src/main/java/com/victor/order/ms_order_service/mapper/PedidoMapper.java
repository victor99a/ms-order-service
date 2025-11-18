package com.victor.order.ms_order_service.mapper;

import com.victor.order.ms_order_service.dto.DetallePedidoDTO;
import com.victor.order.ms_order_service.dto.PedidoDTO;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.Pedido;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    PedidoDTO toDTO(Pedido entity);
    DetallePedidoDTO toDTO(DetallePedido entity);
    List<PedidoDTO> toDTO(List<Pedido> entities);

    Pedido toEntity(PedidoDTO dto);
    DetallePedido toEntity(DetallePedidoDTO dto);

    void updatePedidoFromDTO(PedidoDTO dto, @MappingTarget Pedido entity);
    void updateDetalleFromDTO(DetallePedidoDTO dto, @MappingTarget DetallePedido entity);


}
