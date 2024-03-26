import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MensajeNavegacionComponent } from './mensaje-navegacion.component';

describe('MensajeNavegacionComponent', () => {
  let component: MensajeNavegacionComponent;
  let fixture: ComponentFixture<MensajeNavegacionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MensajeNavegacionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MensajeNavegacionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
