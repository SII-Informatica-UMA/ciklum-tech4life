import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {MensajeGerenteComponent} from './mensaje-gerente/mensaje-gerente.component';
import {MensajeNavegacionComponent} from './mensaje-navegacion/mensaje-navegacion.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MensajeGerenteComponent, MensajeNavegacionComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Tech4Life-app';
}
