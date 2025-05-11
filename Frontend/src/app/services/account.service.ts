import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  //private apiUrl = '/api/accounts';
  private apiUrl = 'http://localhost:8082/api/accounts';

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  createAccount(username: string): Observable<any> {
    const params = { username };
    return this.http.post(`${this.apiUrl}/create`, {}, { params });
  }

  getAccountDetailsSecure(): Observable<any> {
    const token = this.authService.getToken();
    return this.http.get(`${this.apiUrl}/v2/balance`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

}
