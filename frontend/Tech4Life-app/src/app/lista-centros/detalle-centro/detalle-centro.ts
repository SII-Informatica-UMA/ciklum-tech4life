import { Component, Input, Output, EventEmitter } from '@angular/core';
import {Contacto } from '../centro';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactoComponent} from '../formulario-centro/formulario-centro'
import { ContactosService } from '../centro.service';

@Component({
  selector: 'app-detalle-centro',
  templateUrl: './detalle-centro.html',
  styleUrls: ['./detalle-centro.css']
})
export class DetalleCentroComponent {
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
    let ref = this.modalService.open(FormularioContactoComponent);
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
