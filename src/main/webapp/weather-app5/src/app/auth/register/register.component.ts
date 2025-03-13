import { Component, signal } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import {
  FormBuilder,
  FormGroup,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { Router } from '@angular/router';
import { ModalComponent } from "../../shared/modal/modal.component";
@Component({
  selector: 'app-register',
  standalone: true,
  imports: [
    FormsModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    CommonModule,
    ModalComponent
],
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  registerForm: FormGroup;
  showModal = signal(false);
  modalTitle = signal('');
  modalMessage = signal('');

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  ngOnInit(): void {}

  onRegister(): void {
    if (this.registerForm.valid) {
      const { username, email, password } = this.registerForm.value;
      this.authService.register(username, password, email).subscribe({
        next: () => {
          this.modalTitle.set('Registration Successful');
          this.modalMessage.set('You have successfully registered!');
          this.showModal.set(true);
        },
        error: (err) => {
          this.modalTitle.set('Registration Failed');
          this.modalMessage.set(`Error: ${err.message}`);
          this.showModal.set(true);
        },
      });
    }
  }

  closeModal(): void {
    this.showModal.set(false);
    this.router.navigate(['/login']);
  }
}
