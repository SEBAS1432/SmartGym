import { Component } from '@angular/core';
import { UserService } from '../../Core/services/user.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent {
  user: any;

  constructor(private userService: UserService) {
    this.user = this.userService.getCurrentUser();
  }

  logout() {
    this.userService.logout();
    window.location.href = '/login';
  }
}
