import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InformacionComponent } from '../informacion/informacion.component';

@Component({
  selector: 'app-barra-navegacion-gerente',
  standalone: true,
  imports: [CommonModule, FormsModule, InformacionComponent],
  templateUrl: './barra-navegacion-gerente.component.html',
  styleUrl: './barra-navegacion-gerente.component.css'
})
export class BarraNavegacionGerenteComponent {
  logo = './assets/logoT4L.png'
  isDropdownOpen = false;
  
}
