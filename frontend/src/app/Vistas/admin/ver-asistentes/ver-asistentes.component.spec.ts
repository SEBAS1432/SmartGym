import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VerAsistentesComponent } from './ver-asistentes.component';

describe('VerAsistentesComponent', () => {
  let component: VerAsistentesComponent;
  let fixture: ComponentFixture<VerAsistentesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VerAsistentesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VerAsistentesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
