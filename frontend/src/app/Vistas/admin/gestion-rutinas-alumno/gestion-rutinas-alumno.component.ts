import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink, Router } from '@angular/router';
import { RutinaService } from '../../../servicios/rutina.service';
import { UsuarioService } from '../../../servicios/usuario.service'; 

@Component({
  selector: 'app-gestion-rutinas-alumno',
  standalone: true,
  imports: [CommonModule, RouterLink], // Importa RouterLink
  templateUrl: './gestion-rutinas-alumno.component.html',
  styleUrls: ['./gestion-rutinas-alumno.component.scss']
})
export class GestionRutinasAlumnoComponent implements OnInit {
  rutinas: any[] = [];
  isLoading = true;
  alumnoId: number | null = null;
  alumnoNombre: string = ''; // Para mostrar "Rutinas de [Nombre]"

  constructor(
    private route: ActivatedRoute, // Para leer el ID de la URL
    private router: Router, // Para navegar al formulario de creación
    private rutinaService: RutinaService,
    private usuarioService: UsuarioService // Para obtener datos del alumno
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.alumnoId = +id; // Convierte el ID a número
      this.cargarAlumno();
      this.cargarRutinas();
    } else {
      console.error('No se encontró ID del alumno');
      this.isLoading = false;
    }
  }

  cargarAlumno(): void {
    // Obtenemos los datos del alumno para mostrar su nombre
    this.usuarioService.getUsuario(this.alumnoId!.toString()).subscribe(data => {
      this.alumnoNombre = `${data.nombres} ${data.apellidos}`;
    });
  }

  cargarRutinas(): void {
    this.isLoading = true;
    this.rutinaService.getRutinasPorUsuario(this.alumnoId!).subscribe({
      next: (data) => {
        this.rutinas = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error al cargar rutinas', err);
        this.isLoading = false;
      }
    });
  }

  eliminar(rutinaId: number): void {
    if (!confirm('¿Estás seguro de que deseas eliminar esta rutina?')) {
      return;
    }
    this.rutinaService.eliminarRutina(rutinaId).subscribe({
      next: () => {
        alert('Rutina eliminada con éxito');
        this.cargarRutinas(); // Recarga la lista
      },
      error: (err) => {
        console.error('Error al eliminar rutina', err);
        alert('Error al eliminar la rutina.');
      }
    });
  }

  // Método para navegar al formulario de creación
  irACrearRutina(): void {
    // Navegamos a la futura ruta, pasando el ID del alumno
    this.router.navigate(['/trainer/rutinas/nuevo'], { queryParams: { alumnoId: this.alumnoId } });
  }
}