import { Component, ElementRef, ViewChild } from '@angular/core';
import { RouterLink } from '@angular/router';
import { gsap } from 'gsap';
@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  @ViewChild('heroSection') heroSection!: ElementRef;

  ngAfterViewInit() {
    setTimeout(() => {
      gsap.to(this.heroSection.nativeElement, {
        opacity: 1,
        y: 0,
        duration: 1.2,
        ease: 'power3.out',
      });
    }, 600);
  }
}
