import { Component, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { Router, RouterLink } from '@angular/router';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbarModule } from '@angular/material/toolbar';
import { AuthService } from '../../auth/services/auth.service';

@Component({
  selector: 'app-header',
  imports: [
    RouterLink,
    MatIconModule,
    CommonModule,
    MatButtonModule,
    MatMenuModule,
    MatSidenavModule,
    MatToolbarModule,
  ],
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent {
  currentTheme = signal<string>(localStorage.getItem('theme') || 'system');
  authService = inject(AuthService);
  isLoggedIn$ = this.authService.isLoggedIn;
  isMobileMenuOpen = signal<boolean>(false);

  router = inject(Router);

  constructor() {
    this.applyTheme(this.currentTheme());
  }
  get avatarUrl(){
    return localStorage.getItem('avatarUrl');
  }

  logout(): void {
    this.authService.logout();
    this.isMobileMenuOpen.set(false);
    this.router.navigate(['/']);
  }

  toggleMobileMenu() {
    this.isMobileMenuOpen.set(!this.isMobileMenuOpen());
  }

  setTheme(theme: string): void {
    this.currentTheme.set(theme);
    localStorage.setItem('theme', theme);
    this.applyTheme(theme);
  }

  applyTheme(theme: string): void {
    const htmlElement = document.documentElement;
    if (theme === 'system') {
      const prefersDark = window.matchMedia(
        '(prefers-color-scheme: dark)'
      ).matches;
      htmlElement.classList.toggle('dark', prefersDark);
    } else {
      htmlElement.classList.toggle('dark', theme === 'dark');
    }
  }
}
