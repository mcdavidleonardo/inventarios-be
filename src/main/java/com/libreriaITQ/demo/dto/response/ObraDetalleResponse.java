package com.libreriaITQ.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ObraDetalleResponse {
    private Integer id_obra;
    private String titulo;
    private String id_estado;
    private String estado;
    private String id_clasificacion;
    private String clasificacion;
}