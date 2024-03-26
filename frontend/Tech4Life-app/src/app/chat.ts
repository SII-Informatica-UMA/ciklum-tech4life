import { Mensaje } from "./mensaje";

export class Chat {
    nombreLocutor: string;
    ultimoMensaje: string;
    horaUltimoMensaje: string;
  
    constructor(nombreLocutor: string, mensaje:Mensaje) {
      this.nombreLocutor = nombreLocutor;
      this.ultimoMensaje = mensaje.contenido;
      this.horaUltimoMensaje = mensaje.fechaHora.getHours().toString() + ":" + mensaje.fechaHora.getMinutes().toString();
    }
  }
  