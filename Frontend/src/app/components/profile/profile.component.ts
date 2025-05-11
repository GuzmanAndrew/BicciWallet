import { CommonModule, isPlatformBrowser } from '@angular/common';
import { Component, Inject, OnInit, PLATFORM_ID } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit {

  username: string = '';
  profileForm: FormGroup;
  isLoading: boolean = false;
  showPassword: boolean = false;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    private router: Router,
    private userService: UserService,
    private authService: AuthService
  ) {
    this.profileForm = this.fb.group({
      username: [{ value: '', disabled: true }],
      name: [''],
      email: ['', [Validators.email]],
      password: ['']
    });
  }

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.username = this.authService.getUsername() || 'Usuario';
      this.loadUserProfile();
    }
  }

  loadUserProfile(): void {
    this.isLoading = true;
    this.userService.getUserProfile().subscribe({
      next: (userData) => {
        this.profileForm.patchValue({
          username: userData.username,
          name: userData.name || '',
          email: userData.email || ''
        });
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al cargar datos del usuario:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudieron cargar los datos del perfil',
          icon: 'error',
          confirmButtonText: 'Entendido'
        });
        this.isLoading = false;
      }
    });
  }

  updateProfile(): void {
    if (this.profileForm.invalid) {
      Swal.fire({
        title: 'Formulario inválido',
        text: 'Por favor revisa los campos del formulario',
        icon: 'warning',
        confirmButtonText: 'Entendido'
      });
      return;
    }

    const userData = {
      username: this.username,
      name: this.profileForm.get('name')?.value,
      email: this.profileForm.get('email')?.value,
      password: this.profileForm.get('password')?.value || ''
    };

    this.isLoading = true;
    this.userService.updateUserProfile(userData).subscribe({
      next: (response) => {
        Swal.fire({
          title: 'Perfil actualizado',
          text: 'Tus datos han sido actualizados correctamente',
          icon: 'success',
          confirmButtonText: 'Excelente'
        });
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error al actualizar perfil:', error);
        Swal.fire({
          title: 'Error',
          text: 'No se pudo actualizar el perfil. Intenta nuevamente.',
          icon: 'error',
          confirmButtonText: 'Entendido'
        });
        this.isLoading = false;
      }
    });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  navigateToHome(): void {
    this.router.navigate(['/home']);
  }

  logout() {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar tu sesión?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar',
      confirmButtonColor: '#f44336',
      cancelButtonColor: '#757575'
    }).then((result) => {
      if (result.isConfirmed) {
        this.userService.logout();
        this.router.navigate(['/']).then(() => {
          Swal.fire({
            title: 'Sesión cerrada',
            text: 'Has cerrado sesión correctamente',
            icon: 'success',
            timer: 2000,
            showConfirmButton: false
          });
        });
      }
    });
  }

}
