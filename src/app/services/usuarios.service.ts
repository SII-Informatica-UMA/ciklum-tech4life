import { Injectable } from "@angular/core";
import { Login, UsuarioSesion, Rol, RolCentro } from "../entities/login";
import { Observable, of, forkJoin, concatMap, lastValueFrom } from "rxjs";
import {map} from 'rxjs/operators';
import { HttpClient } from "@angular/common/http";
import { BACKEND_URI } from "../config/config";


import { passwords, usuarios } from "./usuarios.db.service";
import { Usuario } from "../entities/usuario";
import { BackendFakeService } from "./backend.fake.service";

@Injectable({
  providedIn: 'root'
})
export class UsuariosService {
  _rolCentro?: RolCentro;

  constructor(private backend: BackendFakeService) {}

  doLogin(login: Login): Observable<UsuarioSesion> {
    let jwtObs = this.backend.login(login.email, login.password);
    let usuarioObs = jwtObs.pipe(concatMap(jwt=>this.backend.getUsuario(this.getUsuarioIdFromJwt(jwt))));
    let join = forkJoin({jwt: jwtObs, usuario: usuarioObs});
    let usuarioSesion = join.pipe(map(obj => {
      return {
        id: obj.usuario.id,
        nombre: obj.usuario.nombre,
        apellido1: obj.usuario.apellido1,
        apellido2: obj.usuario.apellido2,
        email: obj.usuario.email,
        roles: obj.usuario.administrador?[{rol: Rol.ADMINISTRADOR}]:[],
        jwt: obj.jwt
      };
    }));
    return usuarioSesion
    .pipe(concatMap(usuarioSesion=>this.completarConRoles(usuarioSesion)))
    .pipe(map(usuarioSesion=>{
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
    // TODO
    return of(usuarioSesion);
  }

  private getUsuarioIdFromJwt(jwt: string): number {
    return parseInt(jwt); // FIXME
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

  doForgottenPassword(email: string) {
    this.backend.forgottenPassword(email);
  }

  doCambiarContrasenia(password: string, token: string): Promise<void> {
    return lastValueFrom(this.backend.resetPassword(token, password),{defaultValue:undefined});
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



}
