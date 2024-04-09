import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { MensajeService } from '../services/mensaje.service';
@Component({
  selector: 'app-detalle-mensaje',
  standalone: true,
  imports: [],
  templateUrl: './detalle-mensaje.component.html',
  styleUrl: './detalle-mensaje.component.css'
})
export class DetalleMensajeComponent {
  @Input() mensaje?: Mensaje;
  @Output() mensajeEliminado = new EventEmitter<number>();

  constructor (private mensajeService : MensajeService){}
  eliminarMensaje(): void {
    this.mensajeEliminado.emit(this.mensaje?.id);
  }

}
