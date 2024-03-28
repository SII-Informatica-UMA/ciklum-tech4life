import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesCentroComponent } from './detalles-centro.component';

describe('DetallesCentroComponent', () => {
  let component: DetallesCentroComponent;
  let fixture: ComponentFixture<DetallesCentroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallesCentroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetallesCentroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
