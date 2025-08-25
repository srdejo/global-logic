# Technical Test

![Java](https://img.shields.io/badge/Java-11-red)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.5.14-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-7.4-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

Aplicación desarrollada en **Spring Boot 2.5.14** con **Java 11**, que implementa autenticación con **JWT** y persistencia en base de datos **H2 en memoria**.  
El proyecto utiliza **Gradle 7.4** como sistema de construcción.  

## 📦 Tecnologías usadas

- [Spring Boot 2.5.14](https://spring.io/projects/spring-boot)  
- [Java 11](https://adoptium.net/)  
- [Gradle 7.4](https://gradle.org/)  
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)  
- [Spring Security + OAuth2 Resource Server](https://spring.io/projects/spring-security)  
- [JSON Web Token (jjwt)](https://github.com/jwtk/jjwt)  
- [H2 Database](https://www.h2database.com/)  
- [Lombok](https://projectlombok.org/)  

## 🚀 Requisitos previos

- **Java 11**  
- **Gradle 7.4** (aunque puedes usar el wrapper incluido `./gradlew`)  

Verifica versiones instaladas:
```bash
java -version
gradle -version
```

## ▶️ Cómo ejecutar la aplicación

1. Clona el repositorio:
   ```bash
   git clone https://github.com/srdejo/global-logic.git
   cd technical-test
   ```

2. Compila y ejecuta la aplicación:
   ```bash
   ./gradlew bootRun
   ```
   *(en Windows usa `gradlew.bat bootRun`)*

3. La aplicación arrancará en:
   ```
   http://localhost:8080
   ```

## 🗄️ Base de datos H2

La app usa **H2 en memoria**, accesible en:
```
http://localhost:8080/h2-console
```

Configuración por defecto:
- **JDBC URL**: `jdbc:h2:mem:testdb`  
- **Usuario**: `sa`  
- **Password**: *(vacío)*  

## 🔑 Endpoints principales

### Registro de usuario
```http
POST /user/sign-up
Content-Type: application/json

{
    "name": "Daniel",
    "email": "srdejo@gmail.com",
    "password": "a2asfGfdfdf4",
    "phones": [
        {
            "number": 3127655754,
            "citycode": 57,
            "contrycode": "+57"
        }
    ]
}
```

### Login con token
```http
GET /login
Authorization: Bearer <token>
```

### Respuestas esperadas
- **201 Created** → Usuario registrado correctamente.  
- **400 Bad Request** → Usuario ya existe o datos inválidos.  
- **200 OK** → Login exitoso devuelve información del usuario.  

## 🧪 Ejecutar tests

```bash
./gradlew test
```

## 📜 Licencia

Este proyecto está bajo la licencia MIT - ver el archivo [LICENSE](LICENSE) para más detalles.
