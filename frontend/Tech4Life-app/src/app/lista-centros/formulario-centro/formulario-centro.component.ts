import { Component } from '@angular/core';
import  {Contacto} from '../contacto';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
//SI DA ERROR EL IMPORT DE ARRIBA EJECUTAR:
//npm install @ng-bootstrap/ng-bootstrap
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formulario-centro',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './formulario-centro.component.html',
  styleUrl: './formulario-centro.component.css'
})
export class FormularioCentroComponent {


  accion?: "Añadir" | "Editar";
  contacto: Contacto = {id: 0, nombre: '', direccion: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarContacto(): void {
    this.modal.close(this.contacto);
  }

}

imports: [
  FormsModule,
  CommonModule
]
