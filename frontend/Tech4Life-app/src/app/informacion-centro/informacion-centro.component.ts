/* 
  informacion-centro.component.ts
  Esto es el typescript del componente que ha realizado Raúl
*/
import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common'

// Aquí hay que importar todos los componentes que vaya a usar este componente.
// Por ahora ninguno.

@Component({
  selector: 'app-informacion-centro',
  standalone: true,
  imports: [],
  templateUrl: './informacion-centro.component.html',
  styleUrl: './informacion-centro.component.css'
})
export class InformacionCentroComponent {
  
  // Variables que me tiene que dar Ana Martín de la gestión de centros o el objeto centro
  // Fijarnos del ts del profe
  centroNombre = "BasicFit" ; // centro.centroNombre
  centroDir = "Urb avestruz, calle calamar, nº5" ;
  centroValorac = "3 estrellas" ;
  centroInscr = 1259 ;
  centroImagen = 'assets/gimnasio.png' ;
}
