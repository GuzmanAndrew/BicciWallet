import { NgFor, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

  accountDetails = {
    id: '123',
    accountNumber: '4288',
    balance: 1576000
  };

  transactions = [
    {
      id: '1',
      name: 'Fernando Ruiz',
      date: 'Diciembre 30',
      amount: 10397000,
      status: 'completed'
    }
  ];

  transferLimit = 7500;
  transferLimitPercentage = 33; // Porcentaje del límite utilizado

  constructor(
    private router: Router,
    private accountService: AccountService,
    private transactionService: TransactionService
  ) { }

  ngOnInit() {
    this.loadAccountDetails();
    this.loadTransactions();
  }

  loadAccountDetails() {
    // En un entorno real, obtendrías estos datos del backend
    // Por ahora, usamos datos de prueba
    /*
    this.accountService.getAccountDetails('123').subscribe({
      next: (account) => {
        this.accountDetails = account;
      },
      error: (error) => {
        console.error('Error al cargar detalles de la cuenta', error);
      }
    });
    */
  }

  loadTransactions() {
    // En un entorno real, cargarías las transacciones desde el backend
    // Por ahora, usamos datos de prueba
    /*
    this.transactionService.getTransactionHistory(this.accountDetails.id).subscribe({
      next: (transactions) => {
        this.transactions = transactions;
      },
      error: (error) => {
        console.error('Error al cargar transacciones', error);
      }
    });
    */
  }

  navigateToTransfer() {
    this.router.navigate(['/transfer']);
  }

  formatNumber(num: number): string {
    return new Intl.NumberFormat().format(num);
  }
}
