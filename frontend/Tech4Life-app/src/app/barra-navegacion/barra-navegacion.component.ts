import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';
import { ContactosService } from '../usuario.service';
import { Usuario } from '../usuario';

@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent],
  templateUrl: './barra-navegacion.component.html',
  styleUrl: './barra-navegacion.component.css'
})
export class BarraNavegacionComponent {
logo = './assets/logoT4L.png'
isDropdownOpen = false;
usuario: Usuario | undefined;
constructor(private contactosService: ContactosService) { }
ngOnInit(): void {
  this.usuario = this.contactosService.getLogin();
}
mostrarRol (){
  const Adm = "Administrador";
  const Usr = "Usuario";
  if (this.usuario?.administrador==true){
    return Adm;
  } else {
    return Usr;
  }
}
}


