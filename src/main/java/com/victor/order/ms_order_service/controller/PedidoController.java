package com.victor.order.ms_order_service.controller;


import com.victor.order.ms_order_service.dto.CrearPedidoDTO;
import com.victor.order.ms_order_service.dto.PedidoDTO;
import com.victor.order.ms_order_service.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {
    private final PedidoService pedidoService;

    @GetMapping
    public List<PedidoDTO> listar(){
        return pedidoService.listar();
    }

    @GetMapping("/{id}")
    public PedidoDTO buscar(@PathVariable Long id){
        return pedidoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody CrearPedidoDTO dto) {
        PedidoDTO pedido = pedidoService.crear(dto);
        URI uri = URI.create("/api/pedidos/" + pedido.idPedido());
        return ResponseEntity.created(uri).body(pedido);
    }


    @PutMapping("/{id}")
    public PedidoDTO actualizar(@PathVariable Long id, @RequestBody PedidoDTO pedidoDTO){
        return pedidoService.actualizar(id,pedidoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    //Trabajador pastelero
    @GetMapping("/activos")
    public List<PedidoDTO> listarActivos() {
        return pedidoService.listarActivos();
    }

    @PutMapping("/{id}/enviar")
    public PedidoDTO marcarEnviado(@PathVariable Long id) {
        return pedidoService.marcarEnviado(id);
    }
}
