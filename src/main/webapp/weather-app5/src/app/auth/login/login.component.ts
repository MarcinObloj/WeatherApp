import { Component, inject, signal } from '@angular/core';
import { AuthService } from '../services/auth.service';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { AuthInterceptor } from '../services/auth.interceptor';
import { Router } from '@angular/router';
import { ModalComponent } from "../../shared/modal/modal.component";
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    ModalComponent,
    CommonModule
],
  templateUrl: './login.component.html',
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  loginForm: FormGroup;
  showModal = signal(false);
  modalTitle = signal('');
  modalMessage = signal('');
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit(): void {}

  onLogin(): void {
    if (this.loginForm.valid) {
      const { username, password } = this.loginForm.value;
      this.authService.login(username, password).subscribe({
        next: () => {
          this.modalTitle.set('Login Successful');
          this.modalMessage.set('You have been logged in successfully.');
          
          this.showModal.set(true);

        },
        error: (err) => {
          this.modalTitle.set('Login Failed');
          this.modalMessage.set(err.message);
          this.showModal.set(true);
        },
      });
    }
  }
  closeModal(): void {
    this.showModal.set(false);
    this.router.navigate(['/']);
  }
}
