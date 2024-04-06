import { Injectable } from '@angular/core';
import {Centro} from './centro';

@Injectable({
  providedIn: 'root'
})

export class CentrosService {
  private centros: Centro [] = [
    {id: 1, nombre: "Lorax",direccion: "huelva",gerente:"pepa"},
        {id: 2, nombre: "MegaGym",direccion: "malaga",gerente:"lola"},
        {id: 3, nombre: "Semperclara",direccion: "Castilla Leon",gerente: "juan"},
        {id: 4, nombre: "chupi panzis",direccion: "Gotham",gerente: "juan"},
        {id: 5, nombre: "campygym",direccion: "Verdansk",gerente: "juan"}
  ];

  constructor() { }

  getCentros(): Centro [] {
    return this.centros;
  }

  addCentro(centro: Centro) {
    centro.id = Math.max(...this.centros.map(c => c.id)) + 1;
    this.centros.push(centro);
  }

  editarCentro(centro: Centro) {
    let indice = this.centros.findIndex(c => c.id == centro.id);
    this.centros[indice] = centro;
  }

  eliminarCentro(id: number) {
    let indice = this.centros.findIndex(c => c.id == id);
    this.centros.splice(indice, 1);
  }
}
