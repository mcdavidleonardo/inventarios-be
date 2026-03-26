package com.libreriaITQ.demo.controller;

import com.libreriaITQ.demo.security.JwtService;
import com.libreriaITQ.demo.security.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        try {
            String email = request.get("email");
            String clave = request.get("clave");

            if (email == null || clave == null) {
                throw new RuntimeException("Email y clave son obligatorios");
            }

            String token = jwtService.loginAndGenerateToken(email, clave);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("access_token", token);
            response.put("message", "Login exitoso");
            response.put("code", 0);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("code", HttpStatus.UNAUTHORIZED.value());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Ocurrió un error inesperado en el servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearUsuario(@RequestBody Map<String, String> request) {
        try {
            String idRol = request.get("id_rol");
            String nombre = request.get("nombre");
            String email = request.get("email");
            String clave = request.get("clave");

            // Registro de usuario
            usuarioService.crearUsuario(idRol, nombre, email, clave);

            // Respuesta Exitosa
            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Usuario registrado correctamente");
            response.put("code", 0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Respuesta Fallida
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("code", 401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }
}