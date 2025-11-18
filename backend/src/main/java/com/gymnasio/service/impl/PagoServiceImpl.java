package com.gymnasio.service.impl;

import com.gymnasio.domain.model.Pago;
import com.gymnasio.domain.model.Usuario;
import com.gymnasio.dto.PagoResponse;
import com.gymnasio.domain.model.MetodoPago;
import com.gymnasio.dto.PagoRequest;
import com.gymnasio.repository.MembresiaRepository;
import com.gymnasio.repository.MetodoPagoRepository;
import com.gymnasio.domain.model.Membresia;
import com.gymnasio.repository.PagoRepository;
import com.gymnasio.repository.UsuarioRepository;
import com.gymnasio.service.PagoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PagoServiceImpl implements PagoService {

    private static final ZoneId ZONA_LIMA = ZoneId.of("America/Lima");

    private final PagoRepository pagoRepo;
    private final UsuarioRepository usuarioRepo;
    private final MembresiaRepository membresiaRepo;
    private final MetodoPagoRepository metodoPagoRepo;

    public PagoServiceImpl(PagoRepository pagoRepo,
            UsuarioRepository usuarioRepo,
            MembresiaRepository membresiaRepo,
            MetodoPagoRepository metodoPagoRepo) {
        this.pagoRepo = pagoRepo;
        this.usuarioRepo = usuarioRepo;
        this.membresiaRepo = membresiaRepo;
        this.metodoPagoRepo = metodoPagoRepo;
    }

    private Usuario getUsuarioAutenticado() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepo.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no autenticado"));
    }

    @Override
    public List<PagoResponse> listarPagosUsuarioAutenticado() {
        Usuario usuario = getUsuarioAutenticado();
        return pagoRepo.findByUsuarioIdOrderByFechaPagoDesc(usuario.getId())
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PagoResponse> listarPorUsuarioId(Integer usuarioId) {
        return pagoRepo.findByUsuarioIdOrderByFechaPagoDesc(usuarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // permite escritura
    public PagoResponse registrarPago(PagoRequest request) {
        Usuario usuario = usuarioRepo.findById(request.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        MetodoPago metodoPago = metodoPagoRepo.findById(request.metodoPagoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Método de pago no encontrado"));

        Membresia membresia = null;
        if (request.membresiaId() != null) {
            membresia = membresiaRepo.findById(request.membresiaId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Membresía no encontrada"));
        }

        Pago pago = new Pago();
        pago.setUsuario(usuario);
        pago.setMembresia(membresia);
        pago.setMetodoPago(metodoPago);
        pago.setMonto(request.monto());
        pago.setStatus(request.status());
        pago.setReferencia(request.referencia());

        // --- FECHA DE PAGO: convierte a OffsetDateTime ---
        // si no viene fecha, usa "ahora" UTC
        if (request.fechaPago() == null) {
            pago.setFechaPago(OffsetDateTime.now(ZoneOffset.UTC));
        } else {
            // request.fechaPago() es LocalDateTime -> interpreta en Lima y convierte a
            // Offset
            pago.setFechaPago(toOffsetFromLima(request.fechaPago()));
            // Si prefieres UTC directo, usa: toOffsetUTC(request.fechaPago())
        }

        Pago pagoGuardado = pagoRepo.save(pago);
        return toResponse(pagoGuardado);
    }

    private PagoResponse toResponse(Pago p) {
        Integer membresiaId = (p.getMembresia() != null) ? p.getMembresia().getId() : null;
        String metodoPagoNombre = (p.getMetodoPago() != null) ? p.getMetodoPago().getNombre() : "Desconocido";
        return new PagoResponse(
                p.getId(),
                membresiaId,
                metodoPagoNombre,
                p.getMonto(),
                p.getStatus(),
                p.getReferencia(),
                p.getFechaPago() // OffsetDateTime
        );
    }

    // Helpers de conversión
    private static OffsetDateTime toOffsetFromLima(LocalDateTime ldt) {
        return ldt.atZone(ZONA_LIMA).toOffsetDateTime();
    }

    @SuppressWarnings("unused")
    private static OffsetDateTime toOffsetUTC(LocalDateTime ldt) {
        return ldt.atOffset(ZoneOffset.UTC);
    }
}
