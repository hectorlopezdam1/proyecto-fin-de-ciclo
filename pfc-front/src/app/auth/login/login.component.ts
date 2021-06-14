import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { TokenService } from '../services/token.service';
import { Router } from '@angular/router';
import { LoginUsuario } from '../models/login-usuario';
import swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html'
})
export class LoginComponent implements OnInit {

  isLogged: boolean = false;
  loginFail: boolean = false;
  loginUsuario: LoginUsuario;
  nombreUsuario: string;
  password: string;
  roles: string[] = [];
  errorMsg: string;

  constructor(
    private tokenService: TokenService,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    if(this.tokenService.getToken()){
      this.isLogged = true;
      this.loginFail = false;
      this.roles = this.tokenService.getAuthorities();
    }
  }

  onLogin(): void{
    this.loginUsuario = new LoginUsuario(this.nombreUsuario, this.password);
    this.authService.login(this.loginUsuario).subscribe(
      data => {
        this.isLogged = true;
        this.loginFail = false;

        this.tokenService.setToken(data.token);
        this.tokenService.setUserName(data.nombreUsuario);
        this.tokenService.setAuthorities(data.authorities);
        this.roles = data.authorities;
        this.router.navigate(['/'])
          swal.fire({
              title: `Bienvenido ${this.nombreUsuario}!`,
              text: 'Has iniciado sesión.',
              icon: 'success',
              background: '#899dc4',
              confirmButtonColor: '#ff826e'
            }).then((result) => {
              if (result.value) {
                  window.location.reload();  //i.e. if 'confirm' is pressed
              }
            });

      },
      err => {
        this.isLogged = false;
        this.loginFail = true;
        this.errorMsg = err.error.mensaje;
        console.log(this.errorMsg);
        swal.fire({
            title: `Error`,
            text: 'Nombre de usuario o contraseña incorrectos.',
            icon: 'error',
            background: '#899dc4',
            confirmButtonColor: '#ff826e'
          })
      }
    );
  }

}
