import { Destinatario } from "./destinatario";

export interface Mensaje {
    asunto: string;
    destinatarios: [Destinatario] ;   
    copia: [Destinatario] ;     
    copiaOculta: [Destinatario] ;     
    contenido:string ;
    idMensaje:number ;
}