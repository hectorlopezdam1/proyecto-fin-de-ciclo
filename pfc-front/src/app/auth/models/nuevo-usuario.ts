export class NuevoUsuario {

  nombre: string;
  nombreUsuario: string;
  password: string;
  email: string;

  constructor(nombre: string, nombreUsuario: string, password: string, email: string) {
    this.nombre = nombre;
    this.nombreUsuario = nombreUsuario;
    this.password = password;
    this.email = email;
  }

}
