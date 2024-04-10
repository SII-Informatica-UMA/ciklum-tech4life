import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';
import { UsuariosService } from '../services/usuario.service';
import { Usuario } from '../entities/usuario';
import { ListaGerentesComponent } from '../lista-gerentes/lista-gerentes.component';
import { Router } from '@angular/router';
import { ListaCentrosComponent } from '../lista-centros/lista-centros.component';
import { CarruselComponent } from '../carrusel/carrusel.component';
import { UsuarioSesion } from '../entities/login';
import { ListadoUsuarioComponent } from '../listado-usuario/listado-usuario.component';

@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent, ListaGerentesComponent, ListaCentrosComponent, CarruselComponent, ListadoUsuarioComponent],
  templateUrl: './barra-navegacion.component.html',
  styleUrl: './barra-navegacion.component.css'
})
export class BarraNavegacionComponent {
  constructor(private usuariosService: UsuariosService) {}
  isGestionGerentes = false;
  isGestionCentros = false;
  isMenu = true;
  abrirMenu(){
    this.cerrarVentanas();
    this.isMenu=true;
  }
  cerrarVentanas(){
    this.isGestionGerentes = false;
    this.isGestionCentros = false;
    this.isMenu=false;
  }
  abrirGerentes(){
    this.cerrarVentanas();
    this.isGestionGerentes=true;
  }
  abrirCentros(){
    this.cerrarVentanas();
    this.isGestionCentros=true;
  }

logo = './assets/logoT4L.png'
isDropdownOpen = false;
usuario: Usuario | undefined;
ngOnInit(): void {
  this.usuario = this.usuariosService.getUsuarioSesion();
}
mostrarRol (){
  const Adm = "Administrador";
  const Usr = "Usuario";
  if (this.usuario?.isAdministrador()){
    return Adm;
  } else {
    return Usr;
  }
}
}


