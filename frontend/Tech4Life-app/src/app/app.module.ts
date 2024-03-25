import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import {AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CentroListModule } from './lista-centros/listacentroModule';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    CentroListModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }