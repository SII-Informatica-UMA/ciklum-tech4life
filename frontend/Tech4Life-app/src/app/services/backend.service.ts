import { Injectable } from "@angular/core";
import { Observable, map, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { Gerente } from "../entities/gerente";
import { Centro } from "../entities/centro";
import { HttpClient } from "@angular/common/http";
import { BACKEND_URI } from "../config/config";
import { Mensaje } from "../entities/mensaje";

@Injectable({
    providedIn: 'root'
})
export class BackendService {

    constructor(private httpClient: HttpClient) {}

    getGerente(id:number): Observable<Gerente> {
        return this.httpClient.get<Gerente>(BACKEND_URI + '/gerente/' + id) ;
    }

    putGerente(gerente: Gerente): Observable<Gerente> {
        return this.httpClient.put<Gerente>(BACKEND_URI + '/gerente/' + gerente.id, gerente);
    }

    deleteGerente(id: number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/gerente/' + id);
    }

    getCentro(gerente:Gerente): Observable<[Centro]> {
        return this.httpClient.get<[Centro]>(BACKEND_URI + '/centro/' + gerente);
    }

    putUsuario(centro: Centro): Observable<Centro> {
        return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro.id, centro);
    }

    deleteCentro(id: number): Observable<void> {
        return this.httpClient.delete<void>(BACKEND_URI + '/centro/' + id);
    }

    putGerenteaCentro(id:number, centro:Centro): Observable<Centro> {
        return this.httpClient.put<Centro>(BACKEND_URI + '/centro/' + centro + '/gerente' , {id: id}) ;
    }

    deleteGerenteCentro(gerente:Gerente, centro:Centro): Observable<void>{
        return this.httpClient.delete<void>(BACKEND_URI + '/centro/' + centro.id + '/gerente') ;
    }

    getMensajeCentro(centro:Centro): Observable<[Mensaje]>{
        return this.httpClient.get<[Mensaje]>(BACKEND_URI + '/mensaje/centro/' + centro.id) ;
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