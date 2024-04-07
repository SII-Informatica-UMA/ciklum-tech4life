import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetallesGerenteComponent } from './detalles-gerente.component';

describe('DetallesGerenteComponent', () => {
  let component: DetallesGerenteComponent;
  let fixture: ComponentFixture<DetallesGerenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetallesGerenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DetallesGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
