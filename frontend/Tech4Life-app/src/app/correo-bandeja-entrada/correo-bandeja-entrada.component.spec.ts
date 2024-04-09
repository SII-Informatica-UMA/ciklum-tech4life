import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorreoBandejaEntradaComponent } from './correo-bandeja-entrada.component';

describe('CorreoBandejaEntradaComponent', () => {
  let component: CorreoBandejaEntradaComponent;
  let fixture: ComponentFixture<CorreoBandejaEntradaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorreoBandejaEntradaComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CorreoBandejaEntradaComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
