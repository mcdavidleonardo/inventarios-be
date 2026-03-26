package com.libreriaITQ.demo.controller;

import com.libreriaITQ.demo.dto.request.LoteRequest;
import com.libreriaITQ.demo.dto.response.LoteDetalleResponse;
import com.libreriaITQ.demo.service.LoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lotes")
public class LoteController {

    @Autowired
    private LoteService loteService;

    @PostMapping("/crear")
    public ResponseEntity<?> crearLote(@RequestBody LoteRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            loteService.crearLote(request, email);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Lote creado exitosamente");
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
    public ResponseEntity<?> actualizarLote(
            @PathVariable("id") Integer idLote,
            @RequestBody LoteRequest request) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            loteService.actualizarLote(request, idLote, email);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Lote actualizado correctamente.");
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

    @PostMapping("/vender")
    public ResponseEntity<?> venderLibro(@RequestBody Map<String, Object> request) {
        try {
            Integer idLote = (Integer) request.get("id_lote");
            Integer cantidad = (Integer) request.get("cantidad");

            String email = SecurityContextHolder.getContext().getAuthentication().getName();

            Integer nuevoStock = loteService.registrarVenta(idLote, cantidad, email);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("message", "Venta procesada exitosamente");
            response.put("nuevo_stock", nuevoStock);
            response.put("code", 0);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            error.put("code", 401);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<?> obtenerDisponibles() {
        try {
            List<LoteDetalleResponse> libros = loteService.listarLibrosDisponibles();

            Map<String, Object> response = new HashMap<>();
            response.put("status", "ok");
            response.put("data", libros);
            response.put("total", libros.size());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}