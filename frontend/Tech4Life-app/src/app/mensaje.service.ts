import { Injectable } from '@angular/core';
import { Mensaje } from './mensaje';
import { Usuario } from './usuario';
import { ContactosService } from './usuario.service';

@Injectable({
  providedIn: 'root'
})
export class MensajeService {
  private mensajes: Mensaje[] = [
    {
      id: 1,
      remitente: "Ana",
      destinatario: "Juan",
      asunto: "Saludo",
      contenido: "Hola, ¿cómo estás?",
      fechaHora: new Date(),
    },
    {
      id: 2,
      remitente: "Juan",
      destinatario: "Ana",
      asunto: "Saludo",
      contenido: "Muy bien, gracias. ¿Y tú?",
      fechaHora: new Date(),
    },
    {
      id: 3,
      remitente: "Ana",
      destinatario: "Juan",
      asunto: "Saludo",
      contenido: "Estoy bien, gracias. ¿Qué tal el día?",
      fechaHora: new Date(),
    },
    {
      id: 4,
      remitente: "Juan",
      destinatario: "Ana",
      asunto: "Solicitud de información",
      contenido: "Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?Hola Ana, ¿podrías proporcionarme más información sobre el proyecto?",
      fechaHora: new Date(),
    },
    {
      id: 5,
      remitente: "María",
      destinatario: "Juan",
      asunto: "Confirmación de reunión",
      contenido: "Hola Juan, confirmo nuestra reunión para mañana a las 10:00 a.m.",
      fechaHora: new Date(),
    },
    {
      id: 6,
      remitente: "Juan",
      destinatario: "Pedro",
      asunto: "Solicitud de presupuesto",
      contenido: "Hola Pedro, ¿podrías enviarme el presupuesto para el proyecto?",
      fechaHora: new Date(),
    },
    {
      id: 7,
      remitente: "Elena",
      destinatario: "Juan",
      asunto: "Invitación a evento",
      contenido: "Hola Juan, te invito al evento de lanzamiento de nuestro nuevo producto.",
      fechaHora: new Date(),
    },
    {
      id: 8,
      remitente: "Juan",
      destinatario: "Carlos",
      asunto: "Solicitud de reunión",
      contenido: "Hola Carlos, ¿podemos reunirnos para discutir el proyecto?",
      fechaHora: new Date(),
    },
    {
      id: 9,
      remitente: "Pedro",
      destinatario: "Juan",
      asunto: "Respuesta al presupuesto",
      contenido: "Hola Juan, adjunto encontrarás el presupuesto solicitado.",
      fechaHora: new Date(),
    },
    {
      id: 10,
      remitente: "Juan",
      destinatario: "Sandra",
      asunto: "Feliz Cumpleaños",
      contenido: "¡Hola Sandra! ¡Feliz cumpleaños! Que tengas un día maravilloso.",
      fechaHora: new Date(),
    }
];
  constructor() { }

  getMensajes(): Mensaje [] {
    return this.mensajes;
  }

  getMensajesEntrada(mensajes: Mensaje[], nombreUsuario : string): Mensaje[]{ //es el usuario quien recibe el mensaje
    return mensajes.filter(mensaje => mensaje.destinatario === nombreUsuario);
  }
  getMensajesSalida(mensajes: Mensaje[], nombreUsuario : string): Mensaje[]{   //es el usuario quien envia el mensaje
    return mensajes.filter(mensaje => mensaje.remitente === nombreUsuario);
  }

  getAsunto(mensaje:Mensaje){
    return mensaje.asunto;
  }

  redactar(mensaje: Mensaje) {
    mensaje.id = Math.max(...this.mensajes.map(c => c.id)) + 1;
    this.mensajes.push(mensaje);
  }

  eliminarMensaje(id: number) {
    let indice = this.mensajes.findIndex(c => c.id == id);
    this.mensajes.splice(indice, 1);
  }
}
