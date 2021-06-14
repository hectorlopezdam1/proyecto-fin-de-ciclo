import { Component, OnInit } from '@angular/core';
import { TokenService } from '../auth/services/token.service';
import { Router } from '@angular/router';
import swal from 'sweetalert2';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.component.html'
})
export class HeaderComponent {

  isLogged: boolean = false;

  constructor(private tokenService: TokenService, private router: Router){

  }

  ngOnInit(): void{
    if(this.tokenService.getToken()){
      this.isLogged = true;
    }else{
      this.isLogged = false;
    }
  }

  onLogOut(): void{

    this.tokenService.logOut();
    swal.fire({
        title: 'Has cerrado sesión.',
        text: `Hasta pronto!`,
        icon: 'success',
        background: '#899dc4',
        confirmButtonColor: "#ff826e",
      }).then((result) => {
        if (result.value) {
            window.location.reload();  //i.e. if 'confirm' is pressed
        }
      });
  }

  check(){ //comprueba que ha iniciado sesion para ver la lista de animales, sino, salta un alert
    if(this.isLogged == false){
      swal.fire({
          title: `Debes iniciar sesión.`,
          text: 'Identifícate dar en adopción.',
          icon: 'info',
          background: '#899dc4',
          confirmButtonColor: '#ff826e'
        });
    }else{
      this.router.navigate(['/form']);
    }
  }


  scrollAdopt(){
    window.scrollTo(0, 480);
  }

  scrollAbout(){
    window.scrollTo(0, 1180);
  }

  scrollNews(){
    window.scrollTo(0, 1505);
  }

  scrollInicio(){
    window.scrollTo(0, 0);
  }



}
