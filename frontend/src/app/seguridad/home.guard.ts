import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AutenticacionService } from '../services/autenticacion.service';

/**
 * Este guardián protege la página de inicio (/).
 * - Permite el acceso a usuarios públicos y Clientes.
 * - Bloquea a Trainers y Admins, redirigiéndolos a su dashboard.
 */
export const homeGuard: CanActivateFn = (route, state) => {

  const authService = inject(AutenticacionService);
  const router = inject(Router);

  if (!authService.isLoggedIn()) {
    // 1. Si no está logueado (público), SÍ puede ver la página principal.
    return true;
  }

  // 2. Si ESTÁ logueado, revisamos el rol
  const rol = authService.getUsuarioRol();

  if (rol === 'ADMIN' || rol === 'TRAINER') {
    // 3. Si es Admin o Trainer, lo REDIRIGIMOS a su dashboard
    router.navigate(['/trainer/dashboard']);
    return false; // Y bloqueamos el acceso a la página de inicio (/)
  } else {
    // 4. Si es Cliente (o cualquier otro rol), SÍ puede ver la página principal.
    return true;
  }
};