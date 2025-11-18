import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class MembresiaService {
  private apiUrl = '/api/membresias';

  constructor(private http: HttpClient) {}

  /** GET /api/membresias/mi-membresia */
  getMiMembresia(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/mi-membresia`);
  }

  /** POST /api/membresias */
  crearMembresia(datos: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, datos);
  }

  /** GET /api/membresias/usuario/{id} */
  getMembresiasPorUsuario(usuarioId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/usuario/${usuarioId}`);
  }
}
