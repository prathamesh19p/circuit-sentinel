# Resilience4j Circuit Breaker and Rate Limiter with Spring Boot

 * This project showcases how to integrate **Resilience4j's Circuit Breaker** and **Rate Limiter** patterns into a **Spring Boot** application to create **resilient and fault-tolerant microservices**.

- **Circuit Breaker**: Monitors external service calls and, when failures exceed a defined threshold, temporarily halts requests to prevent cascading failures. Once the cooldown period passes, it gradually tests the service for recovery before fully restoring traffic.

- **Rate Limiter**: Controls the flow of requests to a service within a specific time window. By capping the request rate, it helps prevent resource exhaustion, maintain service performance, and ensure overall system stability.

Together, these patterns protect your application from both sudden failures and performance degradation, enabling smoother operation even under stress or during partial outages.

### 🔧 Key Features

- **🛑 Circuit Breaker** – Fully configurable failure rate threshold, retry interval, and recovery period to protect services from cascading failures.
- **⏳ Rate Limiter** – Dynamic control over the number of requests sent to external services within a defined time window.
- **⚡ Spring Boot Integration** – Seamless setup and configuration using Spring Boot’s properties and auto-configuration.
- **🛡 Fallback Mechanisms** – Graceful degradation with default responses when failures occur or limits are reached.


# Tech Stack:
* Spring Boot 3.x
* Resilience4j 2.x
* Maven
* Java 17

* This repository includes sample code to get started quickly with resilient API integrations.
