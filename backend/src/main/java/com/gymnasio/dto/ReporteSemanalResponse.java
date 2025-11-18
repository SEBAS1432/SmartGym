package com.gymnasio.dto;

import java.math.BigDecimal;
import java.time.LocalDate;


public record ReporteSemanalResponse(
    LocalDate fechaInicio,
    LocalDate fechaFin,
    long nuevosUsuarios,
    long totalPagos,
    BigDecimal ingresosTotales,
    long clasesCreadas,
    long reservasHechas
) {
}