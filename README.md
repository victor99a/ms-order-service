# ms-order-service 

**Microservicio de Pedidos**
Se encarga de gestionar:

- `Pedido`
- `DetallePedido`

Incluye operaciones CRUD y cálculo automático de:

- Subtotal por detalle
- IVA por detalle
- Total por detalle
- Total del pedido

Este servicio está pensado para trabajar junto a:

- `ms-catalog-service` → productos y categorías
- `ms-user-service` → usuarios, roles y autenticación (JWT)

---

## 🧱 Tecnologías

- Java 17+
- Spring Boot (Web, Data JPA)
- Gradle
- Base de datos relacional (MySQL 5.7)
- Lombok
- MapStruct

---

## ⚙️ Configuración

En `src/main/resources/application.properties` (o `application.yml`) debes configurar:

```properties
spring.application.name=ms-order-service

# Configuración de base de datos (ejemplo con MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/db_pedidos
spring.datasource.username=root
spring.datasource.password=

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Puerto del microservicio
server.port=8081
