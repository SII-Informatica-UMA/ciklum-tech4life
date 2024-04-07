import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InformacionCentroComponent } from './informacion-centro.component';

describe('InformacionCentroComponent', () => {
  let component: InformacionCentroComponent;
  let fixture: ComponentFixture<InformacionCentroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InformacionCentroComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(InformacionCentroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
