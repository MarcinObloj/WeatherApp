import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ObservedCitiesComponent } from './observed-cities.component';

describe('ObservedCitiesComponent', () => {
  let component: ObservedCitiesComponent;
  let fixture: ComponentFixture<ObservedCitiesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ObservedCitiesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ObservedCitiesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
