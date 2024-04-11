import { Component, OnInit } from '@angular/core';
import {Usuario} from '../entities/usuario';
import { UsuariosService } from '../services/usuario.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioGerenteComponent } from '../formulario-gerente/formulario-gerente.component';
import { DetallesGerenteComponent } from '../detalles-gerente/detalles-gerente.component';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { map } from 'rxjs';
import { Gerente } from '../entities/gerente';

@Component({
  selector: 'app-lista-gerentes',
  standalone: true,
  imports: [DetallesGerenteComponent,FormsModule,CommonModule],
  templateUrl: './lista-gerentes.component.html',
  styleUrl: './lista-gerentes.component.css'
})

export class ListaGerentesComponent implements OnInit {
  onSearch($event: Event) {
  throw new Error('Method not implemented.');
  }
  
    contactos!:Usuario[];
    gerentes!:Gerente[];
    contactoElegido?:Usuario;
    contactoAEliminar?: Usuario;

    term: string = ''; // término de búsqueda
    constructor(private usuariosService: UsuariosService, private modalService: NgbModal) { }
  
    //actualiza en tiempo real
    ngOnInit(): void {
      this.usuariosService.getGerentes()
        .pipe(
            map(gerentes => gerentes as Gerente[]) 
        )
        .subscribe(gerentes => this.gerentes = gerentes);

     this.usuariosService.getUsersGerentes(this.gerentes).pipe(
        map(gerentes => gerentes as Usuario[]) 
    )
    .subscribe(gerentes => this.contactos = gerentes);
;
      
      this.OrdenarPorNombre();
    }
    
    //cerrar paneles cuando se abre otro:
    cerrarPaneles(): void {
      this.contactoElegido = undefined;
      this.contactoAEliminar = undefined;
    }
    //elige contacto
    elegirContacto(contacto: Usuario): void {
      this.cerrarPaneles();
      this.contactoElegido = contacto;
      
    }
  
  
    //cierra detalles
    cerrarDetalles() {
      this.contactoElegido = undefined;
      this.contactoAEliminar= undefined; // Establece el contactoElegido como null para cerrar los detalles
    }
  
  
    //añade gerente
    aniadirContacto(): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioGerenteComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.contacto = {id: 0, nombre: '', apellido: ''};
      ref.componentInstance.gerente = { empresa:''}

      ref.result.then((contacto: Gerente) => {
        this.usuariosService.aniadirGerentes(contacto);
        this.usuariosService.getGerentes()
        .pipe(
            map(gerentes => gerentes as Gerente[]) 
        )
        .subscribe(gerentes => this.gerentes = gerentes);

     this.usuariosService.getUsersGerentes(this.gerentes).pipe(
        map(contactos => contactos as Usuario[]) 
    )
    .subscribe(contactos=> this.contactos= contactos);
        this.OrdenarPorNombre();
      }, (reason) => {});
      
    }
  
    //edita gerente
    contactoEditado(contacto: Usuario): void {
      this.usuariosService.editarUsuario(contacto);
      this.usuariosService.getGerentes()
        .pipe(
            map(gerentes => gerentes as Gerente[]) 
        )
        .subscribe(gerentes => this.gerentes = gerentes);

     
        this.usuariosService.getUsersGerentes(this.gerentes).pipe(
          map(contactos => contactos as Usuario[]) 
      )
      .subscribe(contactos=> this.contactos= contactos);
      this.contactoElegido = this.contactos.find(c => c.id == contacto.id);
     
    }
  
    //Elimina gerente
    eliminarContacto(id: number): void {
      this.usuariosService.eliminarGerente(id);
      this.usuariosService.getGerentes()
      .pipe(
          map(gerentes => gerentes as Gerente[]) 
      )
      .subscribe(gerentes => this.gerentes = gerentes);
      this.contactoAEliminar = undefined;
    }
    
    mostrarConfirmacion(contacto: any) {
      this.cerrarPaneles();
      this.contactoAEliminar = contacto; // Guarda el contacto a eliminar
     
    }
  //Editar cada fila:
  
    editarContacto(contacto: Gerente): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioGerenteComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {}; 
      ref.result.then((contactoEditado: Usuario) => {
        // Actualizar los datos del contacto editado en la lista de contactos
        this.usuariosService.editarUsuario(contactoEditado)
      }, (reason) => { });
      ref.componentInstance.gerente={};
      ref.result.then((contactoEditado: Gerente) => {
        // Actualizar los datos del contacto editado en la lista de contactos
        this.usuariosService.editarGerente(contactoEditado)
        this.OrdenarPorNombre();
      }, (reason) => { });
    }
  
  //ordena los centros por nombres de empresas
    OrdenarPorNombre(): void {
      this.usuariosService.getGerentes()
      .pipe(
          map(gerentes => gerentes as Gerente[]) 
      )
      .subscribe(gerentes=> this.gerentes = gerentes.sort((a, b) => a.empresa.localeCompare(b.empresa)));
      
    }
  
     // Barra de búsqueda
     searchContact() {
      this.cerrarPaneles();
      if (!this.term.trim()) {
        this.usuariosService.getUsersGerentes(this.gerentes).pipe(
          map(contactos => contactos as Usuario[]) 
      )
      .subscribe(contactos=> this.contactos= contactos);
      this.usuariosService.getUsersGerentes(this.gerentes).pipe(
        map(contactos => contactos as Usuario[]) 
    )
    .subscribe(contactos=> this.contactos= contactos.filter(contacto =>
      contacto.nombre.toLowerCase().includes(this.term.toLowerCase())));
       
      }
    }
  //limpia la busqueda , devuelve a estado inicial
    clearSearch() {
      this.term = '';
      this.usuariosService.getUsersGerentes(this.gerentes).pipe(
        map(contactos => contactos as Usuario[]) 
    )
    .subscribe(contactos=> this.contactos= contactos);
    }
  //cuando se pulsa enter se activa el buscar gerente
    onEnter(event: KeyboardEvent) {
      this.cerrarPaneles();
      if (event.key === "Enter") {
        this.searchContact();
      }
    }
  
  }