import { DatePipe, isPlatformBrowser } from '@angular/common';
import { ApplicationRef, ChangeDetectorRef, Component, Inject, NgZone, OnInit, PLATFORM_ID } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import { AccountService } from '../../services/account.service';
import Swal from 'sweetalert2';
import { AuthService } from '../../services/auth.service';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [DatePipe, FormsModule, RouterModule],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.scss'
})
export class TransferComponent implements OnInit {

  rawBalance: number = 0;
  username: any;
  now = Date.now();

  transferData = {
    amount: '',
    recipientUsername: ''
  };

  currentDate = new Date();

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private transactionService: TransactionService,
    private accountService: AccountService,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit() {
    if (isPlatformBrowser(this.platformId)) {
      this.username = this.authService.getUsername() || 'Usuario';
      this.loadAccountBalance();
    }
  }

  get accountBalance(): string {
    return this.rawBalance.toLocaleString();
  }

  navigateToHome() {
    this.router.navigate(['/home']);
  }

  makeTransfer() {
    if (!this.transferData.amount) {
      Swal.fire({
        title: '¡Error!',
        text: 'Por favor ingresa un monto para la transferencia.',
        icon: 'error',
        confirmButtonText: 'CERRAR',
        confirmButtonColor: '#f44336'
      });
      return;
    }

    this.transactionService.sendTransaction({
      receiverUsername: this.transferData.recipientUsername,
      amount: parseFloat(this.transferData.amount.replace(/,/g, ''))
    }).subscribe({
      next: (result) => {
        console.log('Transferencia exitosa', result);
        const transactionId = this.generateTransactionId();

        Swal.fire({
          title: '¡Transferencia Exitosa!',
          html: `
            <p>Tu transferencia por <strong>$${this.transferData.amount}</strong> a <strong>${this.transferData.recipientUsername}</strong> ha sido realizada con éxito.</p>
            <p class="transfer-id">ID de Transferencia: #${transactionId}</p>
          `,
          icon: 'success',
          confirmButtonText: 'CERRAR',
          confirmButtonColor: '#4CAF50',
          timer: 5000,
          timerProgressBar: true
        }).then((result) => {
          this.navigateToHome();
        });
      },
      error: (err) => {
        console.error('Error al realizar transferencia', err);

        Swal.fire({
          title: '¡Error!',
          text: 'Error: Fondos insuficientes',
          icon: 'error',
          confirmButtonText: 'CERRAR',
          confirmButtonColor: '#f44336'
        });
      }
    });
  }

  loadAccountBalance() {
    this.accountService.getAccountDetailsSecure().subscribe({
      next: (account) => {
        this.rawBalance = account.balance;
      },
      error: (error) => {
        console.error('Error al cargar saldo', error);
        this.rawBalance = 0;
      }
    });
  }

  generateTransactionId() {
    return Math.floor(100000 + Math.random() * 900000);
  }

  logout() {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#fcd34d',
      cancelButtonColor: '#fcd34d'
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.logout();
        this.router.navigate(['/']).then(() => {
          Swal.fire({
            title: 'Sesión cerrada',
            text: 'Has cerrado sesión correctamente',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
          });
        });
      }
    });
  }

}
