import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const guestGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  const user = userService.getCurrentUser();

  if (user) {
    router.navigate(['/dashboard']);
    return false; // bloquea acceso a /login
  }
  return true;
};
