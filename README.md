# WeatherApp
WeatherApp to aplikacja webowa, która umożliwia użytkownikom sprawdzanie aktualnej pogody dla wybranych lokalizacji. Projekt składa się z frontendu (Angular, TypeScript) oraz backendu (Spring Boot, MySQL).

## Funkcje
- Pobieranie pogody: Użytkownik może wprowadzić nazwę miasta i otrzymać aktualne dane pogodowe.

- Zapisanie do ulubionych: Użytkownik może zapisywać miasta do ulubionych i śledzić jej dokładną pogodę

- Responsywny design: Aplikacja działa na różnych urządzeniach (komputery, tablety, telefony).

## Autor
Marcin Obłój

## Technologie
### Frontend
<div style="display: flex; gap: 10px; align-items: center;"> <img src="https://img.icons8.com/color/48/000000/angularjs.png" alt="Angular" title="Angular"/> <img src="https://img.icons8.com/color/48/000000/typescript.png" alt="TypeScript" title="TypeScript"/> <img src="https://img.icons8.com/color/48/000000/html-5.png" alt="HTML" title="HTML"/> <img src="https://img.icons8.com/color/48/000000/css3.png" alt="CSS" title="CSS"/> </div>

### Backend
<div style="display: flex; gap: 10px; align-items: center;"> <img src="https://img.icons8.com/color/48/000000/java-coffee-cup-logo.png" alt="Java" title="Java"/> <img src="https://img.icons8.com/color/48/000000/spring-logo.png" alt="Spring Boot" title="Spring Boot"/> <img src="https://img.icons8.com/color/48/000000/mysql-logo.png" alt="MySQL" title="MySQL"/> </div>

## Przegląd aplikacji

### Strona główna:
![Strona_główna](https://i.imgur.com/GaKd5K1.png)
### Logowanie:
![Strona_główna](https://i.imgur.com/pFK94ml.png)

### Pomyślne logowanie:
![Strona_główna](https://i.imgur.com/AcDqxQs.png)

### Rejestracja:
![Strona_główna](https://i.imgur.com/uTt89I9.png)

### Lista miast:
![Strona_główna](https://i.imgur.com/BILx0en.png)

### Obserwowane miasta:
![Strona_główna](https://i.imgur.com/VECGDoc.png)

### Profil:
![Strona_główna](https://i.imgur.com/apd3z1a.png)

### Zmiana avatara:
![Strona_główna](https://i.imgur.com/C0Vx8kF.png)


## Jak uruchomić projekt lokalnie
### Wymagania wstępne
- Java 17 (lub nowsza)

- Node.js (do zarządzania zależnościami frontendu)

- MySQL (lub inna baza danych obsługiwana przez Spring Boot)

- Maven (do zarządzania zależnościami backendu)

- Angular CLI (do uruchomienia frontendu)

### Backend (Spring Boot)
1. Sklonuj repozytorium:

```bash
git clone https://github.com/twoj-uzytkownik/WeatherApp.git
```
2. Przejdź do folderu backendu:

```bash
cd WeatherApp/backend
```
3. Skonfiguruj bazę danych:

Utwórz bazę danych MySQL o nazwie weatherapp.

Zaktualizuj plik application.properties w folderze src/main/resources z danymi do połączenia z bazą danych:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/weatherapp
spring.datasource.username=twoj-uzytkownik
spring.datasource.password=twoje-haslo
```
Uruchom aplikację Spring Boot:
```bash
mvn spring-boot:run
```
### Frontend (Angular)
1. Przejdź do folderu frontendu:

```bash
cd WeatherApp/frontend
```
2. Zainstaluj zależności:

```bash
npm install
```
3. Uruchom aplikację Angular:

```bash
ng serve
```

4. Otwórz przeglądarkę:

Przejdź do adresu http://localhost:4200.
