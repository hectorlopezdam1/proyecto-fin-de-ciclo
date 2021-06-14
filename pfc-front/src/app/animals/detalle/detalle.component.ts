import { Component, OnInit, Input } from '@angular/core';
import { Animal } from "../animal";
import { AnimalService } from "../animal.service";
import swal from 'sweetalert2';
import { HttpEventType } from '@angular/common/http';
import { ModalService } from './modal.service';

@Component({
  selector: 'detalle-animal',
  templateUrl: './detalle.component.html',
  styleUrls: ['./detalle.component.css']
})
export class DetalleComponent implements OnInit {

  @Input() animal: Animal;

  titulo: string = "Detalle del animal";
  selectedImage: File;
  progress: number = 0;

  constructor(
    private animalService: AnimalService,
    public modalService: ModalService
  ) { }

  ngOnInit(): void {
    //this.activatedRoute.paramMap.subscribe( params => {
      //let id: number = +params.get('id');
      //if(id){
        //this.animalService.getAnimal(id).subscribe(animal => {
          //this.animal = animal;
        //});
      //}
    //});
  }

  selectImage(event){
    this.selectedImage = event.target.files[0]; //selecciona el primer archivo del array (sólo hay uno, índice 0)
    this.progress = 0;
    console.log(this.selectedImage);
    if(this.selectedImage.type.indexOf('image') < 0){ // indexOf sin no encuentra imagen, retorna -1
      swal.fire({
        title: "Error al seleccionar la imagen",
        background: '#899dc4',
        text: `El archivo debe ser del tipo jpg, png, jpeg o gif.`,
        icon: 'error',
        confirmButtonText: 'Vale',
        confirmButtonColor: '#ff826e'
      });
      this.selectedImage = null;
    }
  }

  uploadImage(){
    if(!this.selectedImage){
      swal.fire({
        title: "Error en la subida",
        background: '#899dc4',
        text: `Debe seleccionar una foto.`,
        icon: 'error',
        confirmButtonText: 'Vale',
        confirmButtonColor: '#ff826e'
      });
    }else{
      this.animalService.uploadImg(this.selectedImage, this.animal.id)
      .subscribe( event => {
          if(event.type === HttpEventType.UploadProgress){
            this.progress = Math.round((event.loaded / event.total) * 100) // calcular porcentaje de cargado
          }else if(event.type === HttpEventType.Response){ // cuando termina
            let response: any = event.body;
            this.animal = response.animal as Animal;

            this.modalService.notifyUpload.emit(this.animal);
            swal.fire({
              title: "Subida completada",
              background: '#899dc4',
              text: `Se ha subido la imagen ${this.animal.foto}`,
              icon: 'success',
              confirmButtonText: 'Vale',
              confirmButtonColor: '#ff826e'
            });
          }

        });
    }
  }

  closeModal(){
    this.modalService.closeModal();
    this.selectedImage = null;
    this.progress = 0;
  }

}
