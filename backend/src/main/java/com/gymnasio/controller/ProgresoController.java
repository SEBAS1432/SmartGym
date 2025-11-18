package com.gymnasio.controller;

import com.gymnasio.dto.ProgresoRequest;
import com.gymnasio.dto.ProgresoResponse;
import com.gymnasio.service.ProgresoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/progreso")
public class ProgresoController {

    private final ProgresoService service;

    public ProgresoController(ProgresoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar mi historial de progreso (CLIENTE)")
    @GetMapping("/mi-progreso")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<ProgresoResponse>> obtenerMiProgreso() {
        return ResponseEntity.ok(service.listarPorUsuarioAutenticado());
    }

    @Operation(summary = "Registrar nueva entrada de progreso (CLIENTE)")
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ProgresoResponse> crearProgreso(@Valid @RequestBody ProgresoRequest request) {
        ProgresoResponse response = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}