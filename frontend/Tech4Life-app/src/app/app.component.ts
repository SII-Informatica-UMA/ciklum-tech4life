import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ListaCentrosComponent } from "./lista-centros/lista-centros.component";
import { ListaGerentesComponent } from './lista-gerentes/lista-gerentes.component';
@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    imports: [RouterOutlet, ListaCentrosComponent,ListaGerentesComponent]
})
export class AppComponent {
  title = 'Tech4Life-app';
}
