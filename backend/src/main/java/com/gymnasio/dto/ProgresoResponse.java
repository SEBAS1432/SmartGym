package com.gymnasio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

// DTO para devolver la información al frontend
public record ProgresoResponse(
        Integer id,
        LocalDate fecha,
        BigDecimal pesoKg,
        BigDecimal grasaCorporalPct,
        String notas,
        OffsetDateTime fechaCreacion
) {
}