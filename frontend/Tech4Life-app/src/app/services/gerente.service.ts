import { Injectable } from '@angular/core';
import {Gerente} from '../entities/gerente'
import { Centro } from '../entities/centro';
import { ContactosService } from './usuario.service';
import { CentrosService } from './centro.service';
@Injectable({
  providedIn: 'root'
})

export class GerentesService {
  private gerentes: Gerente [] = [
    {
      idUsuario: 1,
      empresa: "Semperclara",
      id: 1
    },
    {
        idUsuario: 2,
        empresa: "dfsdv",
        id: 2
    },
    {
        idUsuario: 3,
        empresa: "dsfdsdfs",
        id: 3
    },
    {
        idUsuario: 4,
        empresa: "Semperclara",
        id: 4
    },
    {
        idUsuario:5,
        empresa: "Semperclara",
        id: 5
    }
  ];

  constructor() { }

  //metodo que devuelve el gerente con el id pasado
  getGerente(id:number) {
    for(let i in this.gerentes){
        if(this.gerentes[i].id==id){
            return this.gerentes[i];
        }
      }
}


  addContacto(gerente: Gerente) {
    gerente.id = Math.max(...this.gerentes.map(c => c.id)) + 1;
    this.gerentes.push(gerente);
  }

  editarGerente(gerente: Gerente) {
    let indice = this.gerentes.findIndex(c => c.id == gerente.id);
    this.gerentes[indice] = gerente;
  }

  eliminarGerente(id: number) {
    let indice = this.gerentes.findIndex(c => c.id == id);
    this.gerentes.splice(indice, 1);
  }

}
