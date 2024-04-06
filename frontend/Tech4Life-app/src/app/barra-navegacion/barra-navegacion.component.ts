import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-barra-navegacion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './barra-navegacion.component.html',
  styleUrl: './barra-navegacion.component.css'
})
export class BarraNavegacionComponent {
logo = './assets/logoT4L.png'
isDropdownOpen = false;

}


