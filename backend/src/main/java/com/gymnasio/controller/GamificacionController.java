package com.gymnasio.controller;

import com.gymnasio.dto.LogroResponse;
import com.gymnasio.dto.PuntosSummaryResponse;
import com.gymnasio.service.GamificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/gamificacion")
@Tag(name = "Gamificación Controller", description = "Endpoints para puntos y logros del cliente")
public class GamificacionController {

    private final GamificacionService service;

    public GamificacionController(GamificacionService service) {
        this.service = service;
    }

    @Operation(summary = "Listar mis logros obtenidos (CLIENTE)")
    @GetMapping("/mis-logros")
    @PreAuthorize("hasRole('CLIENTE')") // Solo los clientes pueden ver sus logros
    public ResponseEntity<List<LogroResponse>> obtenerMisLogros() {
        return ResponseEntity.ok(service.listarMisLogros());
    }

    @Operation(summary = "Obtener mi total de puntos (CLIENTE)")
    @GetMapping("/mis-puntos")
    @PreAuthorize("hasRole('CLIENTE')") // Solo los clientes pueden ver sus puntos
    public ResponseEntity<PuntosSummaryResponse> obtenerMisPuntos() {
        return ResponseEntity.ok(service.obtenerMisPuntos());
    }
}