import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MisLogrosComponent } from './mis-logros.component';

describe('MisLogrosComponent', () => {
  let component: MisLogrosComponent;
  let fixture: ComponentFixture<MisLogrosComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MisLogrosComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MisLogrosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
