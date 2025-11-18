package com.gymnasio.service;
import com.gymnasio.dto.PagoResponse;
import com.gymnasio.dto.PagoRequest;
import java.util.List;

public interface PagoService {
    /**
     * Obtiene el historial de pagos del usuario autenticado,
     * ordenado por fecha descendente.
     */
    List<PagoResponse> listarPagosUsuarioAutenticado();

    PagoResponse registrarPago(PagoRequest request);

    List<PagoResponse> listarPorUsuarioId(Integer usuarioId);
}