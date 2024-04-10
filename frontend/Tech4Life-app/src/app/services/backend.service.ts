import { Injectable } from "@angular/core";
import { Observable, map, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { Gerente } from "../entities/gerente";
import { Centro } from "../entities/centro";
import { HttpClient } from "@angular/common/http";
import { BACKEND_URI } from "../config/config";
import { Mensaje } from "../entities/mensaje";
import { JwtResponse } from "../entities/login";

// Este servicio usa el backend real

@Injectable({
    providedIn: 'root'
})
export class BackendService {

    constructor(private httpClient: HttpClient) {}

    getUsuarios(): Observable<Usuario[]> {
        return this.httpClient.get<Usuario[]>(BACKEND_URI + '/usuario');
    }
    
    postUsuario(usuario: Usuario): Observable<Usuario> {
        return this.httpClient.post<Usuario>(BACKEND_URI + '/usuario', usuario);
    }
    
    putUsuario(usuario: Usuario): Observable<Usuario> {
        return this.httpClient.put<Usuario>(BACKEND_URI + '/usuario/' + usuario.id, usuario);
    }
    
    deleteUsuario(id: number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/usuario/' + id);
    }
    
    getUsuario(id: number): Observable<Usuario> {
        return this.httpClient.get<Usuario>(BACKEND_URI + '/usuario/' + id);
    }
    
    login(email: string, password: string): Observable<string> {
        return this.httpClient.post<JwtResponse>(BACKEND_URI + '/login', {email: email, password: password})
        .pipe(map(jwtResponse => jwtResponse.jwt));
    }
    
    forgottenPassword(email: string): Observable<void> {
        return this.httpClient.post<void>(BACKEND_URI + '/forgottenpassword', {email: email});
    }
    
    resetPassword(token: string, password: string): Observable<void> {
        return this.httpClient.post<void>(BACKEND_URI + '/passwordreset', {token: token, password: password});
    }
    
    getGerente(id:number): Observable<Gerente> {
        return this.httpClient.get<Gerente>(BACKEND_URI + '/gerente/' + id) ;
    }

    putGerente(gerente: Gerente): Observable<Gerente> {
        return this.httpClient.put<Gerente>(BACKEND_URI + '/gerente/' + gerente.id, gerente);
    }

    deleteGerente(id: number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/gerente/' + id);
    }

    getCentro(): Observable<[Centro]> {
        return this.httpClient.get<[Centro]>(BACKEND_URI + '/centro' );
    }


    putCentro(centro: Centro): Observable<Centro> {
        return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro.idCentro, centro);
    }

    deleteCentro(id: number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/centro/' + id);
    }

    putGerenteaCentro(id:number, centro:Centro): Observable<Centro> {
        return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro + '/gerente' , {id: id}) ;
    }

    deleteGerenteCentro(gerente:Gerente, centro:Centro): Observable<void>{
        return this.httpClient.delete<void>(BACKEND_URI + '/centro/' + centro.idCentro + '/gerente') ;
    }

    getMensajeCentro(centro:Centro): Observable<[Mensaje]>{
        return this.httpClient.get<[Mensaje]>(BACKEND_URI + '/mensaje/centro/' + centro.idCentro) ;
    }

    postMensajeCentro(centro:Centro, mensaje:Mensaje): Observable<Mensaje>{
        return this.httpClient.post<Mensaje>(BACKEND_URI + '/mensaje/centro/', {centro:centro , mensaje:mensaje}) ;
    }

    getGerentes(): Observable<[Gerente]>{
        return this.httpClient.get<[Gerente]>(BACKEND_URI + '/gerente') ;
    }

    postGerente(gerente:Gerente): Observable<Gerente> {
        return this.httpClient.post<Gerente>(BACKEND_URI + '/gerente/', gerente) ;
    }

    getCentroDelGerente(gerente:Gerente): Observable<Centro> {
        return this.httpClient.get<Centro>(BACKEND_URI + '/centro/' + gerente);
    }

    postCentro(centro:Centro): Observable<Centro> {
        return this.httpClient.post<Centro>(BACKEND_URI + '/centro', centro) ;
    }

    getMensaje(idMensaje:number): Observable<Mensaje> {
        return this.httpClient.get<Mensaje>(BACKEND_URI + '/mensaje/centro/' + idMensaje) ;
    }

    deleteMensaje(idMensaje:number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/mensaje/centro/' + idMensaje) ;
    }
}