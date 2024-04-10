import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';
import { UsuariosService } from '../services/usuario.service';
import { Usuario } from '../entities/usuario';
import { CarruselComponent } from '../carrusel/carrusel.component';
import { InformacionCentroComponent } from '../informacion-centro/informacion-centro.component';
import { CorreoMenuComponent } from '../correo-menu/correo-menu.component';

@Component({
  selector: 'app-barra-navegacion-gerente',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent, CarruselComponent, InformacionCentroComponent, CorreoMenuComponent],
  templateUrl: './barra-navegacion-gerente.component.html',
  styleUrl: './barra-navegacion-gerente.component.css'
})
export class BarraNavegacionGerenteComponent {
  mostrarRol() {
    const Adm = "Administrador";
    const Usr = "Usuario";
    if (this.usuario?.administrador == true) {
      return Adm;
    } else {
      return Usr;
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
  usuario: Usuario | undefined;

  constructor(private usuariosService: UsuariosService) { }

  ngOnInit(): void {
    this.usuario = this.usuariosService.getLogin();
  }
}