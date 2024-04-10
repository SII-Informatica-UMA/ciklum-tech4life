import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';
import { UsuariosService } from '../services/usuario.service';
import { Usuario } from '../entities/usuario';
import { CarruselComponent } from '../carrusel/carrusel.component';
import { InformacionCentroComponent } from '../informacion-centro/informacion-centro.component';
import { CorreoMenuComponent } from '../correo-menu/correo-menu.component';
import { UsuarioSesion } from '../entities/login';
import { Rol } from '../entities/login';

@Component({
  selector: 'app-barra-navegacion-gerente',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent, CarruselComponent, InformacionCentroComponent, CorreoMenuComponent],
  templateUrl: './barra-navegacion-gerente.component.html',
  styleUrl: './barra-navegacion-gerente.component.css'
})
export class BarraNavegacionGerenteComponent {
  private get rol() {
    return this.usuariosService.rolCentro;
  }

  mostrarRol() {
    if (this.rol?.rol == Rol.GERENTE){
      return "Gerente";
    } else {
      return "Error";
    }
  }
  
  isMenu = true;
  isInfoCentro = false;
  isCorreo = false;
  cerrarVentanas() {
    this.isMenu = false;
    this.isInfoCentro = false;
    this.isCorreo = false;
  }
  abrirMenu() {
    this.cerrarVentanas();
    this.isMenu = true;
  }
  abrirInfoCentro() {
    this.cerrarVentanas();
    this.isInfoCentro = true;
  }

  abrirCorreo() {
    this.cerrarVentanas();
    this.isCorreo = true;
  }

  logo = './assets/logoT4L.png'
  isDropdownOpen = false;
  usuario: UsuarioSesion | undefined;

  constructor(private usuariosService: UsuariosService) { }

  ngOnInit(): void {
    this.usuario = this.usuariosService.getUsuarioSesion();
  }
}