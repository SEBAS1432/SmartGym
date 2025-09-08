import { Injectable } from '@angular/core';
import { USERS } from '../../app/mock-data/mock-data';

@Injectable({ providedIn: 'root' })
export class UserService {
    private users = USERS;

    login(email: string, password: string) {
    return this.users.find(u => u.email === email && u.password === password) || null;
    }

    getAllUsers() {
    return this.users;
    }
}
