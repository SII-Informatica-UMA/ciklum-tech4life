import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BarraNavegacionComponent } from './barra-navegacion/barra-navegacion.component'
import { CarruselComponent } from './carrusel/carrusel.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet,BarraNavegacionComponent,CarruselComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'Tech4Life-app';
}

