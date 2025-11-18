package com.gymnasio.controller;

import com.gymnasio.dto.MembresiaResponse;
import com.gymnasio.service.MembresiaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.gymnasio.dto.MembresiaRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/membresias")
@Tag(name = "Membresía Controller", description = "Endpoints para membresías")
public class MembresiaController {

    private final MembresiaService service;

    public MembresiaController(MembresiaService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener mi membresía activa (CLIENTE)")
    @GetMapping("/mi-membresia")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<MembresiaResponse> obtenerMiMembresia() {
        return ResponseEntity.ok(service.obtenerMembresiaActivaUsuarioAutenticado());
    }

    // --- ENDPOINT NUEVO PARA ADMIN ---
    @Operation(summary = "Asignar o crear una membresía (ADMIN)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MembresiaResponse> crearMembresia(@Valid @RequestBody MembresiaRequest request) {
        MembresiaResponse response = service.crearMembresia(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener historial de membresías de un usuario (ADMIN)")
    @GetMapping("/usuario/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<MembresiaResponse>> obtenerMembresiasPorUsuario(@PathVariable Integer usuarioId) {
        return ResponseEntity.ok(service.listarPorUsuarioId(usuarioId));
    }
}