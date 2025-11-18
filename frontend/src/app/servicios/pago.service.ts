import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PagoService {
  private apiUrl = '/api/pagos';

  constructor(private http: HttpClient) {}

  /** GET /api/pagos/mi-historial */
  getMiHistorialPagos(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mi-historial`);
  }

  /** POST /api/pagos */
  registrarPago(datos: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, datos);
  }

  /** GET /api/pagos/usuario/{id} */
  getPagosPorUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }
}
