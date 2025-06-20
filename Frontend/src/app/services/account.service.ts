import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AccountService {

  private apiUrl = environment.apiAccounts;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  createAccount(username: string): Observable<any> {
    const params = { username };
    return this.http.post(`${this.apiUrl}/accounts/create`, {}, { params });
  }

  getAccountDetailsSecure(): Observable<any> {
    const token = this.authService.getToken();
    return this.http.get(`${this.apiUrl}/accounts/v2/balance`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    });
  }

}
