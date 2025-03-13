import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { tap } from 'rxjs/operators';


@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users';
  private http = inject(HttpClient);
  private loggedIn = new BehaviorSubject<boolean>(this.hasToken());

  register(username: string, password: string, email: string): Observable<any> {
    const body = { username, password, email };
    return this.http.post(`${this.apiUrl}/register`, body);
  }
  private hasToken(): boolean {
    return !!sessionStorage.getItem('token');
  }
  get isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }
  login(username: string, password: string): Observable<any> {
    const body = { username, password };
    return this.http.post(`${this.apiUrl}/login`, body).pipe(
      tap((response: any) => {
        console.log('Login response:', response);
        const token = response.token;
        const userId = response.userId;
        if (token && userId) {
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('userId', userId);
          this.loggedIn.next(true);
        } else {
          console.error('Token is undefined');
        }
      })
    );
  }
  logout(): void {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('userId');
    this.loggedIn.next(false);
  }

  getToken(): string | null {
    return sessionStorage.getItem('token');
  }
}
