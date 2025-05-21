import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {

  private apiUrl = environment.apiTransactions;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  getTransactionHistory(): Observable<any[]> {
    const token = this.authService.getToken();

    return this.http.get<any[]>(`${this.apiUrl}/transactions/history`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

  getTransactionHistoryPaginated(page: number, size: number): Observable<any> {
    const token = this.authService.getToken();

    return this.http.get<any>(`${this.apiUrl}/transactions/history/all`, {
      headers: {
        Authorization: `Bearer ${token}`
      },
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }

  sendTransaction(data: {
    receiverUsername: string;
    amount: number;
  }): Observable<any> {
    const token = this.authService.getToken() || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    const params = {
      receiverUsername: data.receiverUsername,
      amount: data.amount.toString()
    };

    return this.http.post(`${this.apiUrl}/transactions/transfer`, {}, { headers, params });
  }

}
