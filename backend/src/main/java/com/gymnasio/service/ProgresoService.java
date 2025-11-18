package com.gymnasio.service;

import com.gymnasio.dto.ProgresoRequest;
import com.gymnasio.dto.ProgresoResponse;
import java.util.List;

public interface ProgresoService {
    /**
     * Obtiene el historial de progreso del usuario autenticado.
     */
    List<ProgresoResponse> listarPorUsuarioAutenticado();

    /**
     * Crea un nuevo registro de progreso para el usuario autenticado.
     */
    ProgresoResponse crear(ProgresoRequest request);
}