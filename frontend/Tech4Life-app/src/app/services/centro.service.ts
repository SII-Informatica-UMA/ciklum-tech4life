import { Injectable } from '@angular/core';
import {Centro} from '../entities/centro';

@Injectable({
  providedIn: 'root'
})

export class CentrosService {
  private centros: Centro [] = [
        {idCentro: 1, nombre: "Lorax",direccion: "huelva"},
        {idCentro: 2, nombre: "MegaGym",direccion: "malaga"},
        {idCentro: 3, nombre: "Semperclara",direccion: "Castilla Leon"},
        {idCentro: 4, nombre: "chupi panzis",direccion: "Gotham"},
        {idCentro: 5, nombre: "campygym",direccion: "Verdansk"}
  ];

  constructor() { }

  getCentros(): Centro [] {
    return this.centros;
  }
  

  addCentro(centro: Centro) {
    centro.idCentro = Math.max(...this.centros.map(c => c.idCentro)) + 1;
    this.centros.push(centro);
  }

  editarCentro(centro: Centro) {
    let indice = this.centros.findIndex(c => c.idCentro == centro.idCentro);
    this.centros[indice] = centro;
  }

  eliminarCentro(id: number) {
    let indice = this.centros.findIndex(c => c.idCentro == id);
    this.centros.splice(indice, 1);
  }
}
