// filepath: /C:/Users/User/Desktop/htdocs/weather-app5/src/app/weather/cities.service.ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { City } from '../../models/city.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CitiesService {
  private url = `http://localhost:8080/api/users`;
  private http = inject(HttpClient);

  private getAuthHeaders(): HttpHeaders {
    const token = sessionStorage.getItem('token');
    if (!token) {
      throw new Error('User not logged in');
    }
    return new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  getAllCities(userId: string): Observable<City[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<City[]>(`${this.url}/${userId}/cities`, { headers });
  }

  addCityToObserved(userId: string, cityId: string): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post(
      `${this.url}/${userId}/cities/${cityId}/observe`,
      {},
      { headers }
    );
  }

  getObservedCities(userId: string): Observable<City[]> {
    const headers = this.getAuthHeaders();
    return this.http.get<City[]>(`${this.url}/${userId}/observed-cities`, {
      headers,
    });
  }
}
