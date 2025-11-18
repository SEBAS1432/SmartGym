// src/app/servicios/trainer-dashboard.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class TrainerDashboardService {
  private apiUrl = '/api/trainer-dashboard/stats';

  constructor(private http: HttpClient) {}

  /** GET /api/trainer-dashboard/stats */
  getStats(): Observable<any> {
    return this.http.get<any>(this.apiUrl);
  }
}
