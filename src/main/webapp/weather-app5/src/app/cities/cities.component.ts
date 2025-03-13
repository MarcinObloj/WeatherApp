// filepath: /C:/Users/User/Desktop/htdocs/weather-app5/src/app/cities/cities.component.ts
import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { CitiesService } from '../weather/services/cities.service';
import { City } from '../models/city.model';
import { MatIconModule } from '@angular/material/icon';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { CityFilterPipe } from '../city-filter.pipe';
import { FormsModule } from '@angular/forms';
import { MatFormField } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-cities',
  imports: [
    MatIconModule,
    CommonModule,
    RouterLink,
    CityFilterPipe,
    FormsModule,
    MatFormField,
    MatInputModule,
  ],
  templateUrl: './cities.component.html',
  styleUrls: ['./cities.component.css'],
})
export class CitiesComponent implements OnInit {
  cities: City[] = [];
  observedCities: City[] = [];
  citiesService = inject(CitiesService);
  changeDetectorRef = inject(ChangeDetectorRef);
  searchText: string = '';
  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.citiesService.getAllCities(userId).subscribe({
        next: (cities) => {
          this.cities = cities;
          this.loadObservedCities(userId);
        },
        error: (error) => console.error('Error:', error),
      });
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  loadObservedCities(userId: string): void {
    this.citiesService.getObservedCities(userId).subscribe({
      next: (observedCities) => {
        console.log('Observed cities:', observedCities); // Debug
        this.observedCities = observedCities;
        this.updateFavoriteStatus();
      },
      error: (error) => console.error('Error:', error),
    });
  }

  updateFavoriteStatus(): void {
    this.cities = [...this.cities].map((city) => ({
      ...city,
      isFavorite: this.observedCities.some(
        (observedCity) => observedCity.id === city.id
      ),
    }));
  }

  toggleFavorite(city: City): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      this.citiesService
        .addCityToObserved(userId, city.id.toString())
        .subscribe({
          next: () => {
            this.loadObservedCities(userId);
          },
          error: (error) => console.error('Error:', error),
        });
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  trackByCityId(index: number, city: City): string {
    return `${city.id}-${city.isFavorite}`;
  }
}
