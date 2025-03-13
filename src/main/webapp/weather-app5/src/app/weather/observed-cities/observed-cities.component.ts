import { Component, inject, OnInit, signal } from '@angular/core';
import { ObservedCitiesService } from '../services/observed-cities.service';
import { Forecast } from '../../models/forecast.model';
import { Router, RouterLink } from '@angular/router';
import { DecimalPipe } from '@angular/common';
import { MatIcon } from '@angular/material/icon';
import { ModalComponent } from '../../shared/modal/modal.component';

@Component({
  selector: 'app-observed-cities',
  imports: [RouterLink, DecimalPipe, MatIcon, ModalComponent],
  templateUrl: './observed-cities.component.html',
  styleUrl: './observed-cities.component.css',
})
export class ObservedCitiesComponent implements OnInit {
  private weatherService = inject(ObservedCitiesService);
  forecasts = signal<Forecast[]>([]);
  selectedCityId = signal<number | null>(null);
  showModal = signal(false);
  modalTitle = signal('');
  modalMessage = signal('');
  router = inject(Router);

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      const numericUserId = Number(userId);
      this.weatherService.getForecastsForUser(numericUserId).subscribe({
        next: (forecast) => {
          const reversedForecasts = forecast.reverse();

          const uniqueForecasts = reversedForecasts.filter(
            (f, index, self) =>
              index === self.findIndex((f2) => f2.city.id === f.city.id)
          );
          this.forecasts.set(uniqueForecasts);
          console.log('Observed cities:', this.forecasts());
        },
        error: (err) => console.error('Error fetching forecasts:', err),
      });
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  viewChart(cityId: number): void {
    this.router.navigate(['/city-chart', cityId]);
  }

  openMenu(cityId: number): void {
    this.selectedCityId.set(this.selectedCityId() === cityId ? null : cityId);
  }

  addToMainCity(cityId: number): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      const numericUserId = Number(userId);
      this.weatherService.setMainCity(numericUserId, cityId).subscribe({
        next: () => {
          localStorage.setItem('mainCityId', cityId.toString());
          this.modalTitle.set('Success');
          this.modalMessage.set('City added to main city!');
          this.showModal.set(true);
          this.selectedCityId.set(null);
        },
        error: (err) => {
          this.modalTitle.set('Error');
          this.modalMessage.set(`Error setting main city: ${err.message}`);
          this.showModal.set(true);
          console.error('Error setting main city:', err);
        },
      });
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  closeModal(): void {
    this.showModal.set(false);
  }
}
