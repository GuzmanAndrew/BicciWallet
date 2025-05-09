import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  username: string = '';
  password: string = '';
  showPassword: boolean = false;

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  login() {
    if (!this.username || !this.password) {
      alert('Por favor ingresa usuario y contraseña');
      return;
    }

    // Por ahora, simulamos la validación, en una etapa posterior
    // lo conectaremos con el backend real
    this.userService.login(this.username, this.password)
      .subscribe({
        next: (response) => {
          console.log('Login exitoso', response);
          this.router.navigate(['/home']);
        },
        error: (error) => {
          console.error('Error en login', error);
          // Simular que el usuario no existe
          this.router.navigate(['/user-not-found']);
        }
      });
  }
}
