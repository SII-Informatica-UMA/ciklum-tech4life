/* 
  app.component.ts
  Esto es el typescript del componente principal o raiz
*/

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Aquí hay que importar todos los componentes de la aplicación.
import { InformacionCentroComponent } from './informacion-centro/informacion-centro.component';

@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    // En imports hay que poner los todos los componentes de la aplicación
    imports: [RouterOutlet, InformacionCentroComponent] 
})
export class AppComponent {
  title = 'Tech4Life-app';
}
