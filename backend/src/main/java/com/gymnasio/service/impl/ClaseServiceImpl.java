package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Clase;
import com.gymnasio.domain.model.EstadoClase;
import com.gymnasio.domain.model.EstadoReserva;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.ClaseRequest;
import com.gymnasio.dto.ClaseResponse;
import com.gymnasio.dto.DisponibilidadClaseView;
import com.gymnasio.repository.ClaseRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.ClaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class ClaseServiceImpl implements ClaseService {

    private final ClaseRepository claseRepo;
    private final UsuarioRepository usuarioRepo;

    public ClaseServiceImpl(ClaseRepository claseRepo, UsuarioRepository usuarioRepo) {
        this.claseRepo = claseRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public ClaseResponse crear(ClaseRequest r) {
        // r.instructorId() debe ser Integer (no Long)
        Usuario instructor = usuarioRepo.findById(r.instructorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor no existe"));

        Clase c = Clase.builder()
                .titulo(r.titulo())
                .descripcion(r.descripcion())
                .tipo(r.tipo())
                .instructor(instructor)
                .fechaInicio(r.fechaInicio())
                .duracionMinutos(r.duracionMinutos())
                .capacidad(r.capacidad())
                .estado(r.estado() != null ? r.estado() : EstadoClase.PROGRAMADA)
                .build();

        c = claseRepo.save(c);
        return toResponse(c);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaseResponse obtener(Integer id) {
        return claseRepo.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseResponse> listar() {
        return claseRepo.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ClaseResponse actualizar(Integer id, ClaseRequest r) {
        Clase c = claseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada"));

        // Cambiar instructor si viene otro id
        if (r.instructorId() != null && !r.instructorId().equals(c.getInstructor().getId())) {
            Usuario instructor = usuarioRepo.findById(r.instructorId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Instructor no existe"));
            c.setInstructor(instructor);
        }

        if (r.titulo() != null)
            c.setTitulo(r.titulo());
        c.setDescripcion(r.descripcion());
        c.setTipo(r.tipo());
        c.setFechaInicio(r.fechaInicio());
        c.setDuracionMinutos(r.duracionMinutos());
        if (r.capacidad() != null)
            c.setCapacidad(r.capacidad());
        if (r.estado() != null)
            c.setEstado(r.estado());

        // La entidad está administrada: se sincroniza en el commit
        return toResponse(c);
    }

    @Override
    public void eliminar(Integer id) {
        if (!claseRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Clase no encontrada");
        }
        claseRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public DisponibilidadClaseView disponibilidad(Integer claseId) {
        // Ajusta el estado si quieres considerar otro filtro
        return claseRepo.disponibilidad(claseId, EstadoReserva.RESERVADA);
    }

    private ClaseResponse toResponse(Clase c) {
        String instructorNombre = c.getInstructor().getNombres() + " " + c.getInstructor().getApellidos();
        // Asegúrate de que ClaseResponse use Integer para los IDs.
        return new ClaseResponse(
                c.getId(),
                c.getTitulo(),
                c.getDescripcion(),
                c.getTipo(),
                c.getInstructor().getId(),
                instructorNombre,
                c.getFechaInicio(),
                c.getDuracionMinutos(),
                c.getCapacidad(),
                c.getEstado(),
                c.getFechaCreacion(),
                c.getFechaActualizacion());
    }
}
