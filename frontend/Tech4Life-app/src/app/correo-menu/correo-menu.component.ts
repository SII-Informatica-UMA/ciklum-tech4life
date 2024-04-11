import { Component } from '@angular/core';
import { Mensaje } from '../entities/mensaje';
import { CommonModule } from '@angular/common';
import { FormularioMensajeComponent } from '../formulario-mensaje/formulario-mensaje.component';
import { MensajeService } from '../services/mensaje.service';
import { DetalleMensajeComponent } from '../detalle-mensaje/detalle-mensaje.component';
import { FormsModule } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CorreoBandejaEntradaComponent } from '../correo-bandeja-entrada/correo-bandeja-entrada.component';
import { CorreoBandejaSalidaComponent } from '../correo-bandeja-salida/correo-bandeja-salida.component';
import { UsuariosService } from '../services/usuario.service';
import { UsuarioSesion } from '../entities/login';
import { catchError, map, switchMap, throwError } from 'rxjs';
import { Centro } from '../entities/centro';
import { CentrosService } from '../services/centro.service';

@Component({
  selector: 'app-correo-menu',
  standalone: true,
  templateUrl: './correo-menu.component.html',
  styleUrl: './correo-menu.component.css',
  imports: [CorreoBandejaEntradaComponent, CorreoBandejaSalidaComponent, CommonModule, DetalleMensajeComponent, FormsModule]
})

export class CorreoMenuComponent {

  isAbrirEntrada = false;
  isAbrirSalida = false;
  usuario!: UsuarioSesion;
  centro: Centro[] | undefined;
  listaMensajes: Mensaje[] = [];
  idGerente: number | undefined;

  constructor(private mensajeService: MensajeService, private usuariosService: UsuariosService, private modalService: NgbModal, private centroService: CentrosService) { }

  

  cerrarVentanas() {
    this.isAbrirEntrada = false;
    this.isAbrirSalida = false;
  }
  abrirEntrada() {
    this.cerrarVentanas();
    this.isAbrirEntrada = true;
  }
  abrirSalida() {
    this.cerrarVentanas();
    this.isAbrirSalida = true;
  }

  //ref.componentInstance.mensaje = {id: 0, remitente: this.usuarioLoginNombre, destinatario: '', asunto: '', contenido: '', fechaHora: new Date()};
  gerenteID(id:number): number | undefined{
    const gerenteId = this.usuariosService.getGerente(this.usuario.id).subscribe(gerente => {
      if (gerente) {
        this.idGerente = gerente.id;
      }
    });
    return this.idGerente;
  }
  

  redactar(): void {
    this.cerrarVentanas();
    let ref = this.modalService.open(FormularioMensajeComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.mensaje = {asunto: '', destinatario:'' , copia: '', copiaOculta: '', remitente: [{id: this.gerenteID(this.usuario.id) , tipo: 'GERENTE',},], contenido: ''};
    ref.result.then((mensaje: Mensaje) => {
      //this.mensajeService.redactar(mensaje);
      this.usuariosService.getGerente(this.usuario.id).pipe(
        catchError(error => {
          console.error('Error fetching Gerente:', error);
          return throwError(() => new Error('Error fetching Gerente'));
        }),
        map(gerente => {
          if (gerente) {
            this.centroService.getCentrosUsuario(gerente).pipe(
              map(centros => centros as Centro[]) // Convierte el Observable en un array
            ).subscribe(centros => {
              centros.forEach(centro => {
                this.mensajeService.getMensajesCentro(centro).subscribe(mensajes => {
                  // Filtrar mensajes donde el usuario es el remitente
                  const mensajesSalida = mensajes.filter(
                    mensaje => mensaje.remitente.id === this.usuario.id
                  );
                  //const mensajeSalida = mensajesSalida[0];
                  this.listaMensajes.push(...mensajesSalida);
                });
              });
            });
          }
          return gerente;
        }),
      ).subscribe();
    })
  }
}

