import { Component } from '@angular/core';
import { AuthService } from '../../Core/services/auth.service'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router'; // Opcional, para redirigir

@Component({
  selector: 'app-registro',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './registro.component.html',
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent {
  usuario = {
    name: '',
    lastname: '',
    email: '',
    password: ''
  };

  // Inyecta AuthService y Router (opcional)
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  onSubmit() {
    // 1. Llama al método que ahora devuelve un booleano
    const success = this.authService.agregarUsuario(this.usuario);
    
    // 2. Comprueba la respuesta
    if (success) {
      alert('¡Usuario registrado con éxito!');
      
      // Limpia el formulario
      this.usuario = {
        name: '',
        lastname: '',
        email: '',
        password: ''
      };
      
      // Opcional: Redirige al login
      // this.router.navigate(['/login']);

    } else {
      // Si devuelve falso, es porque el email ya existía
      alert('Error: El email ya se encuentra registrado.');
    }
  }
}
