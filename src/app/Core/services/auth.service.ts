// auth.service.ts
import { Injectable } from '@angular/core';
import { USERS } from '../../mock-data/users';

export interface User {
  name: string;
  plan: string;
  validUntil: string;
  memberId?: string;
  email?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUser: User | null = null;

  private storageAvailable(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  login(email: string, password: string): boolean {
    const user = USERS.find(u => u.email === email && u.password === password);
    if (user) {
      const { password, ...safeUser } = user;
      this.currentUser = safeUser;

      if (this.storageAvailable()) {
        localStorage.setItem('currentUser', JSON.stringify(safeUser));
      }

      return true;
    }
    return false;
  }

  getCurrentUser(): User | null {
    if (!this.currentUser && this.storageAvailable()) {
      const saved = localStorage.getItem('currentUser');
      if (saved) this.currentUser = JSON.parse(saved);
    }
    return this.currentUser;
  }

  logout(): void {
    this.currentUser = null;
    if (this.storageAvailable()) {
      localStorage.removeItem('currentUser');
    }
  }

  isLoggedIn(): boolean {
    return this.getCurrentUser() !== null;
  }
}
