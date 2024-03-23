import { Component } from '@angular/core';
import  {Contacto} from '../contacto';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-formulario-contacto',
  templateUrl: './formulario-contacto.component.html',
  styleUrls: ['./formulario-contacto.component.css']
})
export class FormularioContactoComponent {
  accion?: "Añadir" | "Editar";
  contacto: Contacto = {id: 0, nombre: '', direccion: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarContacto(): void {
    this.modal.close(this.contacto);
  }

}
