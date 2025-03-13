// filepath: /C:/Users/User/Desktop/htdocs/weather-app5/src/app/pipes/city-filter.pipe.ts
import { Pipe, PipeTransform } from '@angular/core';
import { Forecast } from './models/forecast.model';
import { City } from './models/city.model';

@Pipe({
  name: 'cityFilter',
})
export class CityFilterPipe implements PipeTransform {
  transform(cities: City[], searchText: string): City[] {
    if (!cities || !searchText) {
      return cities;
    }
    return cities.filter((city) =>
      city.name.toLowerCase().includes(searchText.toLowerCase())
    );
  }
}
