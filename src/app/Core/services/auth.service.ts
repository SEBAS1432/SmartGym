// auth.service.ts
import { Injectable } from '@angular/core';
import { USERS, User, AuthenticatedUser } from '../../mock-data/users';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private usuarios: User[] = USERS;

  constructor() { }

  agregarUsuario(datosRegistro: { name: string, lastname: string, email: string, password: string }) {
    
    const nuevoUsuario: User = {
      name: datosRegistro.name,
      lastname: datosRegistro.lastname,
      email: datosRegistro.email,
      password: datosRegistro.password
    };
  
    this.usuarios.push(nuevoUsuario);
    console.log('Nuevo usuario registrado (sin membresía):', this.usuarios);
  }

  private currentUser: AuthenticatedUser | null = null;


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

  getCurrentUser(): AuthenticatedUser | null {
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
