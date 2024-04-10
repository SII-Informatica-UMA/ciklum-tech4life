/*import { Injectable } from '@angular/core';
import { Usuario } from '../entities/usuario';
import { Centro } from '../entities/centro';

@Injectable({
  providedIn: 'root'
})

export class UsuariosService {
  private contactos: Usuario [] = [
    {
      id: 1, nombre: "pepe", apellido1: "huelva",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 2, nombre: "laura", apellido1: "dede",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 3, nombre: "pepa", apellido1: "reree",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 4, nombre: "rayman", apellido1: "acegsfvdfh",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    },
    {
      id: 5, nombre: "salchicha", apellido1: "dfgdv",
      apellido2: '',
      email: '',
      password: '',
      administrador: false
    }
  ];

  constructor() { }

  getContactos(): Usuario [] {
    return this.contactos;
  }

  // Método que devuelve el id del usuario que se ha loggeado en la aplicación
  getUsuarioLoginID(): number {
    return this.login.id;
  }

  // Método que devuelve el nombre del usuario que se ha loggeado en la aplicación
  getUsuarioLoginNombre(): string {
    return this.login.nombre;
  }

  addContacto(contacto: Usuario) {
    contacto.id = Math.max(...this.contactos.map(c => c.id)) + 1;
    this.contactos.push(contacto);
  }

  editarContacto(contacto: Usuario) {
    let indice = this.contactos.findIndex(c => c.id == contacto.id);
    this.contactos[indice] = contacto;
  }

  eliminarContacto(id: number) {
    let indice = this.contactos.findIndex(c => c.id == id);
    this.contactos.splice(indice, 1);
  }

  private login:Usuario = {
    id: 34522,
    nombre: "Lola",
    apellido1: "Diaz",
    apellido2: "Ruiz",
    email:"correo" ,
    password: "123",
    administrador: true
  }

  getLogin(): Usuario {
    return this.login;
  }

}*/

import { Injectable } from "@angular/core";
import { Login, UsuarioSesion, Rol, RolCentro } from "../entities/login";
import { Observable, of, forkJoin, concatMap, lastValueFrom, throwError } from "rxjs";
import * as jose from 'jose';
import { map, pluck, first, catchError, filter } from 'rxjs/operators';


import { Usuario } from "../entities/usuario";
import { BackendFakeService } from "./backend.fake.service";
import { BackendService } from "./backend.service";
import { usuarios } from "./usuarios.db.service";
import { Gerente } from "../entities/gerente";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  _rolCentro?: RolCentro;

  constructor(private backend: BackendService) { }

  doLogin(login: Login): Observable<UsuarioSesion> {
    let jwtObs = this.backend.login(login.email, login.password);
    let usuarioObs = jwtObs.pipe(concatMap(jwt => this.backend.getUsuario(this.getUsuarioIdFromJwt(jwt))));
    let join = forkJoin({ jwt: jwtObs, usuario: usuarioObs });
    let usuarioSesion = join.pipe(map(obj => {
      return {
        id: obj.usuario.id,
        nombre: obj.usuario.nombre,
        apellido1: obj.usuario.apellido1,
        apellido2: obj.usuario.apellido2,
        email: obj.usuario.email,
        roles: obj.usuario.administrador ? [{ rol: Rol.ADMINISTRADOR }] : [],
        jwt: obj.jwt
      };
    }));
    return usuarioSesion
      .pipe(concatMap(usuarioSesion => this.completarConRoles(usuarioSesion)))
      .pipe(map(usuarioSesion => {
        localStorage.setItem('usuario', JSON.stringify(usuarioSesion));
        if (usuarioSesion.roles.length > 0) {
          this.rolCentro = usuarioSesion.roles[0];
        } else {
          this.rolCentro = undefined;
        }
        return usuarioSesion;
      }));

  }

  private completarConRoles(usuarioSesion: UsuarioSesion): Observable<UsuarioSesion> {
    // TODO: acceder a lo sotros servicios (o simular) para completar con los roles necesarios
    return of(usuarioSesion);
  }

  private getUsuarioIdFromJwt(jwt: string): number {
    let payload = jose.decodeJwt(jwt);
    console.log("Payload: " + JSON.stringify(payload));
    let id = payload.sub;
    if (id == undefined) {
      return 0;
    } else {
      return parseInt(id);
    }
  }

  get rolCentro(): RolCentro | undefined {
    return this._rolCentro;
  }

  set rolCentro(r: RolCentro | undefined) {
    this._rolCentro = r;
  }

  getUsuarioSesion(): UsuarioSesion | undefined {
    const usuario = localStorage.getItem('usuario');
    return usuario ? JSON.parse(usuario) : undefined;
  }

  doLogout() {
    localStorage.removeItem('usuario');
  }

  doForgottenPassword(email: string): Observable<void> {
    return this.backend.forgottenPassword(email);
  }

  doCambiarContrasenia(password: string, token: string): Promise<void> {
    return lastValueFrom(this.backend.resetPassword(token, password), { defaultValue: undefined });
  }

  getUsuarios(): Observable<Usuario[]> {
    return this.backend.getUsuarios();
  }

  editarUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.putUsuario(usuario);
  }

  eliminarUsuario(id: number): Observable<void> {
    return this.backend.deleteUsuario(id);
  }

  aniadirUsuario(usuario: Usuario): Observable<Usuario> {
    return this.backend.postUsuario(usuario);
  }

  /*getGerente(id: number) : Observable<Gerente>{
    const gerenteUsuario = this.backend.getUsuario(id);
    for (let id in usuarios) {
      if (id == gerenteUsuario.id){
        const aux = id;
      } 
      return this.backend.getGerente(parseInt (id));
  }

}*/

  getGerente(id: number): Observable<Gerente>{
    // 1. Leverage RxJS for asynchronous operations:
    return this.backend.getUsuario(id).pipe(
      // 2. Use the `filter` operator to efficiently find the matching Gerente:
      filter(usuario => usuario.id === id),
      // 3. Handle the case where no match is found (optional):
      catchError(() => {
        // Throw a custom error or return an empty Observable
        return throwError(() => new Error('Gerente not found'));
      }),
      // 4. Map to the Gerente object (assuming 'getUsuario' returns a Gerente):
      map(usuario => (usuario as unknown as Gerente))
    );
  }
}
