import { NgClass, NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TransactionService } from '../../services/transaction.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgIf, NgFor, NgClass, RouterModule],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  Math = Math;

  username: any;
  transferLimit = 7500000;
  transferLimitPercentage = 33;

  accountDetails: {
    balance: number;
    accountNumber: string;
  } = {
      balance: 0,
      accountNumber: '****'
    };

  transactions: {
    name: string;
    date: string;
    amount: number;
  }[] = [];

  constructor(
    private router: Router,
    private accountService: AccountService,
    private transactionService: TransactionService
  ) { }

  ngOnInit() {
    this.username = localStorage.getItem('username') || 'Usuario';
    this.loadAccountDetails();
    this.loadTransactions();
  }

  loadAccountDetails() {
    this.accountService.getAccountDetailsSecure().subscribe({
      next: (account) => {
        this.accountDetails = account;
      },
      error: (error) => {
        console.error('Error al cargar detalles de la cuenta', error);
      }
    });
  }

  loadTransactions() {
    this.transactionService.getTransactionHistory().subscribe({
      next: (transactions) => {
        const currentUser = localStorage.getItem('username');
        this.transactions = transactions.map(tx => {
          const isIncoming = tx.receiverUsername === currentUser;
          const amount = isIncoming ? Math.abs(tx.amount) : -Math.abs(tx.amount);

          return {
            name: isIncoming ? tx.senderUsername : tx.receiverUsername,
            date: new Date(tx.timestamp).toLocaleDateString('es-ES', { day: 'numeric', month: 'long' }),
            amount: amount
          };
        });
      },
      error: (error) => {
        console.error('Error al cargar transacciones', error);
      }
    });
  }

  navigateToTransfer() {
    this.router.navigate(['/transfer']);
  }

  formatNumber(num: number): string {
    return new Intl.NumberFormat().format(num);
  }

  navigateToSupport() {
    this.router.navigate(['/support']);
  }

  navigateToHistory() {
    this.router.navigate(['/history']);
  }

  logout() {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#f44336',
      cancelButtonColor: '#757575'
    }).then((result) => {
      if (result.isConfirmed) {
        localStorage.removeItem('token');
        localStorage.removeItem('username');
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
