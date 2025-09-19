// contact.component.ts
import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-contact',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './contacto.component.html',
  styleUrls: ['./contacto.component.css']
})
export class ContactoComponent {
  // Propiedades del formulario
  nombre: string = '';
  email: string = '';
  telefono: string = '';
  tipoConsulta: string = 'informacion';
  mensaje: string = '';
  
  // Estados del componente
  enviado: boolean = false;
  enviando: boolean = false;
  error: string = '';

  // Opciones para el select
  tiposConsulta = [
    { value: 'informacion', label: 'Información general' },
    { value: 'membresia', label: 'Consulta sobre membresías' },
    { value: 'clases', label: 'Clases y entrenadores' },
    { value: 'soporte', label: 'Soporte técnico' },
    { value: 'otros', label: 'Otros' }
  ];

  async enviarFormulario() {
    if (!this.validarFormulario()) {
      return;
    }

    this.enviando = true;
    this.error = '';

    try {
      // Simular envío del formulario
      await this.simularEnvio();
      
      this.enviado = true;
      this.limpiarFormulario();
    } catch (error) {
      this.error = 'Hubo un error al enviar el mensaje. Por favor, intenta nuevamente.';
    } finally {
      this.enviando = false;
    }
  }

  private validarFormulario(): boolean {
    if (!this.nombre.trim()) {
      this.error = 'El nombre es requerido';
      return false;
    }
    
    if (!this.email.trim() || !this.validarEmail(this.email)) {
      this.error = 'Por favor, ingresa un email válido';
      return false;
    }
    
    if (!this.mensaje.trim()) {
      this.error = 'El mensaje es requerido';
      return false;
    }
    
    return true;
  }

  private validarEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }

  private async simularEnvio(): Promise<void> {
    return new Promise((resolve) => {
      setTimeout(() => resolve(), 2000);
    });
  }

  private limpiarFormulario() {
    this.nombre = '';
    this.email = '';
    this.telefono = '';
    this.tipoConsulta = 'informacion';
    this.mensaje = '';
  }

  enviarOtroMensaje() {
    this.enviado = false;
    this.error = '';
  }
}