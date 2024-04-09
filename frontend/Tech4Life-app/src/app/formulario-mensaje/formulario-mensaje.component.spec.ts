import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FormularioMensajeComponent } from './formulario-mensaje.component';

describe('FormularioMensajeComponent', () => {
  let component: FormularioMensajeComponent;
  let fixture: ComponentFixture<FormularioMensajeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormularioMensajeComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(FormularioMensajeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
