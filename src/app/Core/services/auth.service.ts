import { Injectable } from '@angular/core';
import { USERS } from '../../mock-data/users';


@Injectable({ providedIn: 'root' })
export class AuthService {
  private currentUser: any = null;

  login(email: string, password: string): boolean {
    const user = USERS.find(u => u.email === email && u.password === password);
    if (user) {
      this.currentUser = user;
      localStorage.setItem('currentUser', JSON.stringify(user));
      return true;
    }
    return false;
  }

  getCurrentUser() {
    if (!this.currentUser) {
      const saved = localStorage.getItem('currentUser');
      if (saved) this.currentUser = JSON.parse(saved);
    }
    return this.currentUser;
  }

  logout() {
    this.currentUser = null;
    localStorage.removeItem('currentUser');
  }
}
