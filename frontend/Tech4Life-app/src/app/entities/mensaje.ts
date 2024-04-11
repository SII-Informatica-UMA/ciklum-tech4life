import { Destinatario } from "./destinatario";


export interface Mensaje {
  asunto: string;
  destinatarios: [Destinatario] ;
  copia: [Destinatario] ;
  copiaOculta: [Destinatario] ;
  remitente:Destinatario;
  contenido:string ;
  idMensaje:number ;
}
/*
export interface Mensaje{
  id:number;
  remitente: string;
  destinatario: string;
  asunto: string;
  contenido: string;
  fechaHora: Date;
}*/