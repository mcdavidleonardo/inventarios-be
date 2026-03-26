package com.libreriaITQ.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void crearUsuario(String idRol, String nombre, String email, String clave) {
        String claveEncriptada = passwordEncoder.encode(clave);

        String sql = "exec sp_usuario @operacion = 'I', @id_rol = ?, @nombre = ?, @email = ?, @clave = ?";

        try {
            jdbcTemplate.update(sql, idRol, nombre, email, claveEncriptada);
        } catch (Exception e) {
            throw new RuntimeException(e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
        }
    }
}