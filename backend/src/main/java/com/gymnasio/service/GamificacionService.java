package com.gymnasio.service;

import com.gymnasio.dto.LogroResponse;
import com.gymnasio.dto.PuntosSummaryResponse;
import java.util.List;

public interface GamificacionService {
    
    /**
     * Obtiene la lista de logros desbloqueados por el usuario autenticado.
     */
    List<LogroResponse> listarMisLogros();
    
    /**
     * Obtiene el total de puntos acumulados por el usuario autenticado.
     */
    PuntosSummaryResponse obtenerMisPuntos();
}