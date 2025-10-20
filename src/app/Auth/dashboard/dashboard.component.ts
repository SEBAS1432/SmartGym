// dashboard.component.ts
import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../Core/services/auth.service';
import { User } from '../../mock-data/users'; // <-- Importa 'User' desde su archivo original
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthenticatedUser } from '../../mock-data/users';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  user: AuthenticatedUser | null = null;

  sections = [
    { name: 'Rutinas personalizadas', icon: '🏃‍♀️', route: '/rutinas' },
    { name: 'Clases disponibles', icon: '📚', route: '/clases' },
    { name: 'Progreso de entrenamiento', icon: '📊', route: '/progreso' },
    { name: 'Contacto', icon: '📞', route: '/contacto' }
  ];

  constructor(private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser();
    if (!this.user) {
      // Si no hay usuario, lo mandamos al login
      this.router.navigate(['/login']);
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  navigateToSection(route: string): void {
    this.router.navigate([route]);
  }

  getPlanDisplayName(plan: string): string {
    const planNames: { [key: string]: string } = {
      'premium': 'Premium',
      'basic': 'Básico',
      'pro': 'Pro'
    };
    return planNames[plan.toLowerCase()] || plan;
  }

  goHome(): void {
    this.router.navigate(['/']);
  }
  

  getCurrentDate(): string {
    return new Date().toLocaleDateString();
  }
}

