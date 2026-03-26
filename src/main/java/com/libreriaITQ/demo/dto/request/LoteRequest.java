package com.libreriaITQ.demo.dto.request;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class LoteRequest {
    private Integer id_obra;
    private String edicion;
    private String id_plataforma;
    private BigDecimal costo_unitario;
    private Integer stock;
}