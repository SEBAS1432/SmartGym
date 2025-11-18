// src/main/java/com/gymnasio/service/ReservaService.java
package com.gymnasio.service;

import com.gymnasio.dto.ReservaRequest;
import com.gymnasio.dto.ReservaResponse;

import java.util.List;

public interface ReservaService {
    ReservaResponse reservarComoCliente(ReservaRequest req);
    void cancelarMiReserva(Integer reservaId);
    List<ReservaResponse> misReservas();
    List<ReservaResponse> asistentesDeClase(Integer claseId);
}