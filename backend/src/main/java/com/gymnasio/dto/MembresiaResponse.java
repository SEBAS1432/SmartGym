package com.gymnasio.dto;

import com.gymnasio.domain.model.Membresia.EstadoMembresia;
import java.math.BigDecimal;
import java.time.LocalDate;

public record MembresiaResponse(
        Integer id,
        String tipo,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal precio,
        EstadoMembresia estado
) {
}