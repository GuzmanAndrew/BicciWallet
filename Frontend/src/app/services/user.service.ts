import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, tap } from 'rxjs/operators';
import { AuthService } from './auth.service';
import { environment } from '../../environments/environment.prod';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUsers;

  constructor(
    private http: HttpClient,
    private authService: AuthService
  ) { }

  login(username: string, password: string): Observable<any> {
    const params = new HttpParams()
      .set('username', username)
      .set('password', password);

    return this.http.post<{ token: string }>(`${this.apiUrl}/users/login`, {}, { params }).pipe(
      tap((response) => {
        this.authService.setToken(response.token);
        this.authService.setUsername(username);
      }),
      catchError((error) => throwError(() => error))
    );
  }

  register(userData: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/users/register`, userData);
  }

  getUserProfile(): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    return this.http.get(`${this.apiUrl}/profile`, { headers }).pipe(
      catchError((error) => throwError(() => error))
    );
  }

  updateUserProfile(userData: any): Observable<any> {
    const token = this.authService.getToken();
    const headers = new HttpHeaders()
      .set('Authorization', `Bearer ${token}`)
      .set('Content-Type', 'application/json');

    return this.http.patch(`${this.apiUrl}/users/update`, userData, {
      headers: headers
    }).pipe(
      catchError(error => {
        console.error('Error al actualizar perfil:', error);
        return throwError(() => error);
      })
    );
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout(): void {
    this.authService.clearAuth();
  }
}
