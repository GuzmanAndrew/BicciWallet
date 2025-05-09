import { DatePipe, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';

@Component({
  selector: 'app-transfer',
  standalone: true,
  imports: [NgIf, DatePipe, FormsModule],
  templateUrl: './transfer.component.html',
  styleUrl: './transfer.component.scss'
})
export class TransferComponent {

  transferData = {
    amount: '',
    recipientId: '75632',
    description: ''
  };

  accountBalance = '1,576,000';
  currentDate = new Date();
  showSuccessDialog = false;

  constructor(
    private router: Router,
    private transactionService: TransactionService
  ) { }

  ngOnInit() {
    // Cargar datos del destinatario predeterminado y datos de cuenta
  }

  navigateToHome() {
    this.router.navigate(['/home']);
  }

  makeTransfer() {
    if (!this.transferData.amount) {
      alert('Por favor ingresa un monto para la transferencia.');
      return;
    }

    // Aquí se realizaría la llamada real al servicio de transferencia
    /*
    this.transactionService.sendTransaction({
      sourceAccountId: 'account123',
      destinationAccountId: this.transferData.recipientId,
      amount: parseFloat(this.transferData.amount.replace(/,/g, '')),
      description: this.transferData.description
    }).subscribe({
      next: (result) => {
        this.showSuccessDialog = true;
      },
      error: (error) => {
        console.error('Error en la transferencia', error);
        alert('Error al procesar la transferencia. Por favor intenta nuevamente.');
      }
    });
    */

    // Por ahora, simplemente mostramos el diálogo de éxito
    this.showSuccessDialog = true;
  }

  closeSuccessDialog() {
    this.showSuccessDialog = false;
    this.navigateToHome();
  }

  generateTransactionId() {
    // Generar un ID aleatorio para la transferencia
    return Math.floor(100000 + Math.random() * 900000);
  }
}
