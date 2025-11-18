import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RutinaService } from '../../../servicios/rutina.service'; // Importa el servicio

@Component({
  selector: 'app-mis-rutinas',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mis-rutinas.component.html',
  styleUrls: ['./mis-rutinas.component.scss']
})
export class MisRutinasComponent implements OnInit {
  misRutinas: any[] = []; // Array para guardar las rutinas
  isLoading = true; // Indicador de carga

  constructor(private rutinaService: RutinaService) {}

  ngOnInit(): void {
    this.cargarRutinas(); // Llama al método al iniciar
  }

  cargarRutinas(): void {
    this.isLoading = true;
    this.rutinaService.getMisRutinas().subscribe({
      next: (data) => {
        this.misRutinas = data;
        this.isLoading = false;
        console.log('Mis rutinas cargadas:', data);
      },
      error: (err) => {
        console.error('Error al cargar mis rutinas', err);
        this.isLoading = false;
        // Aquí podrías mostrar un mensaje de error al usuario
      }
    });
  }
}