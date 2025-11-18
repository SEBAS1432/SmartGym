package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Membresia;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.MembresiaResponse;
import com.gymnasio.repository.MembresiaRepository;
import com.gymnasio.dto.MembresiaRequest;
import java.util.List;
import java.util.stream.Collectors;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.MembresiaService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class MembresiaServiceImpl implements MembresiaService {

    private final MembresiaRepository membresiaRepo;
    private final UsuarioRepository usuarioRepo;

    public MembresiaServiceImpl(MembresiaRepository membresiaRepo, UsuarioRepository usuarioRepo) {
        this.membresiaRepo = membresiaRepo;
        this.usuarioRepo = usuarioRepo;
    }

    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    @Override
    public MembresiaResponse obtenerMembresiaActivaUsuarioAutenticado() {
        Usuario usuario = getUsuarioAutenticado();
        return membresiaRepo.findByUsuarioIdAndEstado(usuario.getId(), Membresia.EstadoMembresia.ACTIVA)
                .map(this::toResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró membresía activa"));
    }

    // --- MÉTODO NUEVO PARA ADMIN ---
    @Override
    @Transactional
    public MembresiaResponse crearMembresia(MembresiaRequest request) {
        Usuario usuario = usuarioRepo.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        
        // (Opcional: podrías añadir lógica para expirar membresías antiguas de este usuario)

        Membresia membresia = new Membresia();
        membresia.setUsuario(usuario);
        membresia.setTipo(request.tipo());
        membresia.setFechaInicio(request.fechaInicio());
        membresia.setFechaFin(request.fechaFin());
        membresia.setPrecio(request.precio());
        membresia.setEstado(request.estado());

        Membresia membresiaGuardada = membresiaRepo.save(membresia);

        return toResponse(membresiaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembresiaResponse> listarPorUsuarioId(Integer usuarioId) {
        // Usa el método del repositorio que ya tenías (o deberías tener)
        return membresiaRepo.findByUsuarioIdOrderByFechaFinDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private MembresiaResponse toResponse(Membresia m) {
        return new MembresiaResponse(
                m.getId(),
                m.getTipo(),
                m.getFechaInicio(),
                m.getFechaFin(),
                m.getPrecio(),
                m.getEstado()
        );
    }
}