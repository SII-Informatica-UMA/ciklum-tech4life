import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BarraNavegacionGerenteComponent } from './barra-navegacion-gerente.component';

describe('BarraNavegacionGerenteComponent', () => {
  let component: BarraNavegacionGerenteComponent;
  let fixture: ComponentFixture<BarraNavegacionGerenteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BarraNavegacionGerenteComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BarraNavegacionGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
