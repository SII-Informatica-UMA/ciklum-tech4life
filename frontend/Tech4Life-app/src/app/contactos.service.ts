import { Injectable } from '@angular/core';
import {Contacto } from './contacto';

@Injectable({
  providedIn: 'root'
})

export class ContactosService {
  private contactos: Contacto [] = [
    {id: 1, nombre: "Lorax",direccion: "huelva"},
        {id: 2, nombre: "MegaGym",direccion: "malaga"},
        {id: 3, nombre: "Semperclara",direccion: "Castilla Leon"},
        {id: 4, nombre: "chupi panzis",direccion: "Gotham"},
        {id: 5, nombre: "campygym",direccion: "Verdansk"}
  ];

  constructor() { }

  getContactos(): Contacto [] {
    return this.contactos;
  }

  addContacto(contacto: Contacto) {
    contacto.id = Math.max(...this.contactos.map(c => c.id)) + 1;
    this.contactos.push(contacto);
  }

  editarContacto(contacto: Contacto) {
    let indice = this.contactos.findIndex(c => c.id == contacto.id);
    this.contactos[indice] = contacto;
  }

  eliminarContacto(id: number) {
    let indice = this.contactos.findIndex(c => c.id == id);
    this.contactos.splice(indice, 1);
  }
}
