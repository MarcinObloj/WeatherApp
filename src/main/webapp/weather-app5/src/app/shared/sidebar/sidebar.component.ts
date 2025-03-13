import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';

@Component({
  selector: 'app-sidebar',
  imports: [RouterLink, MatIconModule, CommonModule],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {
  avatarUrl = 'path/to/default/avatar.png';
  isOpen = true;
  constructor(private router: Router, private authService: AuthService) {}
  toggleSidebar(): void {
    this.isOpen = !this.isOpen;
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  changeAvatar(): void {
    this.router.navigate(['/change-avatar']);
    console.log('Change avatar clicked');
  }
}
