import { Injectable } from '@angular/core';
import {Centro} from './centro';

@Injectable({
  providedIn: 'root'
})

export class CentrosService {
  private centros: Centro [] = [
        {id: 1, nombre: "Lorax",direccion: "huelva",gerente:"pepa",idUsuario: 34526},
        {id: 2, nombre: "MegaGym",direccion: "malaga",gerente:"lola", idUsuario: 34525},
        {id: 3, nombre: "Semperclara",direccion: "Castilla Leon",gerente: "juan", idUsuario: 34522},
        {id: 4, nombre: "chupi panzis",direccion: "Gotham",gerente: "juan",idUsuario: 34522},
        {id: 5, nombre: "campygym",direccion: "Verdansk",gerente: "juan", idUsuario: 34522}
  ];

  constructor() { }

  getCentros(): Centro [] {
    return this.centros;
  }
  // Devuelve los centros asociados a un usuario a partir de la id de dicho usuario
  getCentrosUsuario(id: number): Centro [] {
    
    // Tendrá que recorrer la lista de centros y 

    // Definir una lista vacía
    let listaResultado: Centro[] = [];

    // Usar un bucle for para agregar elementos a la lista
    for (let i = 0; i < this.centros.length; i++) {
      // Comparamos la id que se nos pasa como parámetro con la id del usuario asignado al centro
      if (id == this.centros[i].idUsuario) listaResultado.push(this.centros[i]);
    }
    return listaResultado ;
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
