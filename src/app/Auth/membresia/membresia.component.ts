import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-membresia',
  imports: [],
  templateUrl: './membresia.component.html',
  styleUrl: './membresia.component.css'
})
export class MembresiaComponent {
    constructor(private router: Router) {}

  goToLogin() {
    this.router.navigate(['/login']);
  }
}
