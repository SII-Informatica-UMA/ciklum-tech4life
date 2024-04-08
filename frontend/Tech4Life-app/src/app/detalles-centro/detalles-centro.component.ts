import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Centro } from '../entities/centro';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';
import { CentrosService } from '../services/centro.service';

@Component({
  selector: 'app-detalles-centro',
  standalone: true,
  imports: [],
  templateUrl: './detalles-centro.component.html',
  styleUrl: './detalles-centro.component.css'
})
export class DetallesCentroComponent {
  mostrarDetalles: boolean = true;
  CerrarDetalles():void {
    this.mostrarDetalles = false;
  }
    @Input() contacto?: Centro;
    @Output() contactoEditado = new EventEmitter<Centro>();
    @Output() contactoEliminado = new EventEmitter<number>();
    @Output() cerrarDetalles = new EventEmitter<void>();
    constructor(private contactosService: CentrosService, private modalService: NgbModal) { }
    cerrar() {
      this.cerrarDetalles.emit();
    }
    editarcontacto(): void {
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {...this.contacto};
      ref.result.then((contacto: Centro) => {
        this.contactoEditado.emit(contacto);
      }, (reason) => {});
    }
  
    eliminarContacto(): void {
      this.contactoEliminado.emit(this.contacto?.id);
    }
  }
  