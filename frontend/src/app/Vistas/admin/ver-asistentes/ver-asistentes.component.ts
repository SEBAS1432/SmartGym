import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router'; // Importa ActivatedRoute
import { ReservaService } from '../../../servicios/reserva.service';
import { ClaseService } from '../../../servicios/clase.service'; // Para obtener info de la clase

@Component({
  selector: 'app-ver-asistentes',
  standalone: true,
  imports: [CommonModule, RouterLink], // Importa RouterLink
  templateUrl: './ver-asistentes.component.html',
  styleUrls: ['./ver-asistentes.component.scss']
})
export class VerAsistentesComponent implements OnInit {
  asistentes: any[] = [];
  claseInfo: any = null; // Para guardar la info de la clase
  claseId: number | null = null;
  isLoading = true;

  constructor(
    private route: ActivatedRoute, // Para leer el ID de la URL
    private reservaService: ReservaService,
    private claseService: ClaseService // Para cargar datos de la clase
  ) {}

  ngOnInit(): void {
    // Obtenemos el ID de la clase desde la URL
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.claseId = +id;
      this.cargarInformacion();
    } else {
      console.error('No se encontró ID de la clase');
      this.isLoading = false;
    }
  }

  cargarInformacion(): void {
    this.isLoading = true;

    // 1. Carga la información de la clase
    this.claseService.getClasePorId(this.claseId!).subscribe({
      next: (dataClase) => {
        this.claseInfo = dataClase;

        // 2. Carga la lista de asistentes
        this.reservaService.getAsistentesPorClase(this.claseId!).subscribe({
          next: (dataAsistentes) => {
            this.asistentes = dataAsistentes;
            this.isLoading = false;
          },
          error: (err) => {
            console.error('Error al cargar asistentes', err);
            this.isLoading = false;
          }
        });
      },
      error: (err) => {
        console.error('Error al cargar la clase', err);
        this.isLoading = false;
      }
    });
  }
}