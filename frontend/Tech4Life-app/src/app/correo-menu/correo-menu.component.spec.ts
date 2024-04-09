import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CorreoMenuComponent } from './correo-menu.component';

describe('CorreoMenuComponent', () => {
  let component: CorreoMenuComponent;
  let fixture: ComponentFixture<CorreoMenuComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CorreoMenuComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CorreoMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
