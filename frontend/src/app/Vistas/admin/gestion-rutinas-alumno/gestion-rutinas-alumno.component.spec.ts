import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GestionRutinasAlumnoComponent } from './gestion-rutinas-alumno.component';

describe('GestionRutinasAlumnoComponent', () => {
  let component: GestionRutinasAlumnoComponent;
  let fixture: ComponentFixture<GestionRutinasAlumnoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GestionRutinasAlumnoComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GestionRutinasAlumnoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
