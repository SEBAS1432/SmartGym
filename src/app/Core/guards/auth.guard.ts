import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = () => {
  const user = inject(AuthService).getCurrentUser();
  const router = inject(Router);
  if (user) return true;
  router.navigate(['/login']);
  return false;
};
