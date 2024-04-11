import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Mensaje } from '../entities/mensaje';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Destinatario } from '../entities/destinatario';

@Component({
  selector: 'app-formulario-mensaje',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './formulario-mensaje.component.html',
  styleUrl: './formulario-mensaje.component.css'
})
export class FormularioMensajeComponent {
  destinatario!:[Destinatario] ;
  remitente!:Destinatario;
  
  constructor(public modal: NgbActiveModal) { }
  mensaje: Mensaje = {
    idMensaje: 0, remitente: this.remitente, destinatarios: this.destinatario, asunto: '', contenido: ''
    ,copia: this.destinatario, copiaOculta: this.destinatario
  };


  

  enviarMensaje(): void {
    this.modal.close(this.mensaje);
  }
}
