// src/app/servicios/reserva.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ReservaService {
  private apiUrl = '/api/reservas';

  constructor(private http: HttpClient) {}

  /** POST /api/reservas  { claseId } */
  crearReserva(claseId: number): Observable<any> {
    const body = { claseId };
    return this.http.post<any>(this.apiUrl, body);
  }

  /** GET /api/reservas/mis */
  getMisReservas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis`);
  }

  /** PUT /api/reservas/{id}/cancelar */
  cancelarReserva(id: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}/cancelar`, {});
  }

  /** GET /api/reservas/clase/{claseId} */
  getAsistentesPorClase(claseId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/clase/${claseId}`);
  }
}
