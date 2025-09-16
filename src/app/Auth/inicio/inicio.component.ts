import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, User } from '../../Core/services/auth.service'; // 👈 importa tu servicio

@Component({
  selector: 'app-inicio',
  imports: [CommonModule],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent implements OnInit {
  user: User | null = null;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit(): void {
    this.user = this.authService.getCurrentUser(); // 👈 obtenemos el usuario si está logeado
  }

  goToLogin() {
    this.router.navigate(['/login']);
  }

  logout() {
    this.authService.logout();
    this.user = null;
  }
}
