import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';
import { ContactosService } from '../usuario.service';
import { Usuario } from '../usuario';
import { CarruselComponent } from '../carrusel/carrusel.component';
import { InformacionCentroComponent } from '../informacion-centro/informacion-centro.component';

@Component({
  selector: 'app-barra-navegacion-gerente',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent, CarruselComponent, InformacionCentroComponent],
  templateUrl: './barra-navegacion-gerente.component.html',
  styleUrl: './barra-navegacion-gerente.component.css'
})
export class BarraNavegacionGerenteComponent {
  isMenu = true;
  isInfoCentro = false;
  cerrarVentanas(){
  this.isMenu=false;
  this.isInfoCentro=false;  
  }
abrirMenu() {
  this.cerrarVentanas();
  this.isMenu=true;
}
abrirInfoCentro(){
  this.cerrarVentanas();
  this.isInfoCentro=true;
}
  logo = './assets/logoT4L.png'
  isDropdownOpen = false;
  usuario: Usuario | undefined;
  constructor(private contactosService: ContactosService) { }
  ngOnInit(): void {
  this.usuario = this.contactosService.getLogin();  
}
}