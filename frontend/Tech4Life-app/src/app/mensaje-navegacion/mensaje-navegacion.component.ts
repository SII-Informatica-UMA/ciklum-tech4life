import { Component, EventEmitter, NgModule, Output } from '@angular/core';
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
/*selectChat(selected : Chat) {
  let indice = this.chats.findIndex(c => c.id == selected.id); // Access the id of each Chat object
  this.chats[indice] = selected;
}
*/
  selectedChat: string | undefined;

  @Output() chatSelected = new EventEmitter<number>();

  onChatSelected(chat: Chat) {
    this.selectedChat = chat.nombreLocutor;
    this.chatSelected.emit(chat.id);
  }


   chats: Chat[] = [
    new Chat(1,"Ana", mensajes[0]),
    new Chat(2,"Juan", mensajes[1]),
    new Chat(3,"María", mensajes[2]),
  ];
  
}

