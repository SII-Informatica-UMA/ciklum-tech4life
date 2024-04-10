import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { MensajeService } from '../services/mensaje.service';
import { DetalleMensajeComponent } from "../detalle-mensaje/detalle-mensaje.component";
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../services/usuario.service';

@Component({
    selector: 'app-correo-bandeja-entrada',
    standalone: true,
    templateUrl: './correo-bandeja-entrada.component.html',
    styleUrl: './correo-bandeja-entrada.component.css',
    imports: [DetalleMensajeComponent, CommonModule]
})
export class CorreoBandejaEntradaComponent {
  listaMensajes: Mensaje[] = []; // Suponiendo que tienes una lista de mensajes definida
  mensajes: Mensaje[] = [];
  mensajeSeleccionado?: Mensaje; // Suponiendo que tienes una variable para el mensaje seleccionado

  constructor(private MensajeService: MensajeService, private usuariosService: UsuariosService) { }

  //usuarioLoginNombre = this.contactosService.getUsuarioLoginNombre();
  usuarioLoginNombre = 'Juan';
  
  ngOnInit(): void {
    this.listaMensajes = this.MensajeService.getMensajes();
    this.mensajes = this.MensajeService.getMensajesEntrada(this.listaMensajes,this.usuarioLoginNombre);
  }

  elegirMensaje(mensaje: Mensaje): void {
    this.mensajeSeleccionado = mensaje;
  }

  eliminarMensaje(id: number): void {
    this.MensajeService.eliminarMensaje(id);
    this.mensajes = this.MensajeService.getMensajesEntrada(this.listaMensajes, this.usuarioLoginNombre);
    this.mensajeSeleccionado = undefined;
  }
  
}
