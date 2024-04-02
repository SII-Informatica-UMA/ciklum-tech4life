import { Component, OnInit } from '@angular/core';
import {Contacto} from './contacto';
import {ContactosService } from './contacto.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
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
    contactoAEliminar?: Contacto;
    contactos: Contacto [] = [];
    contactoElegido?: Contacto;
    term: string = ''; // término de búsqueda
    constructor(private contactosService: ContactosService, private modalService: NgbModal) { }
  
    //actualiza en tiempo real
    ngOnInit(): void {
      this.contactos = this.contactosService.getContactos();
      this.OrdenarPorNombre();
    }
    
  
    //elige contacto
    elegirContacto(contacto: Contacto): void {
      this.contactoElegido = contacto;
    }
  
  
    //cierra detalles
    cerrarDetalles() {
      this.contactoElegido = undefined; 
      this.contactoAEliminar;// Establece el contactoElegido como null para cerrar los detalles
    }
  
  
    //añade centro
    aniadirContacto(): void {
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.contacto = {id: 0, nombre: '', direccion: ''};
      ref.result.then((contacto: Contacto) => {
        this.contactosService.addContacto(contacto);
        this.contactos = this.contactosService.getContactos();
        this.OrdenarPorNombre();
      }, (reason) => {});
      
    }
  
    //edita centro
    contactoEditado(contacto: Contacto): void {
      this.contactosService.editarContacto(contacto);
      this.contactos = this.contactosService.getContactos();
      this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
     
    }
  
    //Elimina centro
    eliminarContacto(id: number): void {
      this.contactosService.eliminarContacto(id);
      this.contactos = this.contactosService.getContactos();
      this.contactoAEliminar = undefined;
    }
    mostrarConfirmacion(contacto: any) {
      this.contactoAEliminar = contacto; // Guarda el contacto a eliminar
    }
    
  //Editar cada fila:
  
    editarContacto(contacto: Contacto): void {
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {}; 
      ref.result.then((contactoEditado: Contacto) => {
        // Actualizar los datos del contacto editado en la lista de contactos
        this.contactosService.editarContacto(contactoEditado)
        this.OrdenarPorNombre();
      }, (reason) => { });
    }
  
  //ordena los centros por nombres
    OrdenarPorNombre(): void {
      this.contactos = this.contactosService.getContactos().sort((a, b) => a.nombre.localeCompare(b.nombre));
    }
  
     // Barra de búsqueda
     searchContact() {
      if (!this.term.trim()) {
        this.contactos = this.contactosService.getContactos();
      } else {
        this.contactos = this.contactosService.getContactos().filter(contacto =>
          contacto.nombre.toLowerCase().includes(this.term.toLowerCase())
        );
      }
    }
  //limpia la busqueda , devuelve a estado inicial
    clearSearch() {
      this.term = '';
      this.contactos = this.contactosService.getContactos();
    }
  //cuando se pulsa enter se activa el buscar centro
    onEnter(event: KeyboardEvent) {
      if (event.key === "Enter") {
        this.searchContact();
      }
    }
  
  }