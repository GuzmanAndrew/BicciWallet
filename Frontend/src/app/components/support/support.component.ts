import { NgFor } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import Swal from 'sweetalert2';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [NgFor, RouterModule],
  templateUrl: './support.component.html',
  styleUrl: './support.component.scss'
})
export class SupportComponent implements OnInit {

  username: string = '';

  faqItems = [
    {
      question: '¿Cómo actualizo mis datos personales?',
      answer: 'Para actualizar tus datos personales, dirígete a la sección "Perfil" desde el menú lateral. Allí encontrarás opciones para modificar tu información personal.'
    },
    {
      question: '¿Cuál es el límite de transferencia diario?',
      answer: 'El límite estándar de transferencia diario es de $7.500.000. Este límite puede ser aumentado solicitándolo a través de la sección de soporte.'
    },
    {
      question: '¿Cuánto tiempo tardan las transferencias?',
      answer: 'Las transferencias entre cuentas de Web Wallet son instantáneas. Las transferencias a otros bancos pueden tomar entre 24-48 horas hábiles.'
    },
    {
      question: '¿Qué hacer si olvidé mi contraseña?',
      answer: 'En la pantalla de inicio de sesión, selecciona la opción "Olvidé mi contraseña". Se te enviará un enlace a tu correo electrónico registrado para establecer una nueva contraseña.'
    }
  ];

  constructor(
    private router: Router,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit() {
    this.username = this.authService.getUsername() || 'Usuario';
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

  navigateToHome() {
    this.router.navigate(['/home']);
  }

}
