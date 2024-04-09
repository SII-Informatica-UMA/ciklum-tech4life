import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Mensaje } from '../entities/mensaje';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-formulario-mensaje',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './formulario-mensaje.component.html',
  styleUrl: './formulario-mensaje.component.css'
})
export class FormularioMensajeComponent {
  mensaje: Mensaje = {
    id: 0, remitente: '', destinatario: '', asunto: '', contenido: '', fechaHora: new Date()
  };
 

  constructor(public modal: NgbActiveModal) { }

  enviarMensaje(): void {
    this.modal.close(this.mensaje);
  }
}
