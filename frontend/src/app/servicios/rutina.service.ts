// src/app/servicios/rutina.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RutinaService {
  private apiUrl = '/api/rutinas';

  constructor(private http: HttpClient) {}

  /** GET /api/rutinas/mis-rutinas */
  getMisRutinas(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-rutinas`);
  }

  /** GET /api/rutinas/usuario/{id} */
  getRutinasPorUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }

  /** GET /api/rutinas/{id} */
  getRutinaPorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  /** POST /api/rutinas */
  crearRutina(rutina: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, rutina);
  }

  /** PUT /api/rutinas/{id} */
  actualizarRutina(id: number, rutina: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, rutina);
  }

  /** DELETE /api/rutinas/{id} */
  eliminarRutina(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/${id}`);
  }
}
