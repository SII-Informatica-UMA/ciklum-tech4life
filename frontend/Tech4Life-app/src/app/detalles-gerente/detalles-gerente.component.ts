import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Usuario } from '../entities/usuario';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { UsuariosService } from '../services/usuario.service';
import { Gerente } from '../entities/gerente';

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
    @Input() contacto?: Usuario;
    @Input() gerente?: Gerente;
    @Output() contactoEditado = new EventEmitter<Usuario>();
    @Output() gerenteEditado = new EventEmitter<Gerente>();
    @Output() contactoEliminado = new EventEmitter<number>();
    @Output() cerrarDetalles = new EventEmitter<void>();
    constructor(private usuariosService: UsuariosService, private modalService: NgbModal) { }
    cerrar() {
      this.cerrarDetalles.emit();
    }
    editarcontacto(): void {
      let ref = this.modalService.open(FormularioGerenteComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {...this.contacto};
      ref.result.then((contacto: Usuario) => {
        this.contactoEditado.emit(contacto);
      }, (reason) => {});

      ref.componentInstance.contacto = {...this.gerente};
      ref.result.then((gerente: Gerente) => {
        this.gerenteEditado.emit(gerente);
      }, (reason) => {});
    }
  
    eliminarContacto(): void {
      this.contactoEliminado.emit(this.contacto?.id);
    }
  }
  