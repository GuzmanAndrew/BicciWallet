import { isPlatformBrowser, NgIf } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { RegistrationStateService } from '../../services/registration-state.service';

@Component({
  selector: 'app-register-account',
  standalone: true,
  imports: [NgIf],
  templateUrl: './register-account.component.html',
  styleUrl: './register-account.component.scss'
})
export class RegisterAccountComponent implements OnInit {

  registeredUser: any = null;
  isLoading: boolean = false;
  errorMessage: string = '';

  constructor(
    private router: Router,
    private accountService: AccountService,
    private registrationStateService: RegistrationStateService
  ) { }

  ngOnInit() {
    this.registeredUser = this.registrationStateService.getRegisteredUser();

    if (!this.registeredUser) {
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

          this.registrationStateService.clearRegisteredUser();

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
