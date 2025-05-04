import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';

@Component({
  selector: 'app-register-account',
  standalone: true,
  imports: [NgIf],
  templateUrl: './register-account.component.html',
  styleUrl: './register-account.component.scss'
})
export class RegisterAccountComponent {

  registeredUser: any = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private accountService: AccountService
  ) { }

  ngOnInit() {
    // Recuperar datos del usuario registrado
    const userData = localStorage.getItem('registeredUser');
    if (userData) {
      this.registeredUser = JSON.parse(userData);
    } else {
      this.errorMessage = 'No se encontraron datos de usuario. Por favor regístrese primero.';
    }
  }

  navigateBack() {
    this.router.navigate(['/register-user']);
  }

  finishRegistration() {
    if (!this.registeredUser) {
      this.errorMessage = 'No hay datos de usuario para crear la cuenta.';
      return;
    }

    this.isLoading = true;

    // Simulamos la creación de cuenta bancaria
    setTimeout(() => {
      // En un caso real, esto llamaría al servicio:
      // this.accountService.createAccount(this.registeredUser.id)

      this.isLoading = false;

      // Limpiamos datos temporales
      localStorage.removeItem('registeredUser');

      // Redirigimos al login
      this.router.navigate(['/login']);
    }, 1500); // Simulamos un retardo para mostrar el spinner
  }
}
