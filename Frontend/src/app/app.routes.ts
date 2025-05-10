import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { HomeComponent } from './components/home/home.component';
import { TransferComponent } from './components/transfer/transfer.component';
import { UserNotFoundComponent } from './components/user-not-found/user-not-found.component';
import { RegisterUserComponent } from './components/register-user/register-user.component';
import { RegisterAccountComponent } from './components/register-account/register-account.component';
import { SupportComponent } from './components/support/support.component';
import { HistoryComponent } from './components/history/history.component';
import { ProfileComponent } from './components/profile/profile.component';

export const routes: Routes = [
   { path: '', redirectTo: 'login', pathMatch: 'full' },
   { path: 'login', component: LoginComponent },
   { path: 'home', component: HomeComponent },
   { path: 'transfer', component: TransferComponent },
   { path: 'user-not-found', component: UserNotFoundComponent },
   { path: 'register-user', component: RegisterUserComponent },
   { path: 'register-account', component: RegisterAccountComponent },
   { path: 'support', component: SupportComponent },
   { path: 'history', component: HistoryComponent },
   { path: 'profile', component: ProfileComponent }
];