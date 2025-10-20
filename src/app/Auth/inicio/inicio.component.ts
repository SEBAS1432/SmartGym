import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from '../../Core/services/auth.service';
import { User } from '../../mock-data/users'; // <-- Importa 'User' desde su archivo original
import { AuthenticatedUser } from '../../mock-data/users';



@Component({
  selector: 'app-inicio',
  imports: [CommonModule,RouterLink,RouterLinkActive],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent implements OnInit {
  user: AuthenticatedUser | null = null;

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
