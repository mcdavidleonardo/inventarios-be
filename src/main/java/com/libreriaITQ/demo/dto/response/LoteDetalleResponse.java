package com.libreriaITQ.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LoteDetalleResponse {
    private Integer id_lote;
    private String titulo;
    private String autor;
    private String carrera;
    private String estado;
    private String genero;
    private String clasificacion;
    private String edicion;
    private String plataforma;
    private BigDecimal precio;
    private Integer stock;
}