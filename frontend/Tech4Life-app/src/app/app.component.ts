import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { BarraNavegacionComponent } from './barra-navegacion/barra-navegacion.component'

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [BarraNavegacionComponent],
  templateUrl: './barra-navegacion/barra-navegacion.component.html',
  styleUrl: './barra-navegacion/barra-navegacion.component.css'
})

export class AppComponent {
  title = 'Tech4Life-app';
}

