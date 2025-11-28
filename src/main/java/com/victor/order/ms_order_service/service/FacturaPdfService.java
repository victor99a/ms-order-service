package com.victor.order.ms_order_service.service;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.victor.order.ms_order_service.model.DetallePedido;
import com.victor.order.ms_order_service.model.Factura;
import com.victor.order.ms_order_service.model.Pedido;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class FacturaPdfService {

    public byte[] generarPdf(Factura factura) {

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);

            document.open();

            // ----- TÍTULO -----
            Font tituloFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            document.add(new Paragraph("Factura N° " + factura.getNumeroFactura(), tituloFont));
            document.add(new Paragraph("Fecha emisión: " + factura.getFechaEmision()));
            document.add(Chunk.NEWLINE);

            // ----- DATOS DEL PEDIDO -----
            Pedido pedido = factura.getPedido();
            if (pedido != null) {
                document.add(new Paragraph("Pedido ID: " + pedido.getIdPedido()));
                document.add(new Paragraph("ID Usuario: " + pedido.getIdUsuario()));
                document.add(new Paragraph("Estado pedido: " + pedido.getEstadoPedido()));
                document.add(Chunk.NEWLINE);
            }

            // ----- TABLA DETALLE -----
            PdfPTable tabla = new PdfPTable(4); // columnas: producto, cant, precio, total
            tabla.setWidthPercentage(100);
            tabla.addCell("Producto");
            tabla.addCell("Cantidad");
            tabla.addCell("Precio unitario");
            tabla.addCell("Total detalle");

            if (pedido != null && pedido.getDetalles() != null) {
                for (DetallePedido det : pedido.getDetalles()) {
                    tabla.addCell(det.getNombreProducto());
                    tabla.addCell(String.valueOf(det.getCantidad()));
                    tabla.addCell(det.getPrecioUnitario().toString());

                    BigDecimal totalDetalle = det.getTotalDetalle();
                    tabla.addCell(totalDetalle != null ? totalDetalle.toString() : "0");
                }
            }

            document.add(tabla);
            document.add(Chunk.NEWLINE);

            // ----- TOTALES -----
            document.add(new Paragraph("Subtotal: " + factura.getSubtotal()));
            document.add(new Paragraph("IVA: " + factura.getIva()));
            document.add(new Paragraph("Total: " + factura.getTotal()));

            document.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando PDF de factura", e);
        }
    }
}
