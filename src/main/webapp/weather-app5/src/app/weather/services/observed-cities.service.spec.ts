import { TestBed } from '@angular/core/testing';

import { ObservedCitiesService } from '../observed-cities.service';

describe('ObservedCitiesService', () => {
  let service: ObservedCitiesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ObservedCitiesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
