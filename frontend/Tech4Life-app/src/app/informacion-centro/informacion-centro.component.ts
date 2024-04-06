/* 
  informacion-centro.component.ts
  Esto es el typescript del componente que ha realizado Raúl
*/
import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common'
import { Centro } from '../centro';
import { CentrosService } from '../centro.service';
import { CommonModule } from '@angular/common';

// Aquí hay que importar todos los componentes que vaya a usar este componente.
// Por ahora ninguno.

@Component({
  selector: 'app-informacion-centro',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './informacion-centro.component.html',
  styleUrl: './informacion-centro.component.css'
})
export class InformacionCentroComponent {

  constructor(private centrosService: CentrosService) {}
  centros = this.centrosService.getCentros();
  centroImagen = 'assets/gimnasio.png' ;
}
