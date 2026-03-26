package com.libreriaITQ.demo.dto.request;

import lombok.Data;

@Data
public class ObraRequest {
    private Integer id_autor;
    private Integer id_carrera;
    private String titulo;
    private String id_genero;
    private String id_estado;
    private String id_clasificacion;
}