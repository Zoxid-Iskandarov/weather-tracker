# 🌦️ Weather Tracker - Веб-приложение для просмотра погоды

[![Java](https://img.shields.io/badge/Java-21%2B-blue)](https://www.java.com/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.3-green)](https://spring.io/projects/spring-boot)
[![Bootstrap](https://img.shields.io/badge/Bootstrap-5.3.0-purple)](https://getbootstrap.com/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue)](https://www.postgresql.org/)

Веб-приложение для просмотра текущей погоды.  
Пользователь может зарегистрироваться и добавить в коллекцию одну или несколько локаций (городов, сёл, других пунктов), после чего главная страница приложения отображает список локаций с их текущей погодой.

---

## 🛠 Технологии и зависимости

### Backend
- Java 21
- Spring Boot 3.5
- Spring Security
- Spring Data JPA
- Spring Validation
- Spring Web
- Spring WebFlux (для API вызовов)
- Thymeleaf + Thymeleaf Extras Spring Security
- PostgreSQL
- Liquibase
- Lombok

### Frontend
- Thymeleaf
- Bootstrap 5.3
- OpenWeatherMap API (для иконок и данных)

### Тестирование
- JUnit 5
- Spring Boot Test
- Spring Security Test

---

## 📌 Основные страницы

### 🏠 Главная страница
- Список сохранённых локаций пользователя
- Отображение текущей погоды, температуры, влажности и описания
- Возможность удалить локацию из списка
- **Путь:** `/`

<div align="center">
  <img src="docs/screenshots/index.png" width="700" alt="Главная страница">
</div>

---

### 🔍 Страница поиска
- Ввод названия локации
- Вывод найденных локаций с координатами, страной и регионом
- Возможность добавить локацию в избранное
- **Путь:** `/search`

<div align="center">
  <img src="docs/screenshots/search-results.png" width="700" alt="Страница поиска">
</div>

---

### 🔐 Аутентификация
#### Регистрация
- Создание нового аккаунта с проверкой ошибок (например, занятый email или слабый пароль)
- **Путь:** `/sign-up`

<div align="center">
  <img src="docs/screenshots/sign-up.png" width="700" alt="Страница регистрации">
</div>

#### Вход
- Вход по email и паролю
- Ссылка на регистрацию
- **Путь:** `/sign-in`

<div align="center">
  <img src="docs/screenshots/sign-in.png" width="700" alt="Страница входа">
</div>
