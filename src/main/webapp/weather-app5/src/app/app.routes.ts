import { Routes } from '@angular/router';
import { CityChartComponent } from './weather/observed-cities/city-chart/city-chart.component';

export const routes: Routes = [
    { path: '', loadComponent: () => import('./home/home.component').then(m => m.HomeComponent) },
    { path: 'login', loadComponent: () => import('./auth/login/login.component').then(m => m.LoginComponent) },
    { path: 'cities', loadComponent: () => import('./cities/cities.component').then(m => m.CitiesComponent) },
    { path: 'observed-cities', loadComponent: () => import('./weather/observed-cities/observed-cities.component').then(m => m.ObservedCitiesComponent) },
    { path: 'register', loadComponent: () => import('./auth/register/register.component').then(m => m.RegisterComponent) },
    { path: 'city-chart/:id', component: CityChartComponent },
    {path: 'profile', loadComponent: () => import ('./weather/observed-cities/profile/profile.component').then(m => m.ProfileComponent)},
    {path: 'change-avatar', loadComponent: () => import ('./weather/observed-cities/profile/change-avatar/change-avatar.component').then(m => m.AvatarChangeComponent)}
];
