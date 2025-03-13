import { Component } from '@angular/core';
import { CarouselModule } from 'primeng/carousel';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button'; // Jeśli używasz przycisków mat
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-avatar',
  standalone: true,
  imports: [CommonModule, CarouselModule, MatButtonModule, ButtonModule], // Importujemy wymagane moduły
  templateUrl: './change-avatar.component.html',
  styleUrls: ['./change-avatar.component.css'],
})
export class AvatarChangeComponent {
  avatars = [
    'avatar (1).png',
    '1.png',
    'avatar (2).png',
    'avatar (3).png',
    'avatar (4).png',
    'avatar (5).png',
    'avatar (6).png',
    'avatar (7).png',
    'avatar (8).png',
    'avatar (9).png',
    'avatar (10).png',
    'avatar (11).png',
    'avatar (12).png',
    'avatar (13).png',
    'avatar (14).png',
    'avatar (15).png',
    
  ];
  selectedAvatar: string = this.avatars[0]; 

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 3,
      numScroll: 3
    },
    {
      breakpoint: '768px',
      numVisible: 2,
      numScroll: 2
    },
    {
      breakpoint: '560px',
      numVisible: 1,
      numScroll: 1
    }
  ];

  constructor(private router: Router) {}

  selectAvatar(avatar: string): void {
    this.selectedAvatar = avatar;
  }

  saveAvatar(): void {
    localStorage.setItem('avatarUrl', this.selectedAvatar);
    this.router.navigate(['/profile']);
  }

  cancel(): void {
    this.router.navigate(['/profile']);
  }
}
