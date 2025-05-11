import { Injectable, Inject, PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';
import { BehaviorSubject } from 'rxjs';

@Injectable({
   providedIn: 'root'
})
export class RegistrationStateService {
   private registeredUserSubject = new BehaviorSubject<any>(null);
   private isBrowser: boolean;

   constructor(@Inject(PLATFORM_ID) platformId: Object) {
      this.isBrowser = isPlatformBrowser(platformId);
      this.initializeFromStorage();
   }

   private initializeFromStorage(): void {
      if (this.isBrowser) {
         const userData = localStorage.getItem('registeredUser');
         if (userData) {
            try {
               this.registeredUserSubject.next(JSON.parse(userData));
            } catch (error) {
               console.error('Error parsing user data:', error);
            }
         }
      }
   }

   setRegisteredUser(userData: any): void {
      this.registeredUserSubject.next(userData);

      if (this.isBrowser && userData) {
         localStorage.setItem('registeredUser', JSON.stringify(userData));
      }
   }

   getRegisteredUser(): any {
      return this.registeredUserSubject.value;
   }

   clearRegisteredUser(): void {
      this.registeredUserSubject.next(null);

      if (this.isBrowser) {
         localStorage.removeItem('registeredUser');
      }
   }
}