package com.libreriaITQ.demo.service;

import com.libreriaITQ.demo.dto.request.LoteRequest;
import com.libreriaITQ.demo.dto.response.LoteDetalleResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoteService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void crearLote(LoteRequest request, String email) {
        String sql = "EXEC sp_lote @operacion = 'I', @id_obra = ?, @edicion = ?, " +
                "@id_plataforma = ?, @costo_unitario = ?, @stock = ?, @email_usuario = ?";

        try {
            jdbcTemplate.update(sql,
                    request.getId_obra(),
                    request.getEdicion(),
                    request.getId_plataforma(),
                    request.getCosto_unitario(),
                    request.getStock(),
                    email
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public void actualizarLote(LoteRequest request, Integer idLote, String email) {
        String sql = "exec sp_lote @operacion = 'A', @id_lote = ?, @costo_unitario = ?, " +
                "@stock = ?, @email_usuario = ?";

        try {
            jdbcTemplate.update(sql,
                    idLote,
                    request.getCosto_unitario(),
                    request.getStock(),
                    email
            );
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public Integer registrarVenta(Integer idLote, Integer cantidad, String email) {
        String sql = "exec sp_lote @operacion = 'B', @id_lote = ?, @cantidad = ?, @email_usuario = ?";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, idLote, cantidad, email);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }

    public List<LoteDetalleResponse> listarLibrosDisponibles() {
        String sql = "exec sp_lote @operacion = 'S'";

        return jdbcTemplate.query(sql, (rs, rowNum) -> new LoteDetalleResponse(
                rs.getInt("id_lote"),
                rs.getString("titulo"),
                rs.getString("autor"),
                rs.getString("carrera"),
                rs.getString("estado"),
                rs.getString("genero"),
                rs.getString("clasificacion"),
                rs.getString("edicion"),
                rs.getString("plataforma"),
                rs.getBigDecimal("precio"),
                rs.getInt("stock")
        ));
    }
}