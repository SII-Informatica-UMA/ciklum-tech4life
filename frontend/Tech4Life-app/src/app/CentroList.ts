import { Component, OnInit } from '@angular/core';
import { Centro } from './Centro';
import { CentrosService } from './centros.service';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component';


@Component({
  selector: 'app-root',
  templateUrl: './CentroList.html',
  styleUrls: ['./CentroList.css'],
  standalone: true
  })
export class CentroList implements OnInit{
  centros: Centro[]= [];
  centroElegido?: Centro;
  modalService: any;
  constructor(private centrosService: CentrosService) { }
  ngOnInit(): void {
    this.centros = this.centrosService.getCentros();
    this.OrdenarPorNombre();
  }


  elegirCentro(centro:Centro):void{
    this.centroElegido = centro;
  }

  addCentro(){
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.contacto = {id: 0, nombre: '', direccion: ''};
    ref.result.then((centro: Centro) => {
      this.centrosService.addCentro(centro);
      this.centros = this.centrosService.getCentros();
      this.OrdenarPorNombre();
    }, () => {});
  }

  centroEditado(centro: Centro): void {
    this.centrosService.editarCentro(centro);
    this.centros = this.centrosService.getCentros();
    this.centroElegido = this.centros.find(c => c.id == centro.id);
   
  }

  eliminarCentro(id: number): void {
    this.centrosService.eliminarCentro(id);
    this.centros = this.centrosService.getCentros();
    this.centroElegido = undefined;
  }


  editarCentro(centro: Centro): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.contacto = {}; 
    ref.result.then((centroEditado: Centro) => {
      // Actualizar los datos del contacto editado en la lista de contactos
      this.centrosService.editarCentro(centroEditado)
      this.OrdenarPorNombre();
    }, () => { });
  }


  OrdenarPorNombre(): void {
    this.centros = this.centrosService.getCentros().sort((a, b) => a.nombre.localeCompare(b.nombre));
  }

}