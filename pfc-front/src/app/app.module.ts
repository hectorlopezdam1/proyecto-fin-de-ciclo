import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { HeaderComponent } from './nav/nav.component';
import { MainComponent } from './main/main.component';
import { FooterComponent } from './footer/footer.component';
import { AnimalsComponent } from './animals/animals.component';
import { AnimalService } from './animals/animal.service';

import { RouterModule, Routes } from '@angular/router';
import { FormComponent } from './animals/form.component';
import { DetalleComponent } from './animals/detalle/detalle.component';
import { FormsModule } from '@angular/forms';
import { LoginComponent } from './auth/login/login.component';
import { RegistroComponent } from './auth/registro/registro.component';
import { PaginatorComponent } from './paginator/paginator.component';

import { ProdGuardService as guard} from './auth/guards/prod-guard.service';

import { interceptorProvider } from './auth/interceptors/prod-interceptor.service';




const routes: Routes = [ //rutas de acceso mediante routerLink

  { path: '', redirectTo: '/main', pathMatch: 'full' },
  { path: 'login', component: LoginComponent }, //no deberia poder si ya estoy loggeado
  { path: 'registro', component: RegistroComponent },
  { path: 'animals', component: AnimalsComponent, canActivate: [guard], data: { expectedRol: ['admin', 'user'] } },
  { path: 'animals/page/:page', component: AnimalsComponent, canActivate: [guard], data: { expectedRol: ['admin', 'user'] } },
  { path: 'main', component: MainComponent },
  { path: 'form', component: FormComponent , canActivate: [guard], data: { expectedRol: ['admin', 'user'] } },
  { path: 'form/:id', component: FormComponent , canActivate: [guard], data: { expectedRol: ['admin'] } }

];

@NgModule({
  declarations: [ //inyeccion de componentes
    AppComponent,
    HeaderComponent,
    MainComponent,
    FooterComponent,
    AnimalsComponent,
    FormComponent,
    LoginComponent,
    RegistroComponent,
    DetalleComponent,
    PaginatorComponent
  ],
  imports: [ //inyeccion de modulos (clases exportadas para las templates de los componentes)
    BrowserModule,
    HttpClientModule, //para conexion con el backend
    FormsModule,
    RouterModule.forRoot(routes) //para utilizar las rutas
  ],
  providers: [
    AnimalService,
    interceptorProvider
    ], //inyeccion de servicios
  bootstrap: [AppComponent]
})
export class AppModule { }
