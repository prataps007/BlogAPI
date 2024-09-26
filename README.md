# Blogging API

This project is a RESTful API for a blogging platform where users can create, update, and manage blog posts, comments, and more.  
The API is built using Java and Spring Boot and provides secure access using JWT Authentication.  
It also features rate limiting, caching, activity logging, and real-time notifications using WebSockets.

## Features

- **User Management**: Register, update, and delete users.
- **Authentication**: JWT-based authentication system for securing API endpoints.
- **Blog Posts**: Create, update, delete, and view blog posts.
- **Comments**: Users can comment on posts, and comments can be updated or deleted.
- **Followers System**: Users can follow each other and get updates in their feeds.
- **Rate Limiting**: Limits the number of requests a user can make in a given time.
- **Caching**: Redis is used to cache frequently accessed data for performance optimization.
- **WebSockets**: Real-time notifications and live feed updates for user activity.
- **Drafts and Scheduling**: Users can save drafts of blog posts and schedule them for future publishing.
- **Content Moderation**: A machine learning-based system moderates content for inappropriate language.
- **Analytics**: Tracks user activity and popular posts for insights and engagement.

## Technologies

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security (with JWT)**
- **Spring Data JPA**
- **MongoDB (configurable)**
- **Redis (for caching)**
- **Spring WebSockets (for real-time communication)**
- **Machine Learning Integration** (TensorFlow or custom model for moderation)
- **Swagger** (for API documentation)

