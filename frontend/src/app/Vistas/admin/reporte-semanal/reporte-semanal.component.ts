import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReporteService } from '../../../servicios/reporte.service'; // Importa el servicio

@Component({
  selector: 'app-reporte-semanal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reporte-semanal.component.html',
  styleUrls: ['./reporte-semanal.component.scss']
})
export class ReporteSemanalComponent implements OnInit {
  reporte: any = null;
  isLoading = true;
  isDownloadingCsv = false;
  isDownloadingPdf = false;

  constructor(private reporteService: ReporteService) {}

  ngOnInit(): void {
    this.cargarReporte();
  }

  cargarReporte(): void {
    this.isLoading = true;
    this.reporteService.getReporteSemanal().subscribe({
      next: (data) => {
        this.reporte = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar el reporte', err);
        this.isLoading = false;
      }
    });
  }

  private guardarArchivo(blob: Blob, extension: 'csv' | 'pdf'): void {
    const a = document.createElement('a');
    const objectUrl = URL.createObjectURL(blob);
    a.href = objectUrl;
    
    const filename = `reporte-semanal-${new Date().toISOString().substring(0, 10)}.${extension}`;
    a.download = filename;
    a.click();
    
    URL.revokeObjectURL(objectUrl);
  }

  descargarCsv(): void {
    this.isDownloadingCsv = true;
    this.reporteService.descargarReporteCsv().subscribe({ 
      next: (blob) => {
        this.guardarArchivo(blob, "csv"); 
        this.isDownloadingCsv = false;
      },
      error: (err) => {
        console.error('Error al descargar el CSV', err);
        alert('No se pudo descargar el reporte CSV.');
        this.isDownloadingCsv = false;
      }
    });
  }

  descargarPdf(): void {
    this.isDownloadingPdf = true;
    this.reporteService.descargarReportePdf().subscribe({
      next: (blob) => {
        this.guardarArchivo(blob, "pdf"); 
        this.isDownloadingPdf = false;
      },
      error: (err) => {
        console.error('Error al descargar el PDF', err);
        alert('No se pudo descargar el reporte PDF.');
        this.isDownloadingPdf = false;
      }
    });
  }
}