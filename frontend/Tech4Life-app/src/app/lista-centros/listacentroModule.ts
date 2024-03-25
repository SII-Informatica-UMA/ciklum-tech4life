import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CentroList } from './listacentro.component';
import { DetalleCentroComponent } from './detalle-centro/detalle-centro'; // Importa el componente de detalle

@NgModule({
  declarations: [
    CentroList, DetalleCentroComponent
  ],
  imports: [
    CommonModule
  ],
  exports: [
    CentroList, DetalleCentroComponent
  ]
})
export class CentroListModule { }