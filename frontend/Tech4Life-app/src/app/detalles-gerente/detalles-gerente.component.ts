import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Contacto } from '../lista-gerentes/contacto';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { ContactosService } from '../lista-gerentes/contacto.service';

@Component({
  selector: 'app-detalles-gerente',
  standalone: true,
  imports: [],
  templateUrl: './detalles-gerente.component.html',
  styleUrl: './detalles-gerente.component.css'
})
export class DetallesGerenteComponent {
  mostrarDetalles: boolean = true;
  CerrarDetalles():void {
    this.mostrarDetalles = false;
  }
    @Input() contacto?: Contacto;
    @Output() contactoEditado = new EventEmitter<Contacto>();
    @Output() contactoEliminado = new EventEmitter<number>();
    @Output() cerrarDetalles = new EventEmitter<void>();
    constructor(private contactosService: ContactosService, private modalService: NgbModal) { }
    cerrar() {
      this.cerrarDetalles.emit();
    }
    editarcontacto(): void {
      let ref = this.modalService.open(FormularioGerenteComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {...this.contacto};
      ref.result.then((contacto: Contacto) => {
        this.contactoEditado.emit(contacto);
      }, (reason) => {});
    }
  
    eliminarContacto(): void {
      this.contactoEliminado.emit(this.contacto?.id);
    }
  }
  