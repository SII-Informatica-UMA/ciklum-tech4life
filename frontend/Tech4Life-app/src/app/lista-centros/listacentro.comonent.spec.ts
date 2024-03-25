import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CentroList } from './listacentro.component';
import { beforeEach } from 'node:test';

describe('CentroList', () => {
  let component: CentroList;
  let fixture: ComponentFixture<CentroList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CentroList]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CentroList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
