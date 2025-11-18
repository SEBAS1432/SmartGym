import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TrainerDashboardService } from '../../../servicios/trainer-dashboard.service'; // Importa el servicio

@Component({
  selector: 'app-dashboard-trainer',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard-trainer.component.html',
  styleUrls: ['./dashboard-trainer.component.scss']
})
export class DashboardTrainerComponent implements OnInit {
  stats: any = null; // Para guardar las estadísticas
  isLoading = true;
  nombreTrainer: string = '';

  constructor(private dashboardService: TrainerDashboardService) {}

  ngOnInit(): void {
    this.cargarStats();
  }

  cargarStats(): void {
    this.isLoading = true;
    this.dashboardService.getStats().subscribe({
      next: (data) => {
        this.stats = data;
        this.nombreTrainer = data.nombreTrainer;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar estadísticas', err);
        this.isLoading = false;
      }
    });
  }
}