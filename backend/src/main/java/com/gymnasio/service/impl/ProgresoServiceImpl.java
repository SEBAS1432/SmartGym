package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Progreso;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.ProgresoRequest;
import com.gymnasio.dto.ProgresoResponse;
import com.gymnasio.repository.ProgresoRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.ProgresoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProgresoServiceImpl implements ProgresoService {

    private final ProgresoRepository progresoRepo;
    private final UsuarioRepository usuarioRepo;

    public ProgresoServiceImpl(ProgresoRepository progresoRepo, UsuarioRepository usuarioRepo) {
        this.progresoRepo = progresoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // Método para obtener el usuario autenticado
    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProgresoResponse> listarPorUsuarioAutenticado() {
        Usuario usuario = getUsuarioAutenticado();
        return progresoRepo.findByUsuarioIdOrderByFechaAsc(usuario.getId())
                .stream()
                .map(this::toResponse) // Usa el método helper
                .collect(Collectors.toList());
    }

    @Override
    public ProgresoResponse crear(ProgresoRequest request) {
        Usuario usuario = getUsuarioAutenticado();

        Progreso progreso = new Progreso(); // Crea una nueva instancia
        progreso.setUsuario(usuario);
        progreso.setFecha(request.fecha());
        progreso.setPesoKg(request.pesoKg());
        progreso.setGrasaCorporalPct(request.grasaCorporalPct());
        progreso.setNotas(request.notas());
        // Las fechas de creación/actualización se manejan automáticamente
        
        progreso = progresoRepo.save(progreso);
        return toResponse(progreso);
    }

    // Helper para convertir Entidad a DTO
    private ProgresoResponse toResponse(Progreso p) {
        return new ProgresoResponse(
                p.getId(),
                p.getFecha(),
                p.getPesoKg(),
                p.getGrasaCorporalPct(),
                p.getNotas(),
                p.getFechaCreacion()
        );
    }
}