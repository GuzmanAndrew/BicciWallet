import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TransactionService } from '../../services/transaction.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-history',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './history.component.html',
  styleUrl: './history.component.scss'
})
export class HistoryComponent {

  username: string = '';
  transactions: any[] = [];

  currentPage: number = 0;
  pageSize: number = 8;
  totalTransactions: number = 0;
  totalPages: number = 0;

  isLoading: boolean = false;

  constructor(
    private router: Router,
    private transactionService: TransactionService
  ) { }

  ngOnInit(): void {
    this.username = localStorage.getItem('username') || 'Usuario';
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.isLoading = true;
    this.transactionService.getTransactionHistoryPaginated(this.currentPage, this.pageSize)
      .subscribe({
        next: (response: any) => {
          // Asumiendo que la respuesta contiene los datos y la información de paginación
          // Ajusta esto según la estructura real de tu API
          this.transactions = response.content || response;
          this.totalTransactions = response.totalElements || this.transactions.length;
          this.totalPages = response.totalPages || Math.ceil(this.totalTransactions / this.pageSize);

          // Formatear las transacciones
          this.formatTransactions();
          this.isLoading = false;
        },
        error: (error) => {
          console.error('Error al cargar las transacciones:', error);
          this.isLoading = false;
        }
      });
  }

  formatTransactions(): void {
    const currentUser = localStorage.getItem('username');
    this.transactions = this.transactions.map(tx => {
      const isIncoming = tx.receiverUsername === currentUser;
      const amount = isIncoming ? Math.abs(tx.amount) : -Math.abs(tx.amount);

      return {
        ...tx,
        formattedDate: this.formatDate(tx.timestamp),
        counterpartyName: isIncoming ? tx.senderUsername : tx.receiverUsername,
        amount: amount,
        formattedAmount: this.formatAmount(amount),
        type: isIncoming ? 'entrada' : 'salida'
      };
    });
  }

  formatDate(timestamp: string): string {
    const date = new Date(timestamp);
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  formatAmount(amount: number): string {
    return new Intl.NumberFormat('es-CO', {
      style: 'currency',
      currency: 'COP',
      maximumFractionDigits: 0
    }).format(Math.abs(amount));
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadTransactions();
    }
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadTransactions();
    }
  }

  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.loadTransactions();
    }
  }

  get pages(): number[] {
    const pagesArray = [];
    const maxVisiblePages = 5;

    if (this.totalPages <= maxVisiblePages) {
      for (let i = 0; i < this.totalPages; i++) {
        pagesArray.push(i);
      }
    } else {
      let startPage = Math.max(0, this.currentPage - Math.floor(maxVisiblePages / 2));
      let endPage = Math.min(this.totalPages - 1, startPage + maxVisiblePages - 1);

      if (endPage - startPage < maxVisiblePages - 1) {
        startPage = Math.max(0, endPage - maxVisiblePages + 1);
      }

      for (let i = startPage; i <= endPage; i++) {
        pagesArray.push(i);
      }
    }

    return pagesArray;
  }

  navigateToHome(): void {
    this.router.navigate(['/home']);
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
