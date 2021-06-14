import { Component, OnInit } from '@angular/core';
import { Animal } from './animal';
import { Tipo } from './tipo';
import { AnimalService } from './animal.service';
import { Router, ActivatedRoute } from '@angular/router';
import swal from 'sweetalert2';


@Component({
  selector: 'app-form',
  templateUrl: './form.component.html'
})
export class FormComponent implements OnInit {

  public animal: Animal = new Animal();
  public tipos: Tipo[];

  constructor(
    private animalService: AnimalService,
    private router: Router,
    private activatedRoute: ActivatedRoute) { }

  ngOnInit(): void {
    this.cargarAnimal();
    this.animalService.getTipos().subscribe(tipos => this.tipos = tipos);
  }

  cargarAnimal(): void{
    this.activatedRoute.params.subscribe(
      params => {
        let id = params['id']
        if(id){
          this.animalService.getAnimal(id)
          .subscribe((animal) => this.animal = animal)
        }
      }
    )
  }

  public create(): void{
    this.animalService.create(this.animal).subscribe(
      animal => {
      this.router.navigate(['/main']);
      swal.fire({
        title: "Listo!",
        background: '#899dc4',
        text: `Has dado en adopción a ${this.animal.nombre}`,
        confirmButtonText: "Genial!",
        confirmButtonColor: "#ff826e",
        icon: 'success'
      });
    });
  }

  public update(): void {
    this.animalService.update(this.animal)
    .subscribe(animal => {
      this.router.navigate(['/animals']);
      swal.fire({
        title: "Listo!",
        background: '#899dc4',
        text: `${this.animal.nombre} actualizado!`,
        confirmButtonText: "Vale",
        confirmButtonColor: "#ff826e",
        icon: 'success'
      });
    })
  }

  compareTipos(o1:Tipo, o2:Tipo): boolean {
    if(o1 === undefined && o2 === undefined){
      return true;
    }
    return o1 === null || o2 === null || o1 === undefined || o2 === undefined ? false: o1.id_tipo === o2.id_tipo;
  }

}
