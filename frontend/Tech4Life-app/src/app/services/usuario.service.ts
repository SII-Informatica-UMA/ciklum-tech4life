import { Injectable } from '@angular/core';
import { Usuario } from '../entities/usuario';
import { Centro } from '../entities/centro';
import { Gerente } from '../entities/gerente';

@Injectable({
  providedIn: 'root'
})

export class ContactosService {
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

  private gerentes: Gerente [] = [
    {
      id: 5, nombre: "salchicha", apellido1: "mcdonals",
      apellido2: 'rosa',
      email: '',
      password: '',
      administrador: false,
      idUsuario: 0,
      empresa: 'Semperclara'
    },
    {
      id: 2, nombre: "laura", apellido1: "dede",
      apellido2: 'risky',
      email: '',
      password: '',
      administrador: false,
      idUsuario: 0,
      empresa: 'patataland'
    },
    {
      id: 3, nombre: "pepa", apellido1: "reree",
      apellido2: 'rosa',
      email: '',
      password: '',
      administrador: false,
      idUsuario: 0,
      empresa: 'gotham'
    },
    {
      id: 4, nombre: "rayman", apellido1: "acegsfvdfh",
      apellido2: 'pepita',
      email: '',
      password: '',
      administrador: false,
      idUsuario: 0,
      empresa: 'Starwars'
    },
    {
      id: 5, nombre: "salchicha", apellido1: "dfgdv",
      apellido2: 'mckey',
      email: '',
      password: '',
      administrador: false,
      idUsuario: 0,
      empresa: 'Foap'
    }
  ];
  constructor() { }

  getContactos(): Usuario [] {
    return this.contactos;
  }

  getGerentes(): Gerente [] {
    return this.gerentes;
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
  addGerente(contacto: Gerente) {
    contacto.id = Math.max(...this.gerentes.map(c => c.id)) + 1;
    this.gerentes.push(contacto);
  }

  editarContacto(contacto: Usuario) {
    let indice = this.contactos.findIndex(c => c.id == contacto.id);
    this.contactos[indice] = contacto;
  }
  editarGerente(contacto: Gerente) {
    let indice = this.gerentes.findIndex(c => c.id == contacto.id);
    this.gerentes[indice] = contacto;
  }

  eliminarContacto(id: number) {
    let indice = this.contactos.findIndex(c => c.id == id);
    this.contactos.splice(indice, 1);
  }

  eliminarGerente(id: number) {
    let indice = this.gerentes.findIndex(c => c.id == id);
    this.gerentes.splice(indice, 1);
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
