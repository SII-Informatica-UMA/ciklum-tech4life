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
        {idCentro: 1, nombre: "Lorax",direccion: "huelva"},
        {idCentro: 2, nombre: "MegaGym",direccion: "malaga"},
        {idCentro: 3, nombre: "Semperclara",direccion: "Castilla Leon"},
        {idCentro: 4, nombre: "chupi panzis",direccion: "Gotham"},
        {idCentro: 5, nombre: "campygym",direccion: "Verdansk"}
  ];

  constructor(private backend: BackendService) { }

  /*getCentros(): Centro [] {
    return this.centros;
  }*/
  // Devuelve los centros asociados a un usuario a partir de la id de dicho usuario
  getCentrosUsuario(gerente: Gerente): Observable<Centro[]>{
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
