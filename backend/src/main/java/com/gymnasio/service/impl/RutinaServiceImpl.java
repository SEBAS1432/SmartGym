package com.gymnasio.service.impl;

import org.springframework.web.server.ResponseStatusException;
import java.util.Set;
import org.springframework.http.HttpStatus;
import com.gymnasio.dto.EjercicioRequest;
import com.gymnasio.dto.RutinaRequest;
import com.gymnasio.domain.model.Rutina;
import com.gymnasio.domain.model.RutinaEjercicio;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.EjercicioResponse;
import com.gymnasio.dto.RutinaResponse;
import com.gymnasio.repository.RutinaRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.RutinaService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RutinaServiceImpl implements RutinaService {

    private final RutinaRepository rutinaRepo;
    private final UsuarioRepository usuarioRepo;

    public RutinaServiceImpl(RutinaRepository rutinaRepo, UsuarioRepository usuarioRepo) {
        this.rutinaRepo = rutinaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    // --- Implementación para Cliente ---
    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponse> listarPorUsuarioAutenticado() {
        Usuario usuario = getUsuarioAutenticado();
        return listarPorUsuarioId(usuario.getId()); // Reutiliza el método de abajo
    }

    // --- Implementaciones para Trainer/Admin ---

    @Override
    @Transactional(readOnly = true)
    public List<RutinaResponse> listarPorUsuarioId(Integer usuarioId) {
        List<Rutina> rutinas = rutinaRepo.findByUsuarioIdWithEjercicios(usuarioId);
        return rutinas.stream()
                .map(this::toRutinaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RutinaResponse crear(RutinaRequest request) {
        // 1. Busca al usuario al que se le asignará la rutina
        Usuario usuario = usuarioRepo.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // 2. Crea la entidad Rutina
        Rutina nuevaRutina = new Rutina();
        nuevaRutina.setUsuario(usuario);
        nuevaRutina.setNombre(request.nombre());
        nuevaRutina.setObjetivo(request.objetivo());
        // Estado por defecto ya está en la entidad (ACTIVA)
        
        // 3. Crea y añade los ejercicios a la rutina
        if (request.ejercicios() != null) {
            Set<RutinaEjercicio> ejerciciosSet = request.ejercicios().stream()
                    .map(ejReq -> crearEjercicioDesdeRequest(ejReq, nuevaRutina))
                    .collect(Collectors.toSet());
            nuevaRutina.setEjercicios(ejerciciosSet);
        }

        // 4. Guarda la rutina (y los ejercicios gracias a CascadeType.ALL)
        Rutina rutinaGuardada = rutinaRepo.save(nuevaRutina);
        return toRutinaResponse(rutinaGuardada);
    }

    @Override
    public RutinaResponse actualizar(Integer rutinaId, RutinaRequest request) {
        Rutina rutina = rutinaRepo.findById(rutinaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada"));

        //Actualiza los datos de la rutina
        rutina.setNombre(request.nombre());
        rutina.setObjetivo(request.objetivo());

        rutina.getEjercicios().clear(); // Limpia la lista actual
        
        if (request.ejercicios() != null) {
            Set<RutinaEjercicio> ejerciciosSet = request.ejercicios().stream()
                    .map(ejReq -> crearEjercicioDesdeRequest(ejReq, rutina))
                    .collect(Collectors.toSet());
            rutina.getEjercicios().addAll(ejerciciosSet); // Añade los nuevos
        }

        Rutina rutinaActualizada = rutinaRepo.save(rutina);
        return toRutinaResponse(rutinaActualizada);
    }

    @Override
    public void eliminar(Integer rutinaId) {
        // (Opcional) Aquí también se podrían verificar permisos
        if (!rutinaRepo.existsById(rutinaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada");
        }
        rutinaRepo.deleteById(rutinaId);
    }
    
    // --- Métodos Helper ---

    // Helper para crear una entidad RutinaEjercicio desde un DTO
    private RutinaEjercicio crearEjercicioDesdeRequest(EjercicioRequest ejReq, Rutina rutina) {
        RutinaEjercicio ej = new RutinaEjercicio();
        ej.setRutina(rutina);
        ej.setEjercicio(ejReq.ejercicio());
        ej.setSeries(ejReq.series());
        ej.setRepeticiones(ejReq.repeticiones());
        ej.setDescansoSegundos(ejReq.descansoSegundos());
        return ej;
    }

    // (El resto de tus helpers 'toRutinaResponse' y 'toEjercicioResponse' 
    // que ya tenías están perfectos, no necesitas cambiarlos)
    
    private RutinaResponse toRutinaResponse(Rutina rutina) {
        List<EjercicioResponse> ejerciciosDto = rutina.getEjercicios() != null ?
                rutina.getEjercicios().stream()
                        .map(this::toEjercicioResponse) 
                        .sorted(Comparator.comparing(EjercicioResponse::id)) 
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return new RutinaResponse(
                rutina.getId(),
                rutina.getUsuario().getId(),
                rutina.getNombre(),
                rutina.getObjetivo(),
                rutina.getEstado(),
                rutina.getFechaCreacion(),
                ejerciciosDto
        );
    }

    private EjercicioResponse toEjercicioResponse(RutinaEjercicio ejercicio) {
        return new EjercicioResponse(
                ejercicio.getId(),
                ejercicio.getEjercicio(),
                ejercicio.getSeries(),
                ejercicio.getRepeticiones(),
                ejercicio.getDescansoSegundos()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RutinaResponse obtenerPorId(Integer rutinaId) {

        Rutina rutina = rutinaRepo.findById(rutinaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Rutina no encontrada"));
        
        return toRutinaResponse(rutina);
    }
}
