package com.libreriaITQ.demo.service;

import com.libreriaITQ.demo.dto.request.ObraRequest;
import com.libreriaITQ.demo.dto.response.InventarioObraResponse;
import com.libreriaITQ.demo.dto.response.ObraDetalleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObraService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void crearObra(ObraRequest request, String email) {
        String sql = "exec sp_obra @operacion = 'I', @id_autor = ?, @id_carrera = ?, " +
                "@titulo = ?, @id_genero = ?, @id_estado = ?, " +
                "@id_clasificacion = ?, @email_usuario = ?";

        try {
            jdbcTemplate.update(sql,
                    request.getId_autor(),
                    request.getId_carrera(),
                    request.getTitulo(),
                    request.getId_genero(),
                    request.getId_estado() != null ? request.getId_estado() : "B",
                    request.getId_clasificacion() != null ? request.getId_clasificacion() : "C",
                    email
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public void actualizarObra(ObraRequest request, Integer idObra, String email) {
        String sql = "exec sp_obra @operacion = 'A', @id_obra = ?, @id_estado = ?, " +
                "@id_clasificacion = ?, @email_usuario = ?";

        try {
            jdbcTemplate.update(sql,
                    idObra,
                    request.getId_estado(),
                    request.getId_clasificacion(),
                    email
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public List<InventarioObraResponse> obtenerInventarioObras() {
        String sql = "exec sp_obra @operacion = 'S'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new InventarioObraResponse(
                rs.getInt("id_obra"),
                rs.getInt("id_lote"),
                rs.getString("titulo"),
                rs.getString("edicion"),
                rs.getString("plataforma"),
                rs.getString("factor_ganancia"),
                rs.getBigDecimal("costo_unitario"),
                rs.getBigDecimal("precio_unitario"),
                rs.getInt("stock")
        ));
    }

    public ObraDetalleResponse obtenerObraPorId(Integer idObra) {
        String sql = "exec sp_obra @operacion = 'Q', @id_obra = ?";

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> new ObraDetalleResponse(
                    rs.getInt("id_obra"),
                    rs.getString("titulo"),
                    rs.getString("id_estado"),
                    rs.getString("estado"),
                    rs.getString("id_clasificacion"),
                    rs.getString("clasificacion")
            ), idObra);
        } catch (Exception e) {
            throw new RuntimeException("Obra no encontrada: " + idObra);
        }
    }
}