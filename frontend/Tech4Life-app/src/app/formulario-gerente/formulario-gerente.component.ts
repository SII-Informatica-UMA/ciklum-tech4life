import { Component } from '@angular/core';
import  {Usuario}from '../entities/usuario'
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
//SI DA ERROR EL IMPORT DE ARRIBA EJECUTAR:
//npm install @ng-bootstrap/ng-bootstrap
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Gerente } from '../entities/gerente';

@Component({
  selector: 'app-formulario-gerente',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './formulario-gerente.component.html',
  styleUrl: './formulario-gerente.component.css'
  
})
export class FormularioGerenteComponent {

  accion?: "Añadir" | "Editar";
  contacto: Gerente= {
    id: 0, 
    nombre:'', 
    apellido1: '',
    apellido2: '',
    email: '',
    password: '',
    administrador: false,
    idUsuario: 0,
    empresa: ''
  };

  constructor(public modal: NgbActiveModal) { }

  guardarContacto(): void {
    this.modal.close(this.contacto);
  }

}








