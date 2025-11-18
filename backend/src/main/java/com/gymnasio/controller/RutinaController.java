package com.gymnasio.controller;

import com.gymnasio.dto.RutinaRequest; // Importa el nuevo DTO
import com.gymnasio.dto.RutinaResponse;
import com.gymnasio.service.RutinaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid; // Importa @Valid
import org.springframework.http.HttpStatus; // Importa HttpStatus
import org.springframework.http.ResponseEntity; // Importa ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rutinas")
@Tag(name = "Rutina Controller", description = "Endpoints para rutinas") // Etiqueta para Swagger
public class RutinaController {

    private final RutinaService service;

    public RutinaController(RutinaService service) {
        this.service = service;
    }


    // --- ENDPOINT PARA CLIENTE ---
    @Operation(summary = "Listar mis rutinas (CLIENTE)")
    @GetMapping("/mis-rutinas")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<List<RutinaResponse>> misRutinas() {
        return ResponseEntity.ok(service.listarPorUsuarioAutenticado());
    }
    
    // --- ENDPOINTS PARA TRAINER/ADMIN ---

    @Operation(summary = "Obtener una rutina por su ID (TRAINER/ADMIN)")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<RutinaResponse> obtenerRutinaPorId(@PathVariable("id") Integer rutinaId) {
        return ResponseEntity.ok(service.obtenerPorId(rutinaId));
    }

    
    @Operation(summary = "Listar rutinas de un usuario específico (TRAINER/ADMIN)")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<List<RutinaResponse>> obtenerRutinasPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuarioId(usuarioId));
    }

    @Operation(summary = "Crear una rutina para un usuario (TRAINER/ADMIN)")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<RutinaResponse> crearRutina(@Valid @RequestBody RutinaRequest request) {
        RutinaResponse response = service.crear(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar una rutina (TRAINER/ADMIN)")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<RutinaResponse> actualizarRutina(@PathVariable("id") Integer rutinaId, 
                                                            @Valid @RequestBody RutinaRequest request) {
        RutinaResponse response = service.actualizar(rutinaId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar una rutina (TRAINER/ADMIN)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<Void> eliminarRutina(@PathVariable("id") Integer rutinaId) {
        service.eliminar(rutinaId);
        return ResponseEntity.noContent().build(); // Devuelve 204 No Content
    }
}