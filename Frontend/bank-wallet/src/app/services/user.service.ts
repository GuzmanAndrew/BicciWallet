import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://192.168.1.10:8081';

  constructor(private http: HttpClient) { }

  login(username: string, password: string): Observable<any> {
    // Para pruebas locales sin backend
    if (username === 'admin' && password === 'admin') {
      const mockResponse = { token: 'mock-jwt-token' };
      localStorage.setItem('token', mockResponse.token);
      localStorage.setItem('username', username);
      return of(mockResponse);
    }

    // Comentado hasta que tengamos el backend conectado
    // return this.http.post(`${this.apiUrl}/users/login?username=${username}&password=${password}`, {})
    //   .pipe(
    //     tap(response => {
    //       localStorage.setItem('token', response.token);
    //       localStorage.setItem('username', username);
    //     }),
    //     catchError(error => throwError(() => error))
    //   );

    // Simular un error para cualquier otro usuario
    return throwError(() => new Error('Usuario no encontrado'));
  }

  register(userData: any): Observable<any> {
    // Para pruebas locales
    return of({ id: 1, ...userData });

    // Cuando conectemos el backend:
    // return this.http.post(`${this.apiUrl}/users/register`, userData);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }
}
