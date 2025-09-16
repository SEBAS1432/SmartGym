import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const guestGuard: CanActivateFn = () => {
  const user = inject(AuthService).getCurrentUser();
  const router = inject(Router);
  if (user) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
