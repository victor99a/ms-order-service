package com.victor.order.ms_order_service.repository;


import com.victor.order.ms_order_service.model.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByEstado(String estado);
}
