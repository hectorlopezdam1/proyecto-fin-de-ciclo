import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { HttpClient, HttpHeaders, HttpRequest, HttpEvent } from '@angular/common/http';
import { map, tap, catchError } from 'rxjs/operators';
import swal from 'sweetalert2';

import { Animal } from './animal';
import { Tipo } from './tipo';


@Injectable({ //representa una clase de tipo servicio
  providedIn: 'root'
})
export class AnimalService {

  private urlEndPoint: string = "http://localhost:8080/app/animals"; //conexion con el backend
  private httpHeaders = new HttpHeaders({'Content-Type' : 'application/json'});

  id_tipo: number;

  constructor(private http: HttpClient, private router: Router) { }

  getAnimals(page: number): Observable<any> { //el Observable hace que cunado se produce un cambio en els servidor se actualice aquí en tiempo real
    //return of (ANIMALS); // el operador 'of' convierte los objetos animales en un observable (flujo / stream)
    return this.http.get(this.urlEndPoint + '/page/' + page) //devuelve los animales de la bd mediante una peticion get al server
    .pipe( //pipe() permite agregar mas operadores
      tap( (response: any) => {
        (response.content as Animal[]).forEach(animal => {
          console.log(animal.nombre)
        })
      }),
      map(  //map() castea un json al tipo del objeto
      (response: any) => {
        (response.content as Animal[]).map(
          animal => {
            return animal;
          });
          return response;
      }) //convierte la respuesta (json, any) a un array de animales
    );
  }

  getTipos(){
    return this.http.get<Tipo[]>(this.urlEndPoint + '/tipos');
  }

  getIdTipo(id_tipo: number){
    this.id_tipo = id_tipo;
  }

  getAnimalsByTipo(page: number): Observable<any> { //el Observable hace que cunado se produce un cambio en els servidor se actualice aquí en tiempo real
    return this.http.get(this.urlEndPoint + '/' + this.id_tipo + '/page/' + page) //devuelve los animales de la bd mediante una peticion get al server
    .pipe( //pipe() permite agregar mas operadores
      tap( (response: any) => {
        (response.content as Animal[]).forEach(animal => {
          console.log(animal.nombre)
        })
      }),
      map(  //map() castea un json al tipo del objeto
      (response: any) => {
        (response.content as Animal[]).map(
          animal => {
            return animal;
          });
          return response;
      }) //convierte la respuesta (json, any) a un array de animales
    );
  }

  create(animal: Animal): Observable<Animal>{
    return this.http.post<Animal>(this.urlEndPoint, animal, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  getAnimal(id: number): Observable<Animal>{
    return this.http.get<Animal>(`${this.urlEndPoint}/${id}`).pipe(
      catchError(e => {
        this.router.navigate(['/']);
        console.error(e.error.mensaje);
        swal.fire('Error al editar', e.error.mensaje, 'error');
        return throwError(e);
      })
    );
  }

  update(animal: Animal): Observable<Animal>{
    return this.http.put<Animal>(`${this.urlEndPoint}/${animal.id}`, animal, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  delete(id: number): Observable<Animal>{
    return this.http.delete<Animal>(`${this.urlEndPoint}/${id}`, {headers: this.httpHeaders}).pipe(
      catchError(e => {
        console.error(e.error.mensaje);
        swal.fire(e.error.mensaje, e.error.error, 'error');
        return throwError(e);
      })
    );
  }

  uploadImg(archivo: File, id): Observable<HttpEvent<{}>>{
    let formData = new FormData();
    formData.append("archivo", archivo);
    formData.append("id", id);

    const req = new HttpRequest('POST', `${this.urlEndPoint}/upload`, formData, { // para implementar la barra de progreso es necesario forma dinámica con httpevent
      reportProgress: true
    });
    return this.http.request(req);
  }
}
