package com.gymnasio.controller;

import com.gymnasio.dto.PagoResponse;
import com.gymnasio.service.PagoService;
import com.gymnasio.dto.PagoRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@Tag(name = "Pago Controller", description = "Endpoints para pagos")
public class PagoController {

    private final PagoService service;

    public PagoController(PagoService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener mi historial de pagos (CLIENTE)")
    @GetMapping("/mi-historial")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<PagoResponse>> obtenerMiHistorialPagos() {
        return ResponseEntity.ok(service.listarPagosUsuarioAutenticado());
    }

    // --- ENDPOINT NUEVO PARA ADMIN ---
    @Operation(summary = "Registrar un pago manualmente (ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagoResponse> registrarPago(@Valid @RequestBody PagoRequest request) {
        PagoResponse response = service.registrarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener historial de pagos de un usuario (ADMIN)")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PagoResponse>> obtenerPagosPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuarioId(usuarioId));
    }
}