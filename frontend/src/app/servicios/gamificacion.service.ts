import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class GamificacionService {
  private apiUrl = '/api/gamificacion';

  constructor(private http: HttpClient) {}

  /** GET /api/gamificacion/mis-puntos */
  getMisPuntos(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/mis-puntos`);
  }

  /** GET /api/gamificacion/mis-logros */
  getMisLogros(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/mis-logros`);
  }
}
