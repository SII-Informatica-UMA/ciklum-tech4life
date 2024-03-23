import { Injectable } from '@angular/core';
import {Centro } from './Centro';

@Injectable({
    providedIn: 'root'
})

export class CentrosService {
    private centros: Centro[]=[
        {id: 1, nombre: "Lorax",direccion: "huelva"},
        {id: 2, nombre: "MegaGym",direccion: "malaga"},
        {id: 3, nombre: "Semperclara",direccion: "Castilla Leon"},
        {id: 4, nombre: "chupi panzis",direccion: "Gotham"},
        {id: 5, nombre: "campygym",direccion: "Verdansk"}
    ];
    constructor(){ }
        getCentros(): Centro[]{
            return this.centros;
        }

        addCentro(centro: Centro){
            centro.id = Math.max(...this.centros.map(c => c.id)) + 1;
             this.centros.push(centro);
        }

        editarCentro(centro:Centro){
            let indice = this.centros.findIndex(c => c.id == centro.id);
            this.centros[indice] = centro;
        }
        eliminarCentro(id: number) {
            let indice = this.centros.findIndex(c => c.id == id);
            this.centros.splice(indice, 1);
        }

        }
 

  