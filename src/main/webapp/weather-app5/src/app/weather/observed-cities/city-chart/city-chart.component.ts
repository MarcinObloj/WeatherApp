import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {
  Chart,
  ChartDataset,
  ChartOptions,
  ChartConfiguration,
  ChartType,
  ChartData,
} from 'chart.js';
import { BaseChartDirective } from 'ng2-charts';
import { ObservedCitiesService } from '../../services/observed-cities.service';

@Component({
  selector: 'app-city-chart',
  imports: [BaseChartDirective],
  templateUrl: './city-chart.component.html',
  styleUrls: ['./city-chart.component.css'],
})
export class CityChartComponent implements OnInit {
  cityId = signal<number | null>(null);

  lineChartLabels: string[] = [];
  lineChartType: string = 'line';
  lineChartPlugins = [];
  lineChartData: ChartData<'line'> = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'Temperature',
      },
      {
        data: [],
        label: 'Humidity',
      },
    ],
  };
  lineChartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      x: {
        ticks: {
          color: '#3498db',
          autoSkip: true,
          maxTicksLimit: 10,
        },
      },
      y: {
        beginAtZero: true,
        ticks: {
          color: '#3498db',
        },
      },
    },
    plugins: {
      legend: {
        display: true,
        position: 'top' as const,
        labels: {
          color: '#3498db',
          font: {
            size: 18,
          },
        },
      },
    },
  };

  lineChartLegend = true;

  private route = inject(ActivatedRoute);
  private observedCitiesService = inject(ObservedCitiesService);
  ngOnInit(): void {
    const cityIdParam = this.route.snapshot.paramMap.get('id');
    if (cityIdParam) {
      this.cityId.set(+cityIdParam);
      this.loadChartData();
    }
  }

  loadChartData(): void {
    if (this.cityId()) {
      this.observedCitiesService
        .getHistoricalDataForCity(this.cityId()!)
        .subscribe((data) => {
          const filteredData = data.filter((_, index) => index % 5 == 0);
          const temperatures = filteredData.map(
            (forecast) => forecast.temperature
          );
          const humidities = filteredData.map((forecast) => forecast.humidity);
          const labels = filteredData.map((forecast) =>
            this.formatTimestamp(forecast.timestamp)
          );

          this.lineChartData = {
            labels: labels,
            datasets: [
              { data: temperatures, label: 'Temperature' },
              { data: humidities, label: 'Humidity' },
            ],
          };
        });
    }
  }

  formatTimestamp(timestamp: string): string {
    const date = new Date(timestamp);
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${month}-${day} ${hours}:${minutes}`;
  }
}
