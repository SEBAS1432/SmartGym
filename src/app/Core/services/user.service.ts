import { Injectable } from '@angular/core';
import { USERS } from '../../mock-data/mock-data';

@Injectable({ providedIn: 'root' })
export class UserService {
  private users = USERS;
  private currentUser: any = null;

  login(email: string, password: string) {
    const user = this.users.find(u => u.email === email && u.password === password);
    if (user) {
      this.currentUser = user;
      if (typeof window !== 'undefined') {
        localStorage.setItem('currentUser', JSON.stringify(user));
      }
      return true;
    }
    return false;
  }

  getCurrentUser() {
    if (!this.currentUser && typeof window !== 'undefined') {
      const saved = localStorage.getItem('currentUser');
      if (saved) this.currentUser = JSON.parse(saved);
    }
    return this.currentUser;
  }

  logout() {
    this.currentUser = null;
    if (typeof window !== 'undefined') {
      localStorage.removeItem('currentUser');
    }
  }
}
