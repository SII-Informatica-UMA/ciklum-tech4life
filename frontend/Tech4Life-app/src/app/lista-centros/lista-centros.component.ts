import { Component, OnInit } from '@angular/core';
import { Centro } from '../entities/centro';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DetallesCentroComponent } from '../detalles-centro/detalles-centro.component';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';
import { CentrosService } from '../services/centro.service';
import { map } from 'rxjs';

@Component({
    selector: 'app-lista-centros',
    standalone: true,
    templateUrl: './lista-centros.component.html',
    styleUrl: './lista-centros.component.css',
    imports: [DetallesCentroComponent,FormsModule,CommonModule]
})
export class ListaCentrosComponent implements OnInit {

  onSearch($event: Event) {
  throw new Error('Method not implemented.');
  }
    contactoAEliminar?: Centro;
    centro:Centro [] = [];
    
    contactoElegido?: Centro;
    term: string = ''; // término de búsqueda
    constructor(private centroService: CentrosService, private modalService: NgbModal) { }
  
    //actualiza en tiempo real
    ngOnInit(): void {
      
      this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
      this.OrdenarPorNombre();
    }
 
    //cerrar paneles cuando se abre otro:
    cerrarPaneles(): void {
       this.contactoElegido = undefined;
       this.contactoAEliminar = undefined;
    }
    //elige contacto
    elegirContacto(contacto: Centro): void {
      this.cerrarPaneles();
      this.contactoElegido = contacto;
      
    }
  
  
    //cierra detalles
    cerrarDetalles() {
      this.contactoElegido = undefined; 
      this.contactoAEliminar= undefined;// Establece el contactoElegido como null para cerrar los detalles
    }
  
  
    //añade centro
    aniadirContacto(): void {
      this.cerrarPaneles();
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Añadir";
      ref.componentInstance.contacto = {id: 0, nombre: '', direccion: ''};
      ref.result.then((contacto: Centro) => {
        this.centroService.addCentro(contacto);
        this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
      this.OrdenarPorNombre();
      }, (reason) => {});
      
    }
  
    //edita centro
    contactoEditado(contacto: Centro): void {
     
      this.centroService.editarCentro(contacto);
      this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
      this.contactoElegido = this.centro.find(c => c.idCentro == contacto.idCentro);
     
    }
  
    //Elimina centro
    eliminarContacto(id: number): void {
      this.centroService.eliminarCentro(id);
      this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
      this.contactoAEliminar = undefined;
    }
    mostrarConfirmacion(contacto: any) {
      this.cerrarPaneles();
      this.contactoAEliminar = contacto; // Guarda el contacto a eliminar
     
    }
    
  //Editar cada fila:
  
    editarContacto(contacto: Centro): void {
      let ref = this.modalService.open(FormularioCentroComponent);
      ref.componentInstance.accion = "Editar";
      ref.componentInstance.contacto = {}; 
      ref.result.then((contactoEditado: Centro) => {
        // Actualizar los datos del contacto editado en la lista de contactos
        this.centroService.editarCentro(contactoEditado)
        this.OrdenarPorNombre();
      }, (reason) => { });
    }
  
  //ordena los centros por nombres
    OrdenarPorNombre(): void {
      this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros.sort((a, b) => a.nombre.localeCompare(b.nombre)));
    }
  
     // Barra de búsqueda
     searchContact() {
      this.cerrarPaneles()
      if (!this.term.trim()) {
        this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
      } else {
        this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros.filter(contacto =>
        contacto.nombre.toLowerCase().includes(this.term.toLowerCase())
      ));
      }
    }
  //limpia la busqueda , devuelve a estado inicial
    clearSearch() {

      this.term = '';
      this.centroService.getCentro()
      .pipe(
          map(centros => centros as Centro[]) // Utiliza map para convertir el Observable en un array
      )
      .subscribe(centros => this.centro = centros);
    }
  //cuando se pulsa enter se activa el buscar centro
    onEnter(event: KeyboardEvent) {
      this.cerrarPaneles();
      if (event.key === "Enter") {
        this.searchContact();
      }
    }
  
  }