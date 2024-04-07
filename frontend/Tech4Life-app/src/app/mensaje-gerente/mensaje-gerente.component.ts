import { Component, Input, NgModule} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-mensaje-gerente',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './mensaje-gerente.component.html',
  styleUrls: ['./mensaje-gerente.component.css']
})


export class MensajeGerenteComponent {
  @Input() selectedChatId: number | undefined;
  mensajesEnviados: string[] = [];
  mensajesRecibidos: string[] = [];
  mensajeEnviar: string = '';

  enviarMensaje() {
    if (this.mensajeEnviar.trim() !== '') {
      this.mensajesEnviados.unshift('Yo: ' + this.mensajeEnviar); // Agrega el mensaje al principio del arreglo de mensajes enviados
      this.mensajeEnviar = '';
    }
  }
}
