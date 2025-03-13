import { Component, inject, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { City } from '../../../models/city.model';
import { Forecast } from '../../../models/forecast.model';
import { ObservedCitiesService } from '../../services/observed-cities.service';
import { AuthService } from '../../../auth/services/auth.service';
import { CommonModule, DatePipe, DecimalPipe } from '@angular/common';
import { SidebarComponent } from "../../../shared/sidebar/sidebar.component";

import { CarouselModule } from 'primeng/carousel';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  standalone: true,
  imports: [DatePipe, CommonModule, DecimalPipe, SidebarComponent,CarouselModule],
  styleUrls: ['./profile.component.css'],
  
})
export class ProfileComponent implements OnInit {
  mainCity!: { city: City; forecast: Forecast };
  forecasts: Forecast[] = [];

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 3,
      numScroll: 3,
    },
    {
      breakpoint: '768px',
      numVisible: 2,
      numScroll: 2,
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1,
    }
  ];

  private router = inject(Router);
  private authService = inject(AuthService);
  private observedCitiesService = inject(ObservedCitiesService);

  ngOnInit(): void {
    const userId = localStorage.getItem('userId');
    if (userId) {
      const numericUserId = Number(userId);

      this.observedCitiesService.getUniqueForecastsForUser(numericUserId).subscribe({
        next: (uniqueForecasts) => {
          this.forecasts = uniqueForecasts;
          console.log('Unique forecasts:', this.forecasts);
        },
        error: (err) => console.error('Error fetching unique forecasts:', err),
      });

      this.observedCitiesService.getMainCity(numericUserId).subscribe({
        next: (response) => {
          this.mainCity = response;
        },
        error: (err) => {
          console.error('Error fetching main city:', err);
        },
      });
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  selectForecast(forecast: Forecast): void {
    this.mainCity = { city: { ...forecast.city, country: (forecast.city as any).country || 'Unknown' }, forecast: forecast };

    
    const userId = localStorage.getItem('userId');
    if (userId) {
      const numericUserId = Number(userId);
      this.observedCitiesService.setMainCity(numericUserId, forecast.city.id).subscribe({
        next: () => {
          localStorage.setItem('mainCityId', forecast.city.id.toString());
          console.log('Main city updated via carousel selection');
        },
        error: (err) => console.error('Error updating main city:', err),
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  onDragStart(event: DragEvent, forecast: Forecast): void {
    event.dataTransfer?.setData('forecast', JSON.stringify(forecast));
  }

  allowDrop(event: DragEvent): void {
    event.preventDefault();
  }

  onDrop(event: DragEvent): void {
    event.preventDefault();
    const forecastData = event.dataTransfer?.getData('forecast');
    if (forecastData) {
      const forecast: Forecast = JSON.parse(forecastData);
      this.mainCity = { city: { ...forecast.city, country: (forecast.city as any).country || 'Unknown' }, forecast: forecast };

      // Aktualizacja głównego miasta na serwerze
      const userId = localStorage.getItem('userId');
      if (userId) {
        const numericUserId = Number(userId);
        this.observedCitiesService.setMainCity(numericUserId, forecast.city.id).subscribe({
          next: () => {
            localStorage.setItem('mainCityId', forecast.city.id.toString());
            console.log('Main city updated via drag & drop');
          },
          error: (err) => console.error('Error updating main city:', err),
        });
      }
    }
  }
}