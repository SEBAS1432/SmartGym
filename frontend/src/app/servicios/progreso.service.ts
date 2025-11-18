import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProgresoService {
  private apiUrl = '/api/progreso';

  constructor(private http: HttpClient) {}

  /** GET /api/progreso/mi-progreso */
  getMiProgreso(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mi-progreso`);
  }

  /** POST /api/progreso */
  crearProgreso(datos: any): Observable<any> {
    return this.http.post<any>(this.apiUrl, datos);
  }
}
