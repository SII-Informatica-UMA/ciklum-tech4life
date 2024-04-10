import { Injectable } from '@angular/core';
import { Usuario } from '../entities/usuario';
import { Centro } from '../entities/centro';

@Injectable({
  providedIn: 'root'
})

export class UsuariosService {
  private contactos: Usuario [] = [
    {
      id: 1, nombre: "pepe", apellido1: "huelva",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 2, nombre: "laura", apellido1: "dede",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 3, nombre: "pepa", apellido1: "reree",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 4, nombre: "rayman", apellido1: "acegsfvdfh",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 5, nombre: "salchicha", apellido1: "dfgdv",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    }
  ];

  constructor() { }

  getContactos(): Usuario [] {
    return this.contactos;
  }

  // Método que devuelve el id del usuario que se ha loggeado en la aplicación
  getUsuarioLoginID(): number {
    return this.login.id;
  }

  // Método que devuelve el nombre del usuario que se ha loggeado en la aplicación
  getUsuarioLoginNombre(): string {
    return this.login.nombre;
  }

  addContacto(contacto: Usuario) {
    contacto.id = Math.max(...this.contactos.map(c => c.id)) + 1;
    this.contactos.push(contacto);
  }

  editarContacto(contacto: Usuario) {
    let indice = this.contactos.findIndex(c => c.id == contacto.id);
    this.contactos[indice] = contacto;
  }

  eliminarContacto(id: number) {
    let indice = this.contactos.findIndex(c => c.id == id);
    this.contactos.splice(indice, 1);
  }

  private login:Usuario = {
    id: 34522,
    nombre: "Lola",
    apellido1: "Diaz",
    apellido2: "Ruiz",
    email:"correo" ,
    password: "123",
    administrador: true
  }

  getLogin(): Usuario {
    return this.login;
  }

}
