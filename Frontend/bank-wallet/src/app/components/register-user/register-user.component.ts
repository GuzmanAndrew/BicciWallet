import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-register-user',
  standalone: true,
  imports: [NgIf, FormsModule],
  templateUrl: './register-user.component.html',
  styleUrl: './register-user.component.scss'
})
export class RegisterUserComponent {

  userData = {
    name: '',
    username: '',
    email: '',
    password: ''
  };

  showPassword = false;
  errorMessage = '';

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
  }

  isFormIncomplete(): boolean {
    return !this.userData.name ||
      !this.userData.username ||
      !this.userData.email ||
      !this.userData.password;
  }

  register() {
    if (this.isFormIncomplete()) {
      this.errorMessage = 'Por favor completa todos los campos';
      return;
    }

    // Validar formato de email
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(this.userData.email)) {
      this.errorMessage = 'Por favor ingresa un correo electrónico válido';
      return;
    }

    // Por ahora, simulamos el registro exitoso
    this.userService.register(this.userData)
      .subscribe({
        next: (response) => {
          console.log('Usuario registrado exitosamente', response);
          // Guardar datos de usuario para la siguiente pantalla
          localStorage.setItem('registeredUser', JSON.stringify(this.userData));
          this.router.navigate(['/register-account']);
        },
        error: (error) => {
          console.error('Error en registro', error);
          this.errorMessage = 'Error al registrar usuario. Por favor intenta nuevamente.';
        }
      });
  }

  navigateBack() {
    this.router.navigate(['/user-not-found']);
  }
}
