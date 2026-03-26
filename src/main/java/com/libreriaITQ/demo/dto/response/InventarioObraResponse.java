package com.libreriaITQ.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class InventarioObraResponse {
    private Integer id_obra;
    private Integer id_lote;
    private String titulo;
    private String edicion;
    private String plataforma;
    private String factor_ganancia;
    private BigDecimal costo_unitario;
    private BigDecimal precio_unitario;
    private Integer stock;
}