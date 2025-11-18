import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AutenticacionService } from '../services/autenticacion.service';

/**
 * Este es el guardián de autenticación.
 * Decide si un usuario puede acceder a una ruta protegida.
 */
export const authGuard: CanActivateFn = (route, state) => {
  
  // Inyectamos los servicios que necesitamos
  const authService = inject(AutenticacionService);
  const router = inject(Router);

  // Comprobamos si el usuario ha iniciado sesión
  if (authService.isLoggedIn()) {
    return true; // Si está logueado, le damos permiso (luz verde)
  } else {
    // Si NO está logueado, lo redirigimos a la página de login
    console.warn('Acceso denegado: El usuario no está autenticado.');
    router.navigate(['/login']);
    return false; // No le damos permiso (luz roja)
  }
};