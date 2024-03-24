import { Component, OnInit } from '@angular/core';
import {Contacto} from './contacto';
import {ContactosService } from './contactos.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactoComponent} from './formulario-contacto/formulario-contacto.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
onSearch($event: Event) {
throw new Error('Method not implemented.');
}

  contactos: Contacto [] = [];
  contactoElegido?: Contacto;
  term: string = ''; // término de búsqueda
  constructor(private contactosService: ContactosService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.contactos = this.contactosService.getContactos();
    this.OrdenarPorNombre();
  }
  
  elegirContacto(contacto: Contacto): void {
    this.contactoElegido = contacto;
  }
  cerrarDetalles() {
    this.contactoElegido = undefined; // Establece el contactoElegido como null para cerrar los detalles
  }
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
  contactoEditado(contacto: Contacto): void {
    this.contactosService.editarContacto(contacto);
    this.contactos = this.contactosService.getContactos();
    this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
   
  }

  eliminarContacto(id: number): void {
    this.contactosService.eliminarContacto(id);
    this.contactos = this.contactosService.getContactos();
    this.contactoElegido = undefined;
  }
  //----------------------FUNCIONES AGREGADAS:--------------------------
  //EDITAR CONTACTOS:

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
  //se muestra todo ordenado:

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

  clearSearch() {
    this.term = '';
    this.contactos = this.contactosService.getContactos();
  }

  onEnter(event: KeyboardEvent) {
    if (event.key === "Enter") {
      this.searchContact();
    }
  }

}