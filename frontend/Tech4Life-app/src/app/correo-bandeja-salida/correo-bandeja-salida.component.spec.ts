import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorreoBandejaSalidaComponent } from './correo-bandeja-salida.component';

describe('CorreoBandejaSalidaComponent', () => {
  let component: CorreoBandejaSalidaComponent;
  let fixture: ComponentFixture<CorreoBandejaSalidaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorreoBandejaSalidaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CorreoBandejaSalidaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
