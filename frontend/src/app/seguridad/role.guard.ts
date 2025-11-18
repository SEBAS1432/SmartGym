import { inject } from '@angular/core';
import { CanActivateFn, ActivatedRouteSnapshot, Router } from '@angular/router';
import { AutenticacionService } from '../services/autenticacion.service';

/**
 * Este guardián revisa si el usuario tiene el rol requerido para acceder a la ruta.
 */
export const roleGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {

  const authService = inject(AutenticacionService);
  const router = inject(Router);

  // 1. Obtenemos el rol del usuario que inició sesión
  const rolUsuario = authService.getUsuarioRol();

  // 2. Obtenemos los roles permitidos para esta ruta
  // (Esto lo definiremos en app.routes.ts)
  const rolesPermitidos = route.data['roles'] as Array<string>;

  // 3. Comprobamos si el rol del usuario está en la lista de roles permitidos
  if (rolUsuario && rolesPermitidos && rolesPermitidos.includes(rolUsuario)) {
    return true; // Tiene permiso, luz verde
  }

  // 4. Si no tiene permiso, lo redirigimos
  console.error('Acceso denegado. Rol no autorizado.');
  router.navigate(['/']); // O a una página de "acceso denegado"
  return false; // Luz roja
};