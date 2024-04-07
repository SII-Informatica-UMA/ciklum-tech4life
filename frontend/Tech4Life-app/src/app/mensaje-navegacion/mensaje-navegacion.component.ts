import { Component, NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Chat } from "../chat";
import { Mensaje, mensajes } from "../mensaje"; // Import the interface


@Component({
  selector: 'app-mensaje-navegacion',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './mensaje-navegacion.component.html',
  styleUrls: ['./mensaje-navegacion.component.css']
})
export class MensajeNavegacionComponent {
   chats: Chat[] = [
    new Chat("Ana", mensajes[0]),
    new Chat("Juan", mensajes[1]),
    new Chat("María", mensajes[2]),
  ];
  accion?: "Añadir" | "Editar";
}

