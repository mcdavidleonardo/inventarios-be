package com.libreriaITQ.demo.service;

import com.libreriaITQ.demo.dto.response.CatalogoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CatalogoService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<CatalogoResponse> obtenerCatalogo(String tabla) {
        String sql = "exec sp_catalogo @operacion = 'Q', @tabla = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                        new CatalogoResponse(
                                rs.getString(1),
                                rs.getString(2)
                        ),
                tabla
        );
    }
}