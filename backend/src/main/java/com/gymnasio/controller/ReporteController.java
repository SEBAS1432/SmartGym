package com.gymnasio.controller;

import com.gymnasio.dto.ReporteSemanalResponse;
import com.gymnasio.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@Tag(name = "Reporte Controller", description = "Endpoints para reportes y estadísticas (ADMIN)")
public class ReporteController {

    private final ReporteService service;

    public ReporteController(ReporteService service) {
        this.service = service;
    }

    @Operation(summary = "Obtener reporte de estadísticas de la última semana (ADMIN)")
    @GetMapping("/resumen-semanal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReporteSemanalResponse> getReporteSemanal() {
        return ResponseEntity.ok(service.obtenerReporteSemanal());
    }

    @Operation(summary = "Descargar reporte semanal detallado en CSV (ADMIN)")
    @GetMapping("/resumen-semanal/csv")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> getReporteSemanalCsv() {
        String csvData = service.generarReporteSemanalCsv();
        
        // Define el nombre del archivo
        String filename = "reporte-semanal-" + LocalDate.now() + ".csv";
        
        // Prepara los headers de la respuesta para forzar la descarga
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv; charset=utf-8");

        return new ResponseEntity<>(csvData, headers, HttpStatus.OK);
    }

    @Operation(summary = "Descargar reporte semanal detallado en PDF (ADMIN)")
    @GetMapping("/resumen-semanal/pdf")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> getReporteSemanalPdf() {
        byte[] pdfData = service.generarReporteSemanalPdf();
        
        // Define el nombre del archivo
        String filename = "reporte-semanal-" + LocalDate.now() + ".pdf";
        
        // Prepara los headers de la respuesta para forzar la descarga
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF) // <-- Indica que es un PDF
                .body(pdfData);
    }
}