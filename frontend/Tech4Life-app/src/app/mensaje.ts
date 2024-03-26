export interface Mensaje {
    id: number;
    remitente: string;
    destinatario: string;
    contenido: string;
    fechaHora: Date;
  }
  // Array de mensajes
  export const mensajes: Mensaje[] = [
    {
      id: 1,
      remitente: "Ana",
      destinatario: "Juan",
      contenido: "Hola, ¿cómo estás?",
      fechaHora: new Date(),
    },
    {
      id: 2,
      remitente: "Juan",
      destinatario: "Ana",
      contenido: "Muy bien, gracias. ¿Y tú?",
      fechaHora: new Date(),
    },
    {
      id: 3,
      remitente: "Ana",
      destinatario: "Juan",
      contenido: "Estoy bien, gracias. ¿Qué tal el día?",
      fechaHora: new Date(),
    },
  ];
  
  