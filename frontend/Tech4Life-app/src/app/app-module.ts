import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { CentroList } from './CentroList';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component';


@NgModule({
  declarations: [
    CentroList,
    FormularioCentroComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [CentroList]
})
export class AppModule { }
