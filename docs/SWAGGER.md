# Documentación de la API con OpenAPI / Swagger

La API está documentada con **OpenAPI 3** usando **springdoc-openapi 3.2.0** (versión compatible con Spring Boot 4.1).
springdoc lee los controllers y DTOs y genera la documentación automáticamente.

| Qué | URL |
|---|---|
| Swagger UI (página interactiva) | http://localhost:8080/swagger-ui.html |
| Especificación OpenAPI (JSON) | http://localhost:8080/v3/api-docs |

## Cómo ejecutarlo

1. Encender **MySQL** (base de datos `restaurante_db`).
2. Si acabas de hacer `git pull`, recargar Maven en IntelliJ (`Ctrl+Shift+O`) para descargar springdoc.
3. Ejecutar `RestauranteSpringApplication` (botón ▶ en IntelliJ) y esperar el mensaje `Started RestauranteSpringApplication`.
4. Abrir http://localhost:8080/swagger-ui.html en el navegador.

## Cómo probar endpoints protegidos (JWT)

Casi todos los endpoints necesitan un token. Sin token responden **401**.

1. Abrir `POST /api/v1/auth/login` → **Try it out** → pegar el body → **Execute**:
   ```json
   { "correo": "cliente@test.com", "password": "12345678" }
   ```
2. Copiar el valor de `token` de la respuesta (sin comillas).
3. Botón **Authorize** (arriba a la derecha) → pegar el token (sin la palabra `Bearer`) → **Authorize** → **Close**.
4. Desde ahora Swagger envía `Authorization: Bearer <token>` en todas las peticiones (se ve en el **Curl** de cada respuesta).

El token dura 24 horas. Si un endpoint vuelve a responder 401, repetir el login.

## Qué se configuró

| Archivo | Cambio |
|---|---|
| `pom.xml` | Dependencia `springdoc-openapi-starter-webmvc-ui` 3.2.0 |
| `config/OpenApiConfig.java` | Título, versión y descripción de la API, y el esquema de seguridad Bearer JWT (botón **Authorize**) |
| `security/SecurityConfig.java` | Rutas públicas: `/swagger-ui.html`, `/swagger-ui/**` y `/v3/api-docs/**`. Los endpoints siguen protegidos |
| `PedidoController` y `RestauranteController` | `@Operation` y `@ApiResponses` en HU-9, HU-15, HU-16 y la búsqueda binaria |

Los demás endpoints aparecen con la documentación automática (URL, parámetros y JSON de entrada y salida).
Para describir un endpoint, agregar encima del método:

```java
@Operation(summary = "HU-X: Título corto", description = "Qué hace, quién lo usa y sus reglas")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "Caso exitoso"),
        @ApiResponse(responseCode = "400", description = "Cuándo falla por reglas de negocio"),
        @ApiResponse(responseCode = "401", description = "Falta el token JWT o es inválido")
})
```

## Guion de la demostración (unos 3 minutos)

1. **Problema:** sin documentación, otros equipos tienen que leer el código para saber cómo usar la API.
2. **`/v3/api-docs`:** el JSON del estándar OpenAPI que springdoc genera solo a partir del código.
3. **Swagger UI:** título y descripción (`OpenApiConfig`), endpoints agrupados por controller, y un endpoint documentado (HU-15) con sus respuestas 200, 400 y 401.
4. **Seguridad en vivo:** `GET /restaurante/buscar?nombre=mi` sin token → **401**; login → **Authorize** → repetir → **200**, y mostrar el header `Authorization: Bearer` en el Curl.
5. **Cierre:** versión 3.2.0 porque es la compatible con Spring Boot 4.1 (la 2.x no arranca).

## Problemas comunes

| Síntoma | Causa | Solución |
|---|---|---|
| `swagger-ui.html` responde 401 | Faltan las rutas públicas en `SecurityConfig` | Revisar el `permitAll()` de Swagger |
| Imports de `io.swagger...` en rojo | Maven no descargó la dependencia | `Ctrl+Shift+O` (Load Maven Changes) |
| El proyecto no arranca tras agregar springdoc | Versión incompatible (2.x) | Usar 3.2.0 con Spring Boot 4.1 |
| Un endpoint responde 401 después de Authorize | El token expiró o se copió con comillas | Hacer login de nuevo y pegar solo el token |
| `ECONNREFUSED` o la página no carga | El proyecto no está corriendo | Ejecutar `RestauranteSpringApplication` |
