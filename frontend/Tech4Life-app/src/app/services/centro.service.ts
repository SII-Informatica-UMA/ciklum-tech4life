import { Injectable } from '@angular/core';
import {Centro} from '../entities/centro';
import { Gerente } from '../entities/gerente';
import { BackendService } from './backend.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class CentrosService {
  private centros: Centro [] = [
        {id: 1, nombre: "Lorax",direccion: "huelva",gerente:"pepa",idUsuario: 34526},
        {id: 2, nombre: "MegaGym",direccion: "malaga",gerente:"juan", idUsuario: 34525},
        {id: 3, nombre: "Semperclara",direccion: "Castilla Leon",gerente: "Lola", idUsuario: 34522},
        {id: 4, nombre: "chupi panzis",direccion: "Gotham",gerente: "Lola",idUsuario: 34522},
        {id: 5, nombre: "campygym",direccion: "Verdansk",gerente: "Lola", idUsuario: 34522}
  ];

  constructor(private backend: BackendService) { }

  /*getCentros(): Centro [] {
    return this.centros;
  }*/
  // Devuelve los centros asociados a un usuario a partir de la id de dicho usuario
  getCentrosUsuario(gerente: Gerente): Observable<Centro []>{
    return this.backend.getCentroDelGerente(gerente);
  }

  addCentro(centro: Centro) {
    return this.backend.postCentro(centro);
  }

  editarCentro(centro: Centro) {
   return this.backend.putCentro(centro);
  }

  eliminarCentro(id: number) {
    return this.backend.deleteCentro(id);
  }
  getCentro(){
    return this.backend.getCentro();
  }
}
