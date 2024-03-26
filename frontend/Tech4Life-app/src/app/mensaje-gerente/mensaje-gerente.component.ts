import { Component, OnInit } from '@angular/core';
import { Mensaje } from '../mensaje';


@Component({
  selector: 'app-mensaje-gerente',
  standalone: true,
  templateUrl: './mensaje-gerente.component.html',
  styleUrls: ['./mensaje-gerente.component.css']
})
export class MensajeGerenteComponent implements OnInit {
  mensajes: Mensaje[] = []; // Lista de mensajes

  constructor() { }

  ngOnInit(): void {
    // ...
  }
  enviarMensaje(texto: string, destinatario: string) {
    let nuevoMensaje: Mensaje;
    nuevoMensaje = {
      id: 4, // Incrementar el ID en base a los mensajes existentes
      remitente: "Gerente",
      destinatario: destinatario,
      contenido: texto,
      fechaHora: new Date(),
    };
  
  // Función para enviar un mensaje
  
    // Crear un nuevo objeto de mensaje
    
    // Agregar el nuevo mensaje a la lista de mensajes
    this.mensajes.push(nuevoMensaje);

    // Actualizar la interfaz de usuario para mostrar el nuevo mensaje
    // ...

    // Desplazar la vista automáticamente al nuevo mensaje
    // ...

    // Mostrar una notificación al usuario
    // ...
  }
}
