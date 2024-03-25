import { Component, OnInit } from '@angular/core';
import {Contacto} from './centro';
import {ContactosService } from './centro.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactoComponent} from './formulario-centro/formulario-centro'

@Component({
  selector: 'app-centro-list',
  templateUrl: './listacentro.component.html',
  styleUrls: ['./listacentro.component.css']
})
export class CentroList implements OnInit {
onSearch($event: Event) {
throw new Error('Method not implemented.');
}

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
    this.contactoElegido = undefined; // Establece el contactoElegido como null para cerrar los detalles
  }


  //añade centro
  aniadirContacto(): void {
    let ref = this.modalService.open(FormularioContactoComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.contacto = {id: 0, nombre: '', apellidos: '', email: '', telefono: ''};
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
    this.contactoElegido = undefined;
  }
  
  
//Editar cada fila:

  editarContacto(contacto: Contacto): void {
    let ref = this.modalService.open(FormularioContactoComponent);
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