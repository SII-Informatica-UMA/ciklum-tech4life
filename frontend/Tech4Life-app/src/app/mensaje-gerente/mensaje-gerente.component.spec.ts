import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MensajeGerenteComponent } from './mensaje-gerente.component';

describe('MensajeGerenteComponent', () => {
  let component: MensajeGerenteComponent;
  let fixture: ComponentFixture<MensajeGerenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MensajeGerenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MensajeGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
