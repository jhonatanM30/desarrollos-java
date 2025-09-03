# Parametrización Middleware

Servicio Spring Boot para la parametrización automática de tablas de middleware y servicios regionales.

## Características

- **Java 21** con Spring Boot 3.2
- **Oracle Database** con JPA/Hibernate
- **Validaciones** automáticas con Bean Validation
- **MapStruct** para transformación de datos
- **Lombok** para reducir boilerplate
- **Logging** estructurado con SLF4J
- **Generación automática** de archivos SQL para QA y PRD

## Arquitectura

El proyecto sigue una arquitectura por capas:

```
├── controller/     # Controladores REST
├── service/        # Lógica de negocio
├── repository/     # Acceso a datos (JPA)
├── entity/         # Entidades JPA
├── dto/           # DTOs para transferencia de datos
├── mapper/        # Mappers (MapStruct)
└── exception/     # Manejo de excepciones
```

## Endpoints

### Parametrización Middleware
```
POST /api/v1/parametrizacion/middleware
```

**Request Body:**
```json
{
  "carpetaQA": "/ruta/archivos/sql/QA",
  "carpetaPRD": "/ruta/archivos/sql/PRD",
  "parametros": [
    {
      "nombreParametro": "FICBCO0256.TIPOCONSULTADGI",
      "valorParametro": "1",
      "descripcionParametro": "Contiene el tipo de consulta a realizar"
    }
  ]
}
```

### Parametrización Regional
```
POST /api/v1/parametrizacion/regional
```

**Request Body:**
```json
{
  "carpetaQA": "/ruta/archivos/sql/QA",
  "carpetaPRD": "/ruta/archivos/sql/PRD",
  "parametros": [
    {
      "idServicio": "FICBCO0252",
      "origen": "HN01",
      "destino": "HN01",
      "estado": "A"
    }
  ]
}
```

## Validaciones

### Parametrización Regional
- **origen/destino**: Solo acepta valores `HN01`, `NI01`, `PA01`, `GT01`
- **estado**: Solo acepta valores `A` (Activo) o `I` (Inactivo)

### Campos Obligatorios
Todos los campos en ambos tipos de parametrización son obligatorios.

## Funcionalidades

### Procesamiento Individual
- Cada parámetro se procesa individualmente
- Si uno falla, continúa con los siguientes
- Respuesta detallada con éxitos y errores

### Generación de Archivos SQL
- **QA**: Prefijo `MIDDLEWARE2` en nombres de tabla
- **PRD**: Prefijo `MIDDLEWARE` en nombres de tabla
- Archivos generados automáticamente en las carpetas especificadas

### Manejo de Errores
- Validación de duplicados
- Manejo de excepciones detallado
- Logging completo de operaciones

## Configuración

### Base de Datos
```yaml
spring:
  datasource:
    url: jdbc:oracle:thin:@172.23.177.185:1970:mdwdbdev
    username: MIDDLEWARE
    password: desarrollo
```

### Prefijos SQL
```yaml
app:
  sql-generation:
    qa-prefix: MIDDLEWARE2
    prd-prefix: MIDDLEWARE
```

## Ejecución

```bash
mvn spring-boot:run
```

El servicio estará disponible en: `http://localhost:8080/api/v1`

## Health Checks

- Middleware: `GET /api/v1/parametrizacion/middleware/health`
- Regional: `GET /api/v1/parametrizacion/regional/health`

## Respuesta Típica

```json
{
  "exitoso": true,
  "mensaje": "Procesamiento completado exitosamente. 2 parámetros procesados.",
  "totalProcesados": 2,
  "exitosos": 2,
  "fallidos": 0,
  "errores": [],
  "rutaArchivos": "QA: /ruta/qa/archivo.sql, PRD: /ruta/prd/archivo.sql"
}
```
