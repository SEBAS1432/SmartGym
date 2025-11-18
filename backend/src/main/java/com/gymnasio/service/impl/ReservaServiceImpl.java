package com.gymnasio.service.impl;

import com.gymnasio.domain.model.*;
import com.gymnasio.dto.ReservaRequest;
import com.gymnasio.dto.ReservaResponse;
import com.gymnasio.repository.ClaseRepository;
import com.gymnasio.repository.ReservaRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.ReservaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional
public class ReservaServiceImpl implements ReservaService {

  private final ReservaRepository reservaRepo;
  private final ClaseRepository claseRepo;
  private final UsuarioRepository usuarioRepo;

  public ReservaServiceImpl(ReservaRepository reservaRepo, ClaseRepository claseRepo, UsuarioRepository usuarioRepo) {
    this.reservaRepo = reservaRepo;
    this.claseRepo = claseRepo;
    this.usuarioRepo = usuarioRepo;
  }

  // ===== Helpers =====
  private Usuario currentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String correo = auth.getName();
    return usuarioRepo.findByCorreo(correo)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no autenticado"));
  }

  private ReservaResponse toResponse(Reserva r) {
    String nombreUsuario = r.getUsuario().getNombres() + " " + r.getUsuario().getApellidos();
    
    // Obtener nombre del instructor
    String instructorNombre = "N/A";
    if (r.getClase() != null && r.getClase().getInstructor() != null) {
      instructorNombre = r.getClase().getInstructor().getNombres() + 
                        " " + r.getClase().getInstructor().getApellidos();
    }
    
    return new ReservaResponse(
        r.getId(),
        r.getClase().getId(),
        r.getClase().getTitulo(),
        r.getUsuario().getId(),
        nombreUsuario,
        r.getEstado(),
        r.getFechaReserva(),
        r.getFechaCreacion(),
        r.getFechaActualizacion(),
        instructorNombre,
        r.getClase().getFechaInicio().toLocalDateTime()
    );
  }

  // API

  @Override
  public ReservaResponse reservarComoCliente(ReservaRequest req) {
    Usuario user = currentUser();
    if (user.getRol() != Rol.CLIENTE) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Solo CLIENTE puede reservar");
    }

    Clase clase = claseRepo.findById(req.claseId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no existe"));

    if (clase.getEstado() != EstadoClase.PROGRAMADA) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "La clase no está programada");
    }

    long reservadas = reservaRepo.countByClaseIdAndEstado(clase.getId(), EstadoReserva.RESERVADA);
    if (reservadas >= clase.getCapacidad()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "La clase está llena");
    }

    boolean yaReservada = reservaRepo.existsByUsuario_IdAndClase_IdAndEstado(
        user.getId(), clase.getId(), EstadoReserva.RESERVADA);
    if (yaReservada) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya tienes una reserva activa en esta clase");
    }

    Reserva r = Reserva.builder()
        .usuario(user)
        .clase(clase)
        .estado(EstadoReserva.RESERVADA)
        .fechaReserva(OffsetDateTime.now())
        .build();

    r = reservaRepo.save(r);
    
    // Recargar con todas las relaciones para poder construir el response completo
    final Integer reservaId = r.getId();
    Reserva reservaCompleta = reservaRepo.findReservasDetalladasPorUsuario(user.getId())
        .stream()
        .filter(res -> res.getId().equals(reservaId))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(
            HttpStatus.INTERNAL_SERVER_ERROR, 
            "No se pudo cargar la reserva"
        ));
    
    return toResponse(reservaCompleta);
  }

  @Override
  public void cancelarMiReserva(Integer reservaId) {
    Usuario user = currentUser();
    Reserva r = reservaRepo.findById(reservaId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva no encontrada"));

    // Un cliente solo puede cancelar su propia reserva
    if (!r.getUsuario().getId().equals(user.getId()) && user.getRol() == Rol.CLIENTE) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puedes cancelar reservas de otros usuarios");
    }

    r.setEstado(EstadoReserva.CANCELADA);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservaResponse> misReservas() {
    Usuario user = currentUser();
    // Usar la query con JOIN FETCH para cargar todas las relaciones
    List<Reserva> reservas = reservaRepo.findReservasDetalladasPorUsuario(user.getId());
    return reservas.stream().map(this::toResponse).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReservaResponse> asistentesDeClase(Integer claseId) {
    Usuario user = currentUser();
    if (user.getRol() == Rol.CLIENTE) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado");
    }
    // Usar la query con JOIN FETCH
    List<Reserva> reservas = reservaRepo.findReservasDetalladasPorClaseId(
        claseId, 
        EstadoReserva.RESERVADA
    );
    return reservas.stream().map(this::toResponse).toList();
  }
}