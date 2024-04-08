import { Component, OnInit } from '@angular/core';
import { Centro } from '../entities/centro';
import { CentrosService } from '../services/centro.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DetallesCentroComponent } from '../detalles-centro/detalles-centro.component';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';

@Component({
    selector: 'app-lista-centros',
    standalone: true,
    templateUrl: './lista-centros.component.html',
    styleUrl: './lista-centros.component.css',
    imports: [DetallesCentroComponent,FormsModule,CommonModule]
})
export class ListaCentrosComponent implements OnInit {

  onSearch($event: Event) {
  throw new Error('Method not implemented.');
  }
    contactoAEliminar?: Centro;
    contactos:Centro [] = [];
    contactoElegido?: Centro;
    term: string = ''; // término de búsqueda
    constructor(private contactosService: CentrosService, private modalService: NgbModal) { }
  
    //actualiza en tiempo real
    ngOnInit(): void {
      this.contactos = this.contactosService.getCentros();
      this.OrdenarPorNombre();
    }
    
    //cerrar paneles cuando se abre otro:
    cerrarPaneles(): void {
       this.contactoElegido = undefined;
       this.contactoAEliminar = undefined;
    }
    //elige contacto
    elegirContacto(contacto: Centro): void {
      this.cerrarPaneles();
      this.contactoElegido = contacto;
      
    }
  
  
    //cierra detalles
    cerrarDetalles() {
      this.contactoElegido = undefined; 
      this.contactoAEliminar= undefined;// Establece el contactoElegido como null para cerrar los detalles
    }
  
  
    //añade centro
    aniadirContacto(): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.contacto = {id: 0, nombre: '', direccion: ''};
      ref.result.then((contacto: Centro) => {
        this.contactosService.addCentro(contacto);
        this.contactos = this.contactosService.getCentros();
        this.OrdenarPorNombre();
      }, (reason) => {});
      
    }
  
    //edita centro
    contactoEditado(contacto: Centro): void {
     
      this.contactosService.editarCentro(contacto);
      this.contactos = this.contactosService.getCentros();
      this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
     
    }
  
    //Elimina centro
    eliminarContacto(id: number): void {
      this.contactosService.eliminarCentro(id);
      this.contactos = this.contactosService.getCentros();
      this.contactoAEliminar = undefined;
    }
    mostrarConfirmacion(contacto: any) {
      this.cerrarPaneles();
      this.contactoAEliminar = contacto; // Guarda el contacto a eliminar
     
    }
    
  //Editar cada fila:
  
    editarContacto(contacto: Centro): void {
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {}; 
      ref.result.then((contactoEditado: Centro) => {
        // Actualizar los datos del contacto editado en la lista de contactos
        this.contactosService.editarCentro(contactoEditado)
        this.OrdenarPorNombre();
      }, (reason) => { });
    }
  
  //ordena los centros por nombres
    OrdenarPorNombre(): void {
      this.contactos = this.contactosService.getCentros().sort((a, b) => a.nombre.localeCompare(b.nombre));
    }
  
     // Barra de búsqueda
     searchContact() {
      this.cerrarPaneles()
      if (!this.term.trim()) {
        this.contactos = this.contactosService.getCentros();
      } else {
        this.contactos = this.contactosService.getCentros().filter(contacto =>
          contacto.nombre.toLowerCase().includes(this.term.toLowerCase())
        );
      }
    }
  //limpia la busqueda , devuelve a estado inicial
    clearSearch() {

      this.term = '';
      this.contactos = this.contactosService.getCentros();
    }
  //cuando se pulsa enter se activa el buscar centro
    onEnter(event: KeyboardEvent) {
      this.cerrarPaneles();
      if (event.key === "Enter") {
        this.searchContact();
      }
    }
  
  }