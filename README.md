# 📱 Explore with me
### _Дипломный проект._

##  Что это?
Бэкенд приложения-афиши для публикации и поиска событий.

Свободное время — ценный ресурс. Ежедневно мы планируем, как его потратить — куда и с кем сходить.
Сложнее всего в таком планировании поиск информации и переговоры.
Нужно учесть много деталей: какие намечаются мероприятия, свободны ли в этот момент друзья, как всех пригласить и где собраться.

Приложение состоит из двух микросервисов и двух БД (для каждого из сервисов):
### 💻 Main
Основной сервис, отвечающий за бизнес-логику приложения.
API основного сервиса состоит из трех частей:
* публичная доступна без регистрации любому пользователю сети;
* закрытая доступна только авторизованным пользователям;
* административная — для администраторов сервиса.

### 📜 Stats
Сервер статистики, отвечающий за сохранение и получение статистики просмотров. \
Модуль статистики состоит из трех подмодулей:
1. Основной сервис статистики
2. Библиотека клиента сервиса статистики для отправки и получения статистики
3. Библиотека DTO

_Схема модулей приложения:_
![Схема модулей приложения](https://github.com/FokuZz/java-explore-with-me/blob/main/info/ewm.png)
___

## ⭐ Возможности приложения:
#### Публичный доступ:
* Поиск событий по критериям (дата, категория события, и т.д.)
* Просмотр существующих категорий событий
* Просмотр подборок, созданных администратором

#### Авторизованные пользователи
* Поиск событий
* Добавление и редактирование событий
* Подача заявок на участие в событиях других пользователей
* Просмотр и модерация заявок, поданных на участие в своих событиях

#### Администратор
* Создание, редактирование и удаление пользователей
* Создание, редактирование и удаление категорий
* Создание, редактирование и удаление подборок событий
* Поиск и редактирование событий (включая публикацию)
___
## ⚙️ Технологический стек
* Java 11
* Spring Boot
* Hibernate (main service)
* JDBC (stats service)
* Junit
* Mockito
* Lombok
* PostgresSQL
* Maven
* Docker
___
### 🌐 Описание API приложения:
Описание API сервера [здесь](api).
___
### 📃 Инструкция по запуску:
1. Скачать проект
2. По умолчанию приложение работает на порту 8080 (основной сервис) и 9090 (сервис статистики).
3. Собрать проект:
```shell
docker compose build
```
4. Запустить проект:
```shell
docker compose up
```
___
