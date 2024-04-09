export interface Mensaje{
  id:number;
  remitente: string;
  destinatario: string;
  asunto: string;
  contenido: string;
  fechaHora: Date;
}