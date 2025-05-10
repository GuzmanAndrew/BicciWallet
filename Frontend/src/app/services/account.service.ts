import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private apiUrl = '/api/accounts';

  constructor(private http: HttpClient) { }

  createAccount(username: string): Observable<any> {
    const params = { username };
    return this.http.post(`${this.apiUrl}/accounts/create`, {}, { params });
  }

  getAccountDetailsSecure(): Observable<any> {
    const token = localStorage.getItem('token');
    return this.http.get(`${this.apiUrl}/accounts/v2/balance`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

}
