import { Injectable, PLATFORM_ID, Inject, Optional } from '@angular/core';
import { isPlatformBrowser, isPlatformServer } from '@angular/common';
import { BehaviorSubject, Observable } from 'rxjs';
import { jwtDecode } from 'jwt-decode';
import { REQUEST, RESPONSE } from './ssr.tokens';
import { Request, Response } from 'express';

interface DecodedToken {
   sub: string;
   exp: number;
   iat: number;
}

@Injectable({
   providedIn: 'root'
})
export class AuthService {
   private tokenSubject = new BehaviorSubject<string | null>(null);
   private usernameSubject = new BehaviorSubject<string | null>(null);
   private isBrowser: boolean;
   private isServer: boolean;

   constructor(
      @Inject(PLATFORM_ID) platformId: Object,
      @Optional() @Inject(REQUEST) private request: Request,
      @Optional() @Inject(RESPONSE) private response: Response
   ) {
      this.isBrowser = isPlatformBrowser(platformId);
      this.isServer = isPlatformServer(platformId);
      this.initializeFromStorage();
   }

   private initializeFromStorage(): void {
      if (this.isBrowser) {
         // Cliente: obtener de localStorage
         const token = localStorage.getItem('token');
         const username = localStorage.getItem('username');

         if (token) {
            this.tokenSubject.next(token);
         }

         if (username) {
            this.usernameSubject.next(username);
         } else if (token) {
            try {
               const decoded = jwtDecode<DecodedToken>(token);
               if (decoded.sub) {
                  this.usernameSubject.next(decoded.sub);
                  localStorage.setItem('username', decoded.sub);
               }
            } catch (error) {
               console.error('Error decodificando token:', error);
            }
         }
      } else if (this.isServer && this.request) {
         // Servidor: obtener de cookies o encabezados
         try {
            const cookieHeader = this.request.headers.cookie;

            if (cookieHeader) {
               const cookies = cookieHeader.split(';').reduce((acc, cookie) => {
                  const [key, value] = cookie.trim().split('=');
                  acc[key] = value;
                  return acc;
               }, {} as Record<string, string>);

               if (cookies['token']) {
                  this.tokenSubject.next(cookies['token']);

                  try {
                     const decoded = jwtDecode<DecodedToken>(cookies['token']);
                     if (decoded.sub) {
                        this.usernameSubject.next(decoded.sub);
                     }
                  } catch (error) {
                     console.error('Error decodificando token de cookie:', error);
                  }
               }
            }

            // También verificar si hay un token en los encabezados de autorización
            const authHeader = this.request.headers.authorization;
            if (authHeader && authHeader.startsWith('Bearer ')) {
               const token = authHeader.substring(7);
               this.tokenSubject.next(token);

               try {
                  const decoded = jwtDecode<DecodedToken>(token);
                  if (decoded.sub) {
                     this.usernameSubject.next(decoded.sub);
                  }
               } catch (error) {
                  console.error('Error decodificando token de encabezado:', error);
               }
            }
         } catch (error) {
            console.error('Error al acceder a cookies o encabezados en SSR:', error);
         }
      }
   }

   setToken(token: string): void {
      if (this.isBrowser) {
         localStorage.setItem('token', token);
      } else if (this.isServer && this.response) {
         // En el servidor, configurar una cookie
         try {
            this.response.setHeader('Set-Cookie', `token=${token}; Path=/; HttpOnly; SameSite=Strict`);
         } catch (error) {
            console.error('Error al establecer cookie en SSR:', error);
         }
      }

      this.tokenSubject.next(token);

      // Extraer username del token
      try {
         const decoded = jwtDecode<DecodedToken>(token);
         if (decoded.sub) {
            this.setUsername(decoded.sub);
         }
      } catch (error) {
         console.error('Error decodificando token:', error);
      }
   }

   setUsername(username: string): void {
      if (this.isBrowser) {
         localStorage.setItem('username', username);
      } else if (this.isServer && this.response) {
         // En el servidor, configurar una cookie
         try {
            this.response.setHeader('Set-Cookie', `username=${username}; Path=/; HttpOnly; SameSite=Strict`);
         } catch (error) {
            console.error('Error al establecer cookie en SSR:', error);
         }
      }

      this.usernameSubject.next(username);
   }

   getToken(): string | null {
      return this.tokenSubject.value;
   }

   getUsername(): string | null {
      return this.usernameSubject.value;
   }

   getTokenObservable(): Observable<string | null> {
      return this.tokenSubject.asObservable();
   }

   getUsernameObservable(): Observable<string | null> {
      return this.usernameSubject.asObservable();
   }

   clearAuth(): void {
      if (this.isBrowser) {
         localStorage.removeItem('token');
         localStorage.removeItem('username');
      } else if (this.isServer && this.response) {
         // En el servidor, eliminar cookies
         try {
            this.response.setHeader('Set-Cookie', [
               'token=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; HttpOnly; SameSite=Strict',
               'username=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; HttpOnly; SameSite=Strict'
            ]);
         } catch (error) {
            console.error('Error al eliminar cookies en SSR:', error);
         }
      }

      this.tokenSubject.next(null);
      this.usernameSubject.next(null);
   }

   isLoggedIn(): boolean {
      return !!this.getToken();
   }
}