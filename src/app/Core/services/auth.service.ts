import { Injectable } from '@angular/core';
import { USERS, User, AuthenticatedUser } from '../../mock-data/users';

@Injectable({ providedIn: 'root' })
export class AuthService {
  
  // Clave para guardar la LISTA de usuarios en localStorage
  private usersStorageKey = 'smartgym_user_list';
  private usuarios: User[] = [];

  constructor() {
    // Al iniciar el servicio, cargamos los usuarios desde localStorage.
    // Si no hay nada, usamos la lista mock (USERS).
    if (this.storageAvailable()) {
      const storedUsers = localStorage.getItem(this.usersStorageKey);
      if (storedUsers) {
        this.usuarios = JSON.parse(storedUsers);
      } else {
        // Si no hay usuarios guardados, usamos el mock y lo guardamos
        this.usuarios = USERS;
        localStorage.setItem(this.usersStorageKey, JSON.stringify(this.usuarios));
      }
    } else {
      // Fallback si localStorage no está disponible
      this.usuarios = USERS;
    }
  }

  // Modificamos agregarUsuario para que devuelva un booleano (éxito o fracaso)
  agregarUsuario(datosRegistro: { name: string, lastname: string, email: string, password: string }): boolean {
    
    // 1. Comprobar si el email ya existe
    const emailExists = this.usuarios.find(u => u.email === datosRegistro.email);
    if (emailExists) {
      console.error('Error: El email ya está registrado.');
      return false; // Devuelve falso (fallo)
    }

    // 2. Si no existe, crea y agrega el nuevo usuario
    const nuevoUsuario: User = {
      name: datosRegistro.name,
      lastname: datosRegistro.lastname,
      email: datosRegistro.email,
      password: datosRegistro.password
    };
  
    this.usuarios.push(nuevoUsuario);

    // 3. Guardar la lista actualizada en localStorage
    if (this.storageAvailable()) {
      localStorage.setItem(this.usersStorageKey, JSON.stringify(this.usuarios));
    }

    console.log('Nuevo usuario registrado:', this.usuarios);
    return true; // Devuelve verdadero (éxito)
  }

  private currentUser: AuthenticatedUser | null = null;

  private storageAvailable(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }

  login(email: string, password: string): boolean {
    // CAMBIO CLAVE: Buscar en this.usuarios, no en USERS
    const user = this.usuarios.find(u => u.email === email && u.password === password);
    
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
