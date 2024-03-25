import { Component } from '@angular/core';
import  {Contacto} from '../centro';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';


@Component({
  selector: 'app-formulario-contacto',
  templateUrl: './formulario-centro.html',
  styleUrls: ['./formulario-centro.css']
})
export class FormularioContactoComponent {

  accion?: "Añadir" | "Editar";
  contacto: Contacto = {id: 0, nombre: '', direccion: ''};

  constructor(public modal: NgbActiveModal) { }

  guardarContacto(): void {
    this.modal.close(this.contacto);
  }

}
