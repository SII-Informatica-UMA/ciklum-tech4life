import { Component, OnInit } from '@angular/core';
import {Contacto} from './contacto';
import {ContactosService } from './contacto.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { DetallesGerenteComponent } from '../detalles-gerente/detalles-gerente.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-lista-gerentes',
  standalone: true,
  imports: [DetallesGerenteComponent,FormsModule,CommonModule],
  templateUrl: './lista-gerentes.component.html',
  styleUrl: './lista-gerentes.component.css'
})

export class ListaGerentesComponent implements OnInit {
  onSearch($event: Event) {
  throw new Error('Method not implemented.');
  }
  
    contactos: Contacto [] = [];
    contactoElegido?: Contacto;
    contactoAEliminar?: Contacto;
    term: string = ''; // término de búsqueda
    constructor(private contactosService: ContactosService, private modalService: NgbModal) { }
  
    //actualiza en tiempo real
    ngOnInit(): void {
      this.contactos = this.contactosService.getContactos();
      this.OrdenarPorNombre();
    }
    
    //cerrar paneles cuando se abre otro:
    cerrarPaneles(): void {
      this.contactoElegido = undefined;
      this.contactoAEliminar = undefined;
    }
    //elige contacto
    elegirContacto(contacto: Contacto): void {
      this.cerrarPaneles();
      this.contactoElegido = contacto;
      
    }
  
  
    //cierra detalles
    cerrarDetalles() {
      this.contactoElegido = undefined;
      this.contactoAEliminar= undefined; // Establece el contactoElegido como null para cerrar los detalles
    }
  
  
    //añade gerente
    aniadirContacto(): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioGerenteComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.contacto = {id: 0, nombre: '', apellido: ''};
      ref.result.then((contacto: Contacto) => {
        this.contactosService.addContacto(contacto);
        this.contactos = this.contactosService.getContactos();
        this.OrdenarPorNombre();
      }, (reason) => {});
      
    }
  
    //edita gerente
    contactoEditado(contacto: Contacto): void {
      this.contactosService.editarContacto(contacto);
      this.contactos = this.contactosService.getContactos();
      this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
     
    }
  
    //Elimina gerente
    eliminarContacto(id: number): void {
      this.contactosService.eliminarContacto(id);
      this.contactos = this.contactosService.getContactos();
      this.contactoAEliminar = undefined;
    }
    
    mostrarConfirmacion(contacto: any) {
      this.cerrarPaneles();
      this.contactoAEliminar = contacto; // Guarda el contacto a eliminar
     
    }
  //Editar cada fila:
  
    editarContacto(contacto: Contacto): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioGerenteComponent);
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
      this.cerrarPaneles();
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
  //cuando se pulsa enter se activa el buscar gerente
    onEnter(event: KeyboardEvent) {
      this.cerrarPaneles();
      if (event.key === "Enter") {
        this.searchContact();
      }
    }
  
  }