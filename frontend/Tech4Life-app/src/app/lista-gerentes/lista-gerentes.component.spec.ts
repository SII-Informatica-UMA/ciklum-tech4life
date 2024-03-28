import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListaGerentesComponent } from './lista-gerentes.component';

describe('ListaGerentesComponent', () => {
  let component: ListaGerentesComponent;
  let fixture: ComponentFixture<ListaGerentesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ListaGerentesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListaGerentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
