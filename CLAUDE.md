# Правила для Claude

## Язык общения
- Всегда отвечай на **русском языке**, независимо от того, на каком языке написан код или документация.
- Комментарии в коде — на английском (стандарт Java/Spring).
- Имена классов, методов, переменных — на английском (стандарт Java/Spring).

## Стиль ответов
- Отвечай коротко и по делу.
- Не пересказывай что сделал — пользователь сам видит диф.
- Не добавляй длинные итоговые списки в конце каждого ответа.

## Проект OneQiQ
- Проект: микросервисная платформа для тестирования и квизов.
- Стек: Java 21, Spring Boot 3.4.1, Spring Cloud 2024.0.0, PostgreSQL, Kafka, Redis, Elasticsearch.
- Архитектура: Hexagonal (Ports & Adapters) + CQRS.
- Maven multi-module монорепо: один parent pom.xml, 16 модулей (common + 15 сервисов).
- Структура каждого сервиса: `domain/` → `application/` → `adapter/in/web`, `adapter/out/persistence`, `adapter/out/messaging` → `bootstrap/`.
- JPA-аннотации прямо на доменных сущностях (pragmatic hexagonal).
- UUID v4 (randomUUID) — UUID v7 добавим позже.
- JWT: HS256 через JJWT 0.12.6.
- Пароли: BCrypt.
- Коды верификации: 6 цифр, хранятся как SHA-256 хэш.

## Порядок разработки сервисов
1. ✅ identity (порт 8081)
2. profile (порт 8082)
3. relationship (порт 8083)
4. catalog (порт 8084)
5. library (порт 8085)
6. search (порт 8086)
7. training (порт 8087)
8. assignment (порт 8088)
9. attempt (порт 8089)
10. history (порт 8090)
11. statistics (порт 8091)
12. notification (порт 8092)
13. mail (порт 8093)
14. media (порт 8094)
15. api-gateway (порт 8080)
