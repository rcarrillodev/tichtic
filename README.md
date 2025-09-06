# TichTic

TichTic is a URL shortener application with a Spring Boot backend and a simple UI interface.

This project is Work in Progress

## Project Structure

The project consists of two main components:

### Backend (tichtic-backend)

A Spring Boot application that provides:

- URL shortening service
- Redis caching for shortened URLs
- Kafka integration for statistics collection
- RESTful API endpoints for URL management

#### Technology Stack

- Java 24
- Spring Boot
- Spring Data JPA
- Redis for caching
- Apache Kafka for event streaming
- Liquibase for database migrations

#### Key Components

- URL Shortener Service
  - URL creation and management
  - Custom URL mapping
  - URL redirection

- Statistics Collection
  - Kafka integration for metrics
  - URL access tracking
  - Statistics event publishing

- Caching Layer
  - Redis implementation
  - Improved response times
  - Reduced database load

### Frontend (tichtic-ui)

A lightweight web interface that provides:

- URL shortening form
- Display of shortened URLs
- Basic styling and user interaction

#### Technology Stack

- HTML5
- CSS3
- JavaScript

## Configuration

### Backend Configuration

The application uses different configuration profiles:

- `application.yml` - Default configuration
- `application-prd.yml` - Production configuration

## API Documentation

The backend provides the following main endpoints:

- `POST /api/urls` - Create a shortened URL
- `GET /{shortUrl}` - Redirect to original URL

