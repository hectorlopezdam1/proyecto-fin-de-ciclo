import { Component, OnInit, Input } from '@angular/core';
import { TokenService } from '../auth/services/token.service';
import { Router } from '@angular/router';
import swal from 'sweetalert2';
import { AnimalService } from '../animals/animal.service';
import { AnimalsComponent } from '../animals/animals.component';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html'
})
export class MainComponent {
  title: string = 'App Angular';
  isLogged: boolean = false;
  animalsComponent: AnimalsComponent;

  constructor(
    private tokenService: TokenService,
    private router: Router,
    private animalService: AnimalService
  ) { }

  ngOnInit(): void {
    if(this.tokenService.getToken()){
      this.isLogged = true;
    }
  }

  check(id_tipo: number){ //comprueba que ha iniciado sesion para ver la lista de animales, sino, salta un alert
    if(this.isLogged == false){
      swal.fire({
          title: `Debes iniciar sesión.`,
          text: 'Identifícate para ver los animales en adopción.',
          icon: 'info',
          background: '#899dc4',
          confirmButtonColor: '#ff826e'
        });
    }else{
      this.animalService.getIdTipo(id_tipo);
      this.router.navigate(['/animals']);
    }
  }


}
