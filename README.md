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

## Puerto del microservicio
server.port=8081


🌐 Endpoints principales

Base path sugerido: /api/pedidos

1. Crear pedido

POST /api/pedidos

Body (DTO de creación):

{
  "idUsuario": 1001,
  "detalles": [
    {
      "idProducto": 52,
      "nombreProducto": "Polera Nike Pro",
      "precioUnitario": 29990,
      "cantidad": 1
    },
    {
      "idProducto": 102,
      "nombreProducto": "Polera nike",
      "precioUnitario": 36990,
      "cantidad": 2
    }
  ]
}


El servicio:

Setea fechaPedido = now()

Calcula subtotal, iva, totalDetalle por cada detalle

Calcula totalPedido como suma de todos los totalDetalle

Persiste el pedido y sus detalles

Respuesta (ejemplo):

{
  "idPedido": 1,
  "idUsuario": 1001,
  "fechaPedido": "2025-11-18T20:15:25.9858363",
  "totalPedido": 65414.3,
  "detalles": [
    {
      "idProducto": 52,
      "nombreProducto": "Polera Nike Pro",
      "precioUnitario": 29990.0,
      "cantidad": 1,
      "subtotal": 29990.0,
      "iva": 5698.1,
      "totalDetalle": 35688.1
    },
    {
      "idProducto": 102,
      "nombreProducto": "Polera nike",
      "precioUnitario": 36990.0,
      "cantidad": 2,
      "subtotal": 73980.0,
      "iva": 14056.2,
      "totalDetalle": 88036.2
    }
  ]
}


(Los valores son referenciales; el cálculo real depende de la constante de IVA configurada.)

2. Listar todos los pedidos

GET /api/pedidos

Devuelve una lista de PedidoDTO con sus detalles.

3. Obtener un pedido por ID

GET /api/pedidos/{id}

Ejemplo:

GET /api/pedidos/1

4. Actualizar un pedido

PUT /api/pedidos/{id}

Body (PedidoDTO):

{
  "idPedido": 1,
  "idUsuario": 2001,
  "fechaPedido": "2025-11-18T20:15:25.9858363",
  "totalPedido": 0,
  "detalles": [
    {
      "idProducto": 52,
      "nombreProducto": "Polera Nike Pro",
      "precioUnitario": 29990,
      "cantidad": 2
    },
    {
      "idProducto": 102,
      "nombreProducto": "Polera nike",
      "precioUnitario": 36990,
      "cantidad": 1
    }
  ]
}


El servicio:

Busca el pedido existente

Limpia los detalles anteriores

Reconstruye los detalles desde el DTO

Recalcula subtotal, iva, totalDetalle

Recalcula totalPedido

Mantiene la fechaPedido original

5. Eliminar un pedido

DELETE /api/pedidos/{id}

Ejemplo:

DELETE /api/pedidos/1
