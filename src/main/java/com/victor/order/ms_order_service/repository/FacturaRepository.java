package com.victor.order.ms_order_service.repository;

import com.victor.order.ms_order_service.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
