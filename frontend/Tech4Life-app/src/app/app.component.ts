/* 
  app.component.ts
  Esto es el typescript del componente principal o raiz
*/

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { ListaCentrosComponent } from "./lista-centros/lista-centros.component";
import { ListaGerentesComponent } from './lista-gerentes/lista-gerentes.component';
import { DetallesCentroComponent } from './detalles-centro/detalles-centro.component';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app.routes';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';


// Aquí hay que importar todos los componentes de la aplicación.
import { InformacionCentroComponent } from './informacion-centro/informacion-centro.component';

@Component({
    selector: 'app-root',
    standalone: true,
    templateUrl: './app.component.html',
    styleUrl: './app.component.css',
    // En imports hay que poner los todos los componentes de la aplicación
    imports: [RouterOutlet, InformacionCentroComponent,ListaCentrosComponent,ListaGerentesComponent] 

})
export class AppComponent {
  title = 'Tech4Life-app';
}
