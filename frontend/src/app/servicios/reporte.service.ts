import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  private apiUrl = '/api/reportes';

  constructor(private http: HttpClient) {}

  /** GET /api/reportes/resumen-semanal */
  getReporteSemanal(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/resumen-semanal`);
  }

  /** GET /api/reportes/resumen-semanal/csv -> Blob */
  descargarReporteCsv(): Observable<Blob> {
    return this.http.get<Blob>(`${this.apiUrl}/resumen-semanal/csv`, {
      responseType: 'blob' as 'json'
    });
  }

  /** GET /api/reportes/resumen-semanal/pdf -> Blob */
  descargarReportePdf(): Observable<Blob> {
    return this.http.get<Blob>(`${this.apiUrl}/resumen-semanal/pdf`, {
      responseType: 'blob' as 'json'
    });
  }
}
