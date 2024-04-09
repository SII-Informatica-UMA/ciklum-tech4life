import { Component } from '@angular/core';
import { Mensaje } from '../mensaje';
import { CommonModule } from '@angular/common'; 
import { FormularioMensajeComponent } from '../formulario-mensaje/formulario-mensaje.component';
import { MensajeService } from '../mensaje.service';
import { DetalleMensajeComponent } from '../detalle-mensaje/detalle-mensaje.component';
import { FormsModule } from '@angular/forms';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { CorreoBandejaEntradaComponent } from '../correo-bandeja-entrada/correo-bandeja-entrada.component';
import { CorreoBandejaSalidaComponent } from '../correo-bandeja-salida/correo-bandeja-salida.component';
import { ContactosService } from '../usuario.service';

@Component({
    selector: 'app-correo-menu',
    standalone: true,
    templateUrl: './correo-menu.component.html',
    styleUrl: './correo-menu.component.css',
    imports: [CorreoBandejaEntradaComponent, CorreoBandejaSalidaComponent,CommonModule, DetalleMensajeComponent, FormsModule]
})

export class CorreoMenuComponent {
  mensajes: Mensaje[] = [];

  isAbrirEntrada = false;
  isAbrirSalida = false;

  constructor(private mensajeService: MensajeService, private contactosService: ContactosService, private modalService: NgbModal) { }
  
  //usuarioLoginNombre = this.contactosService.getUsuarioLoginNombre();
  usuarioLoginNombre = 'Juan';
  
  cerrarVentanas(){
    this.isAbrirEntrada = false;
    this.isAbrirSalida = false;
  }
  abrirEntrada(){
    this.cerrarVentanas();
    this.isAbrirEntrada=true;
  }
  abrirSalida(){
    this.cerrarVentanas();
    this.isAbrirSalida=true;
  }
  redactar(): void {
    this.cerrarVentanas();
      let ref = this.modalService.open(FormularioMensajeComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.mensaje = {id: 0, remitente: this.usuarioLoginNombre, destinatario: '', asunto: '', contenido: '', fechaHora: new Date()};
      ref.result.then((mensaje: Mensaje) => {
        this.mensajeService.redactar(mensaje);
        this.mensajes = this.mensajeService.getMensajesSalida(this.mensajes, this.usuarioLoginNombre);
        this.OrdenarPorFecha();
      }, (reason) => {});
  }
  OrdenarPorFecha(): void {
    this.mensajes.sort((a, b) => b.fechaHora.getTime() - a.fechaHora.getTime());  
  }
}


