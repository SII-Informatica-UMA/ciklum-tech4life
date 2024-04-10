import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { MensajeService } from '../services/mensaje.service';
import { DetalleMensajeComponent } from "../detalle-mensaje/detalle-mensaje.component";
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../services/usuario.service';
import { UsuarioSesion } from '../entities/login';
import { Centro } from '../entities/centro';
import { CentrosService } from '../services/centro.service';
import { catchError, map, throwError } from 'rxjs';

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

  constructor(private MensajeService: MensajeService, private usuariosService: UsuariosService, private centroService : CentrosService) { }

  //usuarioLoginNombre = this.contactosService.getUsuarioLoginNombre();
  usuario!: UsuarioSesion;
  centro: Centro[] | undefined;
  ngOnInit(): void {
    this.usuariosService.getGerente(this.usuario.id).pipe(
   
      catchError(error => {
       
        console.error('Error fetching Gerente:', error);
        return throwError(() => new Error('Error fetching Gerente')); 
      }),
      
      map(gerente => {
        if (gerente) {
          
          this.centro = gerente.centros; 
      
        }
        return gerente; 
      })
      
    ).subscribe();
    this.centro = this.centroService.getCentrosUsuario();
    this.usuario = this.usuariosService.getUsuarioSesion();
    this.listaMensajes = this.MensajeService.getMensajes();
    this.mensajes = 
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
