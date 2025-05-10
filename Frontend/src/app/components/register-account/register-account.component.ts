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

    this.accountService.createAccount(this.registeredUser.username)
      .subscribe({
        next: (response) => {
          console.log('Cuenta bancaria creada exitosamente', response);
          this.isLoading = false;
          localStorage.removeItem('registeredUser');
          this.router.navigate(['/login']);
        },
        error: (error) => {
          console.error('Error al crear la cuenta', error);
          this.isLoading = false;
          this.errorMessage = 'Ocurrió un error al crear tu cuenta bancaria.';
        }
      });
  }
}
