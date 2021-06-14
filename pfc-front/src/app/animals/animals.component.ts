import { Component, OnInit, Input } from '@angular/core';
import { Animal } from './animal';
import { AnimalService } from './animal.service';
import { TokenService } from '../auth/services/token.service';
import { ModalService } from './detalle/modal.service';

import swal from 'sweetalert2';

import { ActivatedRoute, Router } from "@angular/router";

@Component({
  selector: 'app-animals',
  templateUrl: './animals.component.html'
})
export class AnimalsComponent implements OnInit {

  animals: Animal[];
  isLogged = false;
  roles: string[];
  isAdmin: boolean = false;
  paginador: any;
  id_tipo: number;
  selectedAnimal: Animal;
  categoria: string;

  constructor(
    private animalService: AnimalService,
    private activatedRoute: ActivatedRoute,
    private tokenService: TokenService,
    private modalService: ModalService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.id_tipo = this.animalService.id_tipo;
    this.setCategoria()
    if(this.tokenService.getToken()){
      this.isLogged = true;
      this.activatedRoute.paramMap.subscribe( params => {
      let page: number = +params.get('page');
      if(!page){
        page = 0;
      }
      if(this.id_tipo == undefined || this.id_tipo == null || this.id_tipo == 0){
        this.animalService.getAnimals(page).subscribe(
          response => {
            this.animals = response.content as Animal[];
            this.paginador = response;
          });
      }
      else{
        this.animalService.getAnimalsByTipo(page).subscribe(
          response => {
            this.animals = response.content as Animal[];
            this.paginador = response;
          });
      }

      this.modalService.notifyUpload.subscribe(animal => {
        this.animals = this.animals.map( animalOrig => {
          if(animal.id == animalOrig.id){
            animalOrig.foto = animal.foto;
          }
          return animalOrig;
        });
      });

    });
    }else{
      this.isLogged = false;
    }
    this.roles = this.tokenService.getAuthorities();
    console.log(this.roles);
    this.roles.forEach(rol => {
      if(rol === 'ROLE_ADMIN'){ //privilegios para editar y eliminar animales
        this.isAdmin = true;
      }
    });
  }

  delete(animal: Animal): void {
    swal.fire({
      title: 'Estás seguro?',
      text: `Seguro que quieres eliminar a ${animal.nombre}?`,
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#289b83',
      cancelButtonColor: '#ff826e',
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'No, cancelar',
      background: '#899dc4',
  }).then((result) => {
      if (result.isConfirmed) {
        this.animalService.delete(animal.id).subscribe(
          response =>{
            this.animals = this.animals.filter(a => a !== animal)
            swal.fire({
              title: 'Listo!',
              text: `${animal.nombre} eliminado con éxito!`,
              icon: 'success',
              background: '#899dc4',
              confirmButtonColor: '#ff826e'
            });
          }
        );
      }
    });
  }

  openModal(animal: Animal){
    this.selectedAnimal = animal;
    this.modalService.openModal();
  }

  setCategoria(){
    if(this.id_tipo == 1){
      this.categoria = 'Perros';
    }
    else if(this.id_tipo == 2){
      this.categoria = 'Gatos';
    }
    else if(this.id_tipo == 3){
      this.categoria = 'Roedores';
    }
    else if(this.id_tipo == 4){
      this.categoria = 'Reptiles';
    }
    else if(this.id_tipo == 5){
      this.categoria = 'Aves';
    }else if(this.id_tipo == 0 || this.id_tipo == null || this.id_tipo == undefined){
      this.categoria = 'Todos';
    }
  }


  check(id_tipo: number){ //comprueba que ha iniciado sesion para ver la lista de animales, sino, salta un alert
    if(this.isLogged == false){
      swal.fire({
          title: `Debes iniciar sesión.`,
          text: 'Identifícate para ver los animales en adopción.',
          icon: 'info',
          background: '#899dc4'
        });
    }else{
      this.id_tipo = id_tipo;
      this.setCategoria();
      this.animalService.getIdTipo(id_tipo);
      if(id_tipo == 0){
        this.animalService.getAnimals(0).subscribe( //animals es el resultado del stream y se asigna a this.animals
          response => {
            this.animals = response.content as Animal[];
            this.paginador = response;
          });
      }else{
        this.animalService.getAnimalsByTipo(0).subscribe( //animals es el resultado del stream y se asigna a this.animals
          response => {
            this.animals = response.content as Animal[];
            this.paginador = response;
          });
      }

    }
  }

}
