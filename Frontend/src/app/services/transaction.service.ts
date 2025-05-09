import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private apiUrl = 'http://192.168.1.10:8081'; // Cambiar por la URL real del backend

  constructor(private http: HttpClient) { }

  getTransactionHistory(accountId: string): Observable<any[]> {
    // Para pruebas locales
    return of([
      {
        id: '1',
        name: 'Fernando Ruiz',
        date: 'Diciembre 30',
        amount: 10397000
      }
    ]);

    // Cuando conectemos el backend:
    // return this.http.get<any[]>(`${this.apiUrl}/ms-transactions/history?accountId=${accountId}`);
  }

  sendTransaction(data: any): Observable<any> {
    // Para pruebas locales
    return of({
      id: Math.floor(Math.random() * 10000),
      ...data,
      status: 'completed'
    });

    // Cuando conectemos el backend:
    // return this.http.post(`${this.apiUrl}/ms-transactions/send`, data);
  }
}
