package com.libreriaITQ.demo.controller;

import com.libreriaITQ.demo.dto.request.ObraRequest;
import com.libreriaITQ.demo.dto.response.InventarioObraResponse;
import com.libreriaITQ.demo.service.ObraService;
import com.libreriaITQ.demo.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/obras")
public class ObraController {

    @Autowired
    private ObraService obraService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearObra(@RequestBody ObraRequest request, @RequestHeader("Authorization") String token) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            obraService.crearObra(request, email);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Obra registrada correctamente");
            response.put("code", 0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("code", 401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarObra(
            @PathVariable("id") Integer idObra,
            @RequestBody ObraRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            obraService.actualizarObra(request, idObra, email);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Obra actualizada correctamente");
            response.put("code", 0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("code", 401);

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @GetMapping("/inventario")
    public ResponseEntity<?> consultarInventario() {
        try {
            List<InventarioObraResponse> inventario = obraService.obtenerInventarioObras();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("data", inventario);
            response.put("total_registros", inventario.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(obraService.obtenerObraPorId(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}