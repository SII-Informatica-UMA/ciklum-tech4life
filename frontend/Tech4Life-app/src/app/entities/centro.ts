export interface Centro {
    id: number; // id del centro SE DEBERÍA LLAMAR idCentro
    nombre: string;
    direccion: string;

    gerente: string; // ESTE ATRIBUTO NO APARECE EN LA API DEL BACKEND
    idUsuario: number; // id del usuario que es gerente del centro  ESTE ATRIBUTO NO APARECE EN LA API DEL BACKEND
}