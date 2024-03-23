import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Centro } from '../Centro';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import { FormularioCentroComponent } from '../formulario-centro/formulario-centro.component';
import { CentrosService } from '../centros.service';


@Component({
  selector: 'app-detalles-centro',
  templateUrl: './detallescentro.html',
  styleUrls: ['./detallescentro.cs']
})
export class DetalleCentroComponent {
  @Input() centro?: Centro;
  @Output() centroEditado = new EventEmitter<Centro>();
  @Output() centroEliminado = new EventEmitter<number>();

  constructor(private CentrosService: CentrosService, private modalService: NgbModal) { }

  editarcentro(): void {
    let ref = this.modalService.open(FormularioCentroComponent);
    ref.componentInstance.accion = "Editar";
    ref.componentInstance.centro = {...this.centro};
    ref.result.then((centro: Centro) => {
      this.centroEditado.emit(centro);
    }, (reason) => {});
  }

  eliminarCentro(): void {
    this.centroEliminado.emit(this.centro?.id);
  }
}
