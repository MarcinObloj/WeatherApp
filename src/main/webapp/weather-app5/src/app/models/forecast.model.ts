export interface Forecast {
  id: number;
  city: {
    id: number;
    name: string;
    latitude: number;
    longitude: number;
  };
  temperature: number;
  humidity: number;
  timestamp: string;
  feelsLikeTemperature: number;
  mainWeather: string;
  rainChance: string;
  sunset: string;
}