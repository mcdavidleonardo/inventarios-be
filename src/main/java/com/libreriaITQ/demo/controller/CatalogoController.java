package com.libreriaITQ.demo.controller;

import com.libreriaITQ.demo.service.CatalogoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogos")
public class CatalogoController {

    @Autowired
    private CatalogoService catalogoService;

    @GetMapping("/{tabla}")
    public ResponseEntity<?> getCatalogo(@PathVariable String tabla) {
        return ResponseEntity.ok(catalogoService.obtenerCatalogo(tabla));
    }
}