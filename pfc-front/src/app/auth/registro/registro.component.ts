import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';
import { NuevoUsuario } from '../models/nuevo-usuario';
import swal from 'sweetalert2';

@Component({
  selector: 'app-registro',
  templateUrl: './registro.component.html'
})
export class RegistroComponent implements OnInit {

  isLogged: boolean = false;
  nuevoUsuario: NuevoUsuario;
  nombre: string;
  nombreUsuario: string;
  password: string;
  email: string;
  errorMsg: string;

  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if(this.tokenService.getToken()){
      this.isLogged = true;
    }
  }

  onRegister(): void{
    this.nuevoUsuario = new NuevoUsuario(this.nombre, this.nombreUsuario, this.password, this.email);
    this.authService.nuevo(this.nuevoUsuario).subscribe(
      data => {
        swal.fire({
            title: `Bienvenido ${this.nombreUsuario}!`,
            text: 'Gracias por darte de alta.',
            icon: 'success',
            background: '#899dc4',
            confirmButtonColor: '#ff826e'
          });
          console.log(this.nuevoUsuario);

        this.router.navigate(['/login'])

      },
      err => {
        this.errorMsg = err.error.mensaje;
        console.log(this.errorMsg);
        swal.fire({
            title: 'Error',
            text: this.errorMsg,
            icon: 'error',
            background: '#899dc4',
            confirmButtonColor: '#ff826e'
          });
      }
    );
  }

}
