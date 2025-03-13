import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Forecast } from '../../models/forecast.model';
import { City } from '../../models/city.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ObservedCitiesService {
  private apiUrl = `http://localhost:8080/api/weather`;
  private http = inject(HttpClient);
  private getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('token');
    if (!token) {
      throw new Error('User not logged in');
    }
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }
  getForecastsForUser(userId: number): Observable<Forecast[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Forecast[]>(`${this.apiUrl}/forecast/${userId}`, {
      headers,
    });
  }
  getHistoricalDataForCity(cityId: number): Observable<Forecast[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<Forecast[]>(`${this.apiUrl}/historical/${cityId}`, {
      headers,
    });
  }
  setMainCity(userId: number, cityId: number): Observable<any> {
    const headers = this.getAuthHeaders();
    const city = { id: cityId };
    return this.http.post(`${this.apiUrl}/${userId}/main-city`, city, {
      headers,
      responseType: 'text',
    });
  }
  getMainCity(userId: number): Observable<{ city: City; forecast: Forecast }> {
    const headers = this.getAuthHeaders();
    return this.http.get<{ city: City; forecast: Forecast }>(
      `${this.apiUrl}/${userId}/main-city`,
      { headers }
    );
  }
  getUniqueForecastsForUser(userId: number): Observable<Forecast[]> {
    return this.getForecastsForUser(userId).pipe(
      map(forecasts => {
        const reversed = [...forecasts].reverse();
        return reversed.filter((f, index, self) =>
          index === self.findIndex(f2 => f2.city.id === f.city.id)
        );
      })
    );
  }
  
}
