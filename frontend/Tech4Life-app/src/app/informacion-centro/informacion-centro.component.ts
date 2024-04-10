/* 
  informacion-centro.component.ts
  Esto es el typescript del componente que ha realizado Raúl
*/
import { Component } from '@angular/core';
import { NgOptimizedImage } from '@angular/common'
import { Centro } from '../entities/centro';
import { CentrosService } from '../services/centro.service';
import { Usuario } from '../entities/usuario';
import { UsuariosService } from '../services/usuario.service';
import { CommonModule } from '@angular/common';
import { BackendService } from '../services/backend.service';
import { UsuarioSesion } from '../entities/login';
import { Observable, catchError, map, throwError } from 'rxjs';

// Aquí hay que importar todos los componentes que vaya a usar este componente.
// Por ahora ninguno.

@Component({
  selector: 'app-informacion-centro',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './informacion-centro.component.html',
  styleUrl: './informacion-centro.component.css'
})
export class InformacionCentroComponent {

  usuario!: UsuarioSesion;
  centros: Centro[] | undefined;

  // Simplemente el constructor
  constructor(private centrosService: CentrosService, private usuariosService: UsuariosService) { }


  ngOnInit() {
    this.usuariosService.getGerente(this.usuario.id).pipe(
   
      catchError(error => {
       
        console.error('Error fetching Gerente:', error);
        return throwError(() => new Error('Error fetching Gerente')); 
      }),
      
      map(gerente => {
        if (gerente) {
          
          this.centros = gerente.centros; 
      
        }
        return gerente; 
      })
      
    ).subscribe();
  }
  centroImagen = 'assets/gimnasio.png';
}

  
// En usuarioLoginID guardamos la id del usuario que se ha loggueado en la aplicación
// usuarioLoginID = this.usuariosService.getUsuarioLoginID();

// En usuarioLoginNombre guardamos el nombre del usuario que se ha loggueado en la aplicación
//usuarioLoginNombre = this.usuariosService.getUsuarioLoginNombre();

// centros va a contener una lista de objetos Centro, solo apareceran los centros de los que es gestor el usuario logguead

