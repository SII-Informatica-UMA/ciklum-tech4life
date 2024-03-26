import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-mensaje-gerente',
  standalone: true,
  templateUrl: './mensaje-gerente.component.html',
  styleUrls: ['./mensaje-gerente.component.css']
})
export class MensajeGerenteComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
    // Puedes colocar tu lógica aquí, se ejecutará una vez que el componente esté inicializado.
  }

  enviarMensaje(): void {
    const messageInput = document.getElementById('messageInput') as HTMLInputElement;
    const chatMessages = document.getElementById('chatMessages');

    if (chatMessages) { // Verificar si chatMessages no es null
      const messageText = messageInput.value.trim();
      if (messageText !== '') {
        this.appendMessage('You', messageText, chatMessages);
        messageInput.value = '';
      }
    }
  }

  appendMessage(sender: string, message: string, chatMessages: HTMLElement): void {
    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.innerHTML = `<strong>${sender}:</strong> ${message}`;
    chatMessages.appendChild(messageElement);
    chatMessages.scrollTop = chatMessages.scrollHeight;
  }
}




