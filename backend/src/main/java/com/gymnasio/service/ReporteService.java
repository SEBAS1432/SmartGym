package com.gymnasio.service;

import com.gymnasio.dto.ReporteSemanalResponse;

public interface ReporteService {
    ReporteSemanalResponse obtenerReporteSemanal();
    String generarReporteSemanalCsv();
    byte[] generarReporteSemanalPdf();
}