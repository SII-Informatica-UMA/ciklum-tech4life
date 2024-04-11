/* 
  app.component.ts
  Esto es el typescript del componente principal o raiz
*/

import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { RouterModule } from '@angular/router';
import { ListaCentrosComponent } from "./lista-centros/lista-centros.component";
import { ListaGerentesComponent } from './lista-gerentes/lista-gerentes.component';
import { DetallesCentroComponent } from './detalles-centro/detalles-centro.component';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { BarraNavegacionComponent } from './barra-navegacion/barra-navegacion.component'
import { CarruselComponent } from './carrusel/carrusel.component';
import { InformacionComponent } from './informacion/informacion.component';
import { BarraNavegacionGerenteComponent } from './barra-navegacion-gerente/barra-navegacion-gerente.component';
import { CorreoBandejaEntradaComponent } from './correo-bandeja-entrada/correo-bandeja-entrada.component';
import { CorreoBandejaSalidaComponent } from './correo-bandeja-salida/correo-bandeja-salida.component';
import { CorreoMenuComponent } from './correo-menu/correo-menu.component';
import { CommonModule, TitleCasePipe } from '@angular/common';
import { UsuariosService } from './services/usuario.service';


// Aquí hay que importar todos los componentes de la aplicación.
import { InformacionCentroComponent } from './informacion-centro/informacion-centro.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, RouterModule, InformacionCentroComponent,ListaCentrosComponent,ListaGerentesComponent,BarraNavegacionComponent,CarruselComponent,InformacionComponent,BarraNavegacionGerenteComponent, CorreoBandejaEntradaComponent, CorreoBandejaSalidaComponent , CorreoMenuComponent, CommonModule, FormsModule, TitleCasePipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  _rolIndex: number = 0

  constructor(private usuarioService: UsuariosService, private router: Router) {
    this.actualizarRol()
  }

  get rolIndex() {
    return this._rolIndex;
  }

  set rolIndex(i: number) {
    this._rolIndex = i;
    this.actualizarRol();
  }

  actualizarRol() {
    let u = this.usuarioSesion;
    if (u) {
      this.usuarioService.rolCentro = u.roles[this.rolIndex];
    } else {
      this.usuarioService.rolCentro = undefined;
    }
  }

  get rol() {
    return this.usuarioService.rolCentro;
  }

  get usuarioSesion() {
    return this.usuarioService.getUsuarioSesion();
  }

  logout() {
    this.usuarioService.doLogout();
    this.actualizarRol();
    this.router.navigateByUrl('/login');
  }
}