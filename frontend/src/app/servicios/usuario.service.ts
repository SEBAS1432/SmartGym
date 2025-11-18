import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {
  // USA LA URL COMPLETA para evitar problemas
  private apiUrl = 'http://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) { }

  // --- AÑADE ESTA FUNCIÓN HELPER ---
  private getHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  // --- ACTUALIZA TODOS TUS MÉTODOS ---

  /** GET /api/usuarios/{id} */
  getUsuario(id: string): Observable<any> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.get<any>(`${this.apiUrl}/${id}`, { headers });
  }

  /** PUT /api/usuarios/{id} */
  actualizarUsuario(id: string, datos: any): Observable<any> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.put<any>(`${this.apiUrl}/${id}`, datos, { headers });
  }

  /** GET /api/usuarios/lista-simple */
  getInstructores(): Observable<any[]> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.get<any[]>(`${this.apiUrl}/lista-simple`, { headers });
  }

  /** GET /api/usuarios/rol/CLIENTE */
  getClientes(): Observable<any[]> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.get<any[]>(`${this.apiUrl}/rol/CLIENTE`, { headers });
  }

  // --- ADMIN ---
  /** GET /api/usuarios */
  getAllUsuarios(): Observable<any[]> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.get<any[]>(this.apiUrl, { headers });
  }

  /** POST /api/usuarios */
  crearUsuario(datos: any): Observable<any> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.post<any>(this.apiUrl, datos, { headers });
  }

  /** DELETE /api/usuarios/{id} */
  eliminarUsuario(id: number): Observable<any> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.delete<any>(`${this.apiUrl}/${id}`, { headers });
  }

  /** PUT /api/usuarios/{id}/activar */
  activarUsuario(id: number): Observable<any> {
    const headers = this.getHeaders(); // <-- AÑADE HEADERS
    return this.http.put<any>(`${this.apiUrl}/${id}/activar`, {}, { headers });
  }
}