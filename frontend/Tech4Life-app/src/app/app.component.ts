import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ListaCentrosComponent } from "./lista-centros/lista-centros.component";

@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    imports: [RouterOutlet, ListaCentrosComponent]
})
export class AppComponent {
  title = 'Tech4Life-app';
}
