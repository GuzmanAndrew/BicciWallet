import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private apiUrl = 'http://192.168.1.10:8082';

  constructor(private http: HttpClient) { }

  createAccount(userId: string): Observable<any> {
    // Para pruebas locales
    return of({
      id: Math.floor(Math.random() * 10000),
      userId: userId,
      accountNumber: `4288${Math.floor(Math.random() * 10000000)}`,
      balance: 1576000
    });

    // Cuando conectemos el backend:
    // return this.http.post(`${this.apiUrl}/ms-accounts/create`, { userId });
  }

  getAccountDetails(accountId: string): Observable<any> {
    // Para pruebas locales
    return of({
      id: accountId,
      accountNumber: '4288',
      balance: 1576000
    });

    // Cuando conectemos el backend:
    // return this.http.get(`${this.apiUrl}/ms-accounts/find?accountId=${accountId}`);
  }
}
