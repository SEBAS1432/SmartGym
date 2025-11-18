package com.gymnasio.controller;

import com.gymnasio.dto.TrainerDashboardStats;
import com.gymnasio.service.TrainerDashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trainer-dashboard")
@Tag(name = "Trainer Dashboard Controller", description = "Endpoints para estadísticas del Trainer")
public class TrainerDashboardController {

    private final TrainerDashboardService service;

    public TrainerDashboardController(TrainerDashboardService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener estadísticas para el Dashboard (TRAINER/ADMIN)")
    @GetMapping("/stats")
    @PreAuthorize("hasAnyRole('ADMIN', 'TRAINER')")
    public ResponseEntity<TrainerDashboardStats> getStats() {
        return ResponseEntity.ok(service.getDashboardStats());
    }
}