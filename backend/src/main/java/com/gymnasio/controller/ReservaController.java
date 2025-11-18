package com.gymnasio.controller;

import com.gymnasio.dto.ReservaRequest;
import com.gymnasio.dto.ReservaResponse;
import com.gymnasio.service.ReservaService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

  private final ReservaService service;

  public ReservaController(ReservaService service) {
    this.service = service;
  }

  @Operation(summary = "Reservar cupo (CLIENTE)")
  @PostMapping
  @PreAuthorize("hasRole('CLIENTE')")
  public ReservaResponse reservar(@Valid @RequestBody ReservaRequest req) {
    return service.reservarComoCliente(req);
  }

  @Operation(summary = "Cancelar mi reserva (CLIENTE) o cancelar de otro (ADMIN/TRAINER)")
  @PutMapping("/{id}/cancelar")
  @PreAuthorize("hasAnyRole('CLIENTE','ADMIN','TRAINER')")
  public void cancelar(@PathVariable Integer id) {
    service.cancelarMiReserva(id);
  }

  @Operation(summary = "Listar mis reservas (CLIENTE)")
  @GetMapping("/mis")
  @PreAuthorize("hasRole('CLIENTE')")
  public List<ReservaResponse> misReservas() {
    return service.misReservas();
  }

  @Operation(summary = "Asistentes de una clase (ADMIN/TRAINER)")
  @GetMapping("/clase/{claseId}")
  @PreAuthorize("hasAnyRole('ADMIN','TRAINER')")
  public List<ReservaResponse> asistentes(@PathVariable Integer claseId) {
    return service.asistentesDeClase(claseId);
  }
}