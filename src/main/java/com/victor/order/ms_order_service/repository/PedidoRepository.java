package com.victor.order.ms_order_service.repository;

import com.victor.order.ms_order_service.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

//por relacion entre tablas solo es necesario crear pedido
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}

