import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-not-found',
  standalone: true,
  imports: [],
  templateUrl: './user-not-found.component.html',
  styleUrl: './user-not-found.component.scss'
})
export class UserNotFoundComponent {

  constructor(private router: Router) { }

  navigateToRegister() {
    this.router.navigate(['/register-user']);
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }

}
