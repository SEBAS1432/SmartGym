import { Component } from '@angular/core';
// 1. Cambia la importación para que apunte a AuthService
import { AuthService } from '../../Core/services/auth.service'; 
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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
    lastname: '', // <-- Agregado
    email: '',
    password: ''
  };

  // 2. Inyecta AuthService en el constructor
  constructor(private authService: AuthService) { }

  onSubmit() {
    // 3. Llama al método desde authService
    this.authService.agregarUsuario(this.usuario);
    
    alert('¡Usuario registrado con éxito!');
    this.usuario = {
      name: '',
      lastname: '',
      email: '',
      password: ''
    };
  }
}