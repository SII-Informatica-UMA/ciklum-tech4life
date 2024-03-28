import { Injectable } from '@angular/core';
import {Contacto } from './contacto';

@Injectable({
  providedIn: 'root'
})

export class ContactosService {
  private contactos: Contacto [] = [
    {id: 1, nombre: "pepe",apellido: "huelva",centro: "dvfvgd"},
    {id: 2, nombre: "laura",apellido: "dede",centro: "dvfgdvd"},
    {id: 3, nombre: "pepa",apellido: "reree",centro: "dvfgdvf"},
    {id: 4, nombre: "rayman",apellido: "acegsfvdfh",centro: "sdgvdfdv"},
    {id: 5, nombre: "salchicha",apellido: "dfgdv",centro: "asacfd"}
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
