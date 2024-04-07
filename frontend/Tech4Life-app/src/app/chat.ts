import { Mensaje } from "./mensaje";

export class Chat {
    id : number;
    nombreLocutor: string;
    ultimoMensaje: string;
    horaUltimoMensaje: string;
  
    constructor(id: number, nombreLocutor: string, mensaje:Mensaje) {
      this.id = id;
      this.nombreLocutor = nombreLocutor;
      this.ultimoMensaje = mensaje.contenido;
      this.horaUltimoMensaje = mensaje.fechaHora.getHours().toString() + ":" + mensaje.fechaHora.getMinutes().toString();
    }
  }
  