import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { MensajeService } from '../services/mensaje.service';
import { DetalleMensajeComponent } from "../detalle-mensaje/detalle-mensaje.component";
import { CommonModule } from '@angular/common';
import { UsuariosService } from '../services/usuario.service';
import { UsuarioSesion } from '../entities/login';
import { Centro } from '../entities/centro';
import { CentrosService } from '../services/centro.service';
import { catchError, map, switchMap, throwError } from 'rxjs';

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

  constructor(private mensajeService: MensajeService, private usuariosService: UsuariosService, private centrosService : CentrosService) { }

  //usuarioLoginNombre = this.contactosService.getUsuarioLoginNombre();
  usuario!: UsuarioSesion;
  centros: Centro[] | undefined;

ngOnInit(): void {
  this.usuariosService.getGerente(this.usuario.id).pipe(
    catchError(error => {
      console.error('Error fetching Gerente:', error);
      return throwError(() => new Error('Error fetching Gerente'));
    }),
    map(gerente => {
      if (gerente) {
        this.centrosService.getCentrosUsuario(gerente).pipe(
          map(centros => centros as Centro[]) // Convierte el Observable en un array
        ).subscribe(centros => {
          centros.forEach(centro => {
            this.mensajeService.getMensajesCentro(centro).subscribe(mensajes => {
              // Filtrar mensajes donde el usuario está en la lista de destinatarios
              const mensajesEntrada = mensajes.filter(
                mensaje => mensaje.destinatarios.some(
                  destinatario => destinatario.id === this.usuario.id
                )
              );
              this.listaMensajes.push(...mensajesEntrada);
            });
          });
        });
      }
      return gerente;
    }),
  ).subscribe();
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
          this.centrosService.getCentrosUsuario(gerente).pipe(
            map(centros => centros as Centro[]) // Convierte el Observable en un array
          ).subscribe(centros => {
            centros.forEach(centro => {
              this.mensajeService.getMensajesCentro(centro).subscribe(mensajes => {
                // Filtrar mensajes donde el usuario está en la lista de destinatarios
                const mensajesEntrada = mensajes.filter(
                  mensaje => mensaje.destinatarios.some(
                    destinatario => destinatario.id === this.usuario.id
                  )
                );
                this.listaMensajes.push(...mensajesEntrada);
              });
            });
          });
        }
        return gerente;
      }),
    ).subscribe();
  
    this.mensajeSeleccionado = undefined;
  }
  
  
  
}
