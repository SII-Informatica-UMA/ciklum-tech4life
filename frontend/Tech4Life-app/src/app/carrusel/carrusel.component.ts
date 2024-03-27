import { Component } from '@angular/core';

@Component({
  selector: 'app-carrusel',
  standalone: true,
  imports: [],
  templateUrl: './carrusel.component.html',
  styleUrl: './carrusel.component.css'
})
export class CarruselComponent {
  carrusel1 = './assets/anuncioGimnasio.jpg';
  carrusel2 = './assets/anuncioGimnasio2.jpg';
  carrusel3 = './assets/gimnasioHotel.jpg';
  carrusel4 = './assets/hombreMaquina.jpg';
}
