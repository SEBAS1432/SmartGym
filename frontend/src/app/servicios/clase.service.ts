import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ClaseService {
  private apiUrl = '/api/clases';

  constructor(private http: HttpClient) {}

  // --- CLIENTE / ADMIN / TRAINER ---
  getClases(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getClasePorId(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  // --- SOLO ADMIN/TRAINER ---
  crearClase(clase: any): Observable<any> {
    return this.http.post(this.apiUrl, clase);
  }

  actualizarClase(id: number, clase: any): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, clase);
  }

  eliminarClase(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`);
  }
}
