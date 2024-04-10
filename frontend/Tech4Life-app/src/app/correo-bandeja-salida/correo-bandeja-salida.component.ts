import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { MensajeService } from '../services/mensaje.service';
import { DetalleMensajeComponent } from "../detalle-mensaje/detalle-mensaje.component";
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../services/usuario.service';
import { UsuarioSesion } from '../entities/login';
import { Centro } from '../entities/centro';
import { catchError, map, switchMap, throwError } from 'rxjs';

@Component({
    selector: 'app-correo-bandeja-salida',
    standalone: true,
    templateUrl: './correo-bandeja-salida.component.html',
    styleUrl: './correo-bandeja-salida.component.css',
    imports: [DetalleMensajeComponent, CommonModule]
})
export class CorreoBandejaSalidaComponent {
  listaMensajes: Mensaje[] = [];
  mensajes: Mensaje[] = []; // Suponiendo que tienes una lista de mensajes definida
  mensajeSeleccionado?: Mensaje; // Suponiendo que tienes una variable para el mensaje seleccionado

  constructor(private mensajeService: MensajeService, private usuariosService: UsuariosService) { }

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
      }),
      switchMap(centro => {
        return this.mensajeService.getMensajesCentro(centro.centros); 
      })
    ).subscribe(mensajes => {
      // Filtrar mensajes donde el usuario está en la lista de destinatarios
      const mensajesEntrada = mensajes.filter(
        mensaje => mensaje.remitente.some(
          remitente => remitente.id === this.usuario.id
        )
      );
      this.listaMensajes.push(...mensajesEntrada);
    });
  }

  elegirMensaje(mensaje: Mensaje): void {
    this.mensajeSeleccionado = mensaje;
  }

  eliminarMensaje(id: number): void {
    this.mensajeService.eliminarMensaje(id);

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
      }),
      switchMap(centro => {
        return this.mensajeService.getMensajesCentro(centro.centros); 
      })
    ).subscribe(mensajes => {
      // Filtrar mensajes donde el usuario está en la lista de destinatarios
      const mensajesEntrada = mensajes.filter(
        mensaje => mensaje.remitente.some(
          remitente => remitente.id === this.usuario.id
        )
      );
      this.listaMensajes.push(...mensajesEntrada);
    });
  
    this.mensajeSeleccionado = undefined;
  }
  
}
