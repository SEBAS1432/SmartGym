package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.LogroResponse;
import com.gymnasio.dto.PuntosSummaryResponse;
import com.gymnasio.repository.LogroUsuarioRepository;
import com.gymnasio.repository.PuntosUsuarioRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.GamificacionService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // Por defecto, solo lectura
public class GamificacionServiceImpl implements GamificacionService {

    private final LogroUsuarioRepository logroUsuarioRepo;
    private final PuntosUsuarioRepository puntosUsuarioRepo;
    private final UsuarioRepository usuarioRepo;

    public GamificacionServiceImpl(LogroUsuarioRepository logroUsuarioRepo,
                                   PuntosUsuarioRepository puntosUsuarioRepo,
                                   UsuarioRepository usuarioRepo) {
        this.logroUsuarioRepo = logroUsuarioRepo;
        this.puntosUsuarioRepo = puntosUsuarioRepo;
        this.usuarioRepo = usuarioRepo;
    }

    // Helper para obtener el usuario logueado
    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    @Override
    public List<LogroResponse> listarMisLogros() {
        Usuario usuario = getUsuarioAutenticado();
        // Usa el método con JOIN FETCH del repositorio
        return logroUsuarioRepo.findByUsuarioIdWithLogro(usuario.getId())
                .stream()
                // Mapea la entidad LogroUsuario al DTO LogroResponse
                .map(logroUsuario -> new LogroResponse(
                        logroUsuario.getLogro().getNombre(),      // Accede al nombre desde el Logro anidado
                        logroUsuario.getLogro().getDescripcion(), // Accede a la descripción
                        logroUsuario.getLogro().getPuntos(),      // Accede a los puntos
                        logroUsuario.getFecha()                   // Fecha en que se obtuvo (de LogroUsuario)
                ))
                .collect(Collectors.toList());
    }

    @Override
    public PuntosSummaryResponse obtenerMisPuntos() {
        Usuario usuario = getUsuarioAutenticado();
        // Usa el método SUM del repositorio
        Integer total = puntosUsuarioRepo.sumPuntosByUsuarioId(usuario.getId());
        // Si el usuario no tiene puntos, la suma devuelve null, así que retornamos 0
        return new PuntosSummaryResponse(total != null ? total : 0);
    }
}