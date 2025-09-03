# API Documentation - Parametrización Middleware

## Descripción General

Esta API proporciona operaciones CRUD completas para la parametrización de middleware y servicios regionales, con generación automática de archivos SQL siguiendo la convención: `<OPERACION>_<ENTORNO>_<NOMBRETABLA>.sql`

## Endpoints Disponibles

### Parametrización Middleware

#### 1. Crear Parametrización (INSERT)
**POST** `/parametrizacion/middleware`

```json
{
  "carpetaQA": "C:/sql/qa",
  "carpetaPRD": "C:/sql/prd",
  "parametros": [
    {
      "nombreParametro": "TIMEOUT_CONEXION",
      "valorParametro": "30000",
      "descripcionParametro": "Tiempo de espera para conexiones en milisegundos"
    },
    {
      "nombreParametro": "MAX_REINTENTOS",
      "valorParametro": "3",
      "descripcionParametro": "Número máximo de reintentos para operaciones fallidas"
    }
  ]
}
```

**Archivos generados:**
- `INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)

#### 2. Actualizar Parametrización (UPDATE)
**PUT** `/parametrizacion/middleware`

```json
{
  "carpetaQA": "C:/sql/qa",
  "carpetaPRD": "C:/sql/prd",
  "parametros": [
    {
      "nombreParametro": "TIMEOUT_CONEXION",
      "valorParametro": "45000",
      "descripcionParametro": "Tiempo de espera actualizado para conexiones"
    },
    {
      "nombreParametro": "MAX_REINTENTOS",
      "valorParametro": "5"
    }
  ]
}
```

**Nota:** Solo se actualizan los campos que no son `null`. El `nombreParametro` es obligatorio como clave primaria.

**Archivos generados:**
- `UPDATE_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `UPDATE_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)

#### 3. Eliminar Parametrización (DELETE)
**DELETE** `/parametrizacion/middleware`

```json
{
  "carpetaQA": "C:/sql/qa",
  "carpetaPRD": "C:/sql/prd",
  "clavesPrimarias": [
    "TIMEOUT_CONEXION",
    "MAX_REINTENTOS"
  ]
}
```

**Archivos generados:**
- `DELETE_FROM_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `DELETE_FROM_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)

#### 4. Consultar Parametrización (SELECT)
**POST** `/parametrizacion/middleware/consultar`

```json
{
  "criteriosBusqueda": [
    "TIMEOUT_CONEXION",
    "MAX_REINTENTOS",
    "URL_SERVICIO"
  ]
}
```

**Respuesta:**
```json
[
  {
    "nombreParametro": "TIMEOUT_CONEXION",
    "valorParametro": "45000",
    "descripcionParametro": "Tiempo de espera actualizado para conexiones"
  },
  {
    "nombreParametro": "MAX_REINTENTOS",
    "valorParametro": "5",
    "descripcionParametro": "Número máximo de reintentos para operaciones fallidas"
  }
]
```

### Parametrización Regional

#### 1. Crear Servicios Regionales (INSERT)
**POST** `/parametrizacion/regional`

```json
{
  "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
    {
      "idServicio": "SERV002",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "ConsultaCliente",
      "version": "V3",
      "estado": "A",
      "logActivado": "S",
      "ubicacion": "/ruta/servicio"
    }
  ]
}


{
   "exitoso": true,
   "mensaje": "OperaciÃ³n completada exitosamente. 1 registros procesados.",
   "totalProcesados": 1,
   "exitosos": 1,
   "fallidos": 0,
   "errores": [],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_REGIONAL.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_REGIONAL.sql"
}
```

**Archivos generados:**
- `INSERT_INTO_MIDDLEWARE2_MW_SERVICIOS_REGIONALES.sql` (QA)
- `INSERT_INTO_MIDDLEWARE_MW_SERVICIOS_REGIONALES.sql` (PRD)

#### 2. Actualizar Servicios Regionales (UPDATE)
**PUT** `/parametrizacion/regional`

```json
{
  "carpetaQA": "C:/sql/qa",
  "carpetaPRD": "C:/sql/prd",
  "parametros": [
    {
      "idServicio": "SRV001",
      "origen": "HN",
      "destino": "NI",
      "estado": "I"
    },
    {
      "idServicio": "SRV002",
      "estado": "I"
    }
  ]
}
```

**Archivos generados:**
- `UPDATE_MIDDLEWARE2_MW_SERVICIOS_REGIONALES.sql` (QA)
- `UPDATE_MIDDLEWARE_MW_SERVICIOS_REGIONALES.sql` (PRD)

#### 3. Eliminar Servicios Regionales (DELETE)
**DELETE** `/parametrizacion/regional`

```json
{
  "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
  {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "CONSULTA_SALDO",
      "version": "V2",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
   },
      {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "CONSULTA_SALDO",
      "version": "V3",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
   },
    {
      "idServicio": "SERV002",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "ConsultaCliente",
      "version": "V3"      
    },
    {
      "idServicio": "SERV005",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "ConsultaCliente",
      "version": "V3"      
    }
  ]
  
  }
  
  {
   "exitoso": false,
   "mensaje": "OperaciÃ³n completada con errores. 3 exitosos, 1 fallidos.",
   "totalProcesados": 4,
   "exitosos": 3,
   "fallidos": 1,
   "errores": [   {
      "tipo": "REGIONAL",
      "parametro": "SERV005, HN01, HN01, ConsultaCliente, V3",
      "error": "REGISTRO_NO_ENCONTRADO",
      "detalle": "El registro no existe en la base de datos con los valores clave: SERV005, HN01, HN01, ConsultaCliente, V3",
      "valoresParametro": null,
      "insertFallido": null
   }],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\DELETE_FROM_MIDDLEWARE2_PARAMETRIZACION_REGIONAL.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\DELETE_FROM_MIDDLEWARE_PARAMETRIZACION_REGIONAL.sql"
}
```

**Archivos generados:**
- `DELETE_FROM_MIDDLEWARE2_MW_SERVICIOS_REGIONALES.sql` (QA)
- `DELETE_FROM_MIDDLEWARE_MW_SERVICIOS_REGIONALES.sql` (PRD)

#### 4. Consultar Servicios Regionales (SELECT)
**POST** `/parametrizacion/regional/consultar`

```json
{
  "criteriosBusqueda": [
    "SRV001",
    "SRV002"
  ]
}
```

**Respuesta:**
```json
[
      {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "CONSULTA_SALDO",
      "version": "V2",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
   },
      {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "CONSULTA_SALDO",
      "version": "V3",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
   },
      {
      "idServicio": "SERV002",
      "origen": "HN01",
      "destino": "HN01",
      "operacion": "ConsultaCliente",
      "version": "V3",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
   }
]
```

## Respuestas de Operaciones CRUD

### Respuesta Exitosa (INSERT/UPDATE/DELETE)
```json
{
  "exitoso": true,
  "mensaje": "Operación completada exitosamente. 2 registros procesados.",
  "totalProcesados": 2,
  "exitosos": 2,
  "fallidos": 0,
  "errores": [],
  "archivoSqlQa": "C:/sql/qa/INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql",
  "archivoSqlPrd": "C:/sql/prd/INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql"
}
```

### Respuesta con Errores
```json
{
  "exitoso": false,
  "mensaje": "Operación completada con errores. 1 exitosos, 1 fallidos.",
  "totalProcesados": 2,
  "exitosos": 1,
  "fallidos": 1,
  "errores": [
    {
      "tipo": "MIDDLEWARE",
      "parametro": "TIMEOUT_CONEXION",
      "error": "DUPLICADO",
      "detalle": "El parámetro ya existe en la base de datos",
      "valoresParametro": {
        "NOMBRE_PARAMETRO": "TIMEOUT_CONEXION",
        "VALOR_PARAMETRO": "30000",
        "DESCRIPCION_PARAMETRO": "Tiempo de espera para conexiones"
      },
      "insertFallido": "INSERT INTO PARAMETRIZACION_MIDDLEWARE (NOMBRE_PARAMETRO, VALOR_PARAMETRO, DESCRIPCION_PARAMETRO) VALUES ('TIMEOUT_CONEXION', '30000', 'Tiempo de espera para conexiones');"
    }
  ],
  "archivoSqlQa": "C:/sql/qa/INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql",
  "archivoSqlPrd": "C:/sql/prd/INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql"
}
```

## Convención de Nombres de Archivos SQL

### Formato
`<OPERACION>_<ENTORNO>_<NOMBRETABLA>.sql`

### Operaciones
- `INSERT_INTO`: Para operaciones de inserción
- `UPDATE`: Para operaciones de actualización
- `DELETE_FROM`: Para operaciones de eliminación

### Entornos
- `MIDDLEWARE2`: Para ambiente QA
- `MIDDLEWARE`: Para ambiente PRD

### Tablas
- `PARAMETRIZACION_MIDDLEWARE`: Para parametrización middleware
- `MW_SERVICIOS_REGIONALES`: Para servicios regionales

### Ejemplos de Nombres de Archivos
- `INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql`
- `UPDATE_MIDDLEWARE_MW_SERVICIOS_REGIONALES.sql`
- `DELETE_FROM_MIDDLEWARE2_MW_SERVICIOS_REGIONALES.sql`

## Códigos de Error

### Tipos de Error
- **DUPLICADO**: El registro ya existe en la base de datos
- **NO_ENCONTRADO**: El registro no existe para actualizar/eliminar
- **SIN_CAMBIOS**: No se especificaron campos para actualizar
- **ERROR_PROCESAMIENTO**: Error general durante el procesamiento
- **ERROR_ARCHIVO**: Error al generar archivos SQL

### Tipos de Operación
- **MIDDLEWARE**: Errores en parametrización middleware
- **REGIONAL**: Errores en servicios regionales
- **SISTEMA**: Errores del sistema (generación de archivos, etc.)

## Notas Importantes

1. **Actualización Dinámica**: En las operaciones UPDATE, solo se actualizan los campos que no son `null`
2. **Clave Primaria**: 
   - Middleware: `nombreParametro`
   - Regional: `idServicio`
3. **Consultas**: No generan archivos SQL, solo retornan los datos
4. **Transaccionalidad**: Todas las operaciones son transaccionales
5. **Validaciones**: Se valida existencia antes de UPDATE/DELETE
6. **Archivos SQL**: Se generan automáticamente con prefijos de esquema según el entorno

# Documentación de la API de Parametrización de Servicios Regionales

## Tabla de Contenidos
- [Introducción](#introducción)
- [Estructura de Datos](#estructura-de-datos)
- [Endpoints](#endpoints)
  - [Consultar Servicios Regionales](#consultar-servicios-regionales)
  - [Crear Servicios Regionales](#crear-servicios-regionales)
  - [Actualizar Servicios Regionales](#actualizar-servicios-regionales)
  - [Eliminar Servicios Regionales](#eliminar-servicios-regionales)
- [Manejo de Errores](#manejo-de-errores)
- [Ejemplos de Uso](#ejemplos-de-uso)
- [Escenarios de Prueba](#escenarios-de-prueba)

## Introducción

Este documento describe la API para la gestión de parámetros de servicios regionales del middleware. La API permite realizar operaciones CRUD sobre los servicios regionales, generando automáticamente scripts SQL para los entornos de QA y producción.

## Estructura de Datos

### MwServiciosRegionalesDto
```json
{
  "idServicio": "string (15)",
  "origen": "string (10)",
  "destino": "string (10)",
  "operacion": "string (60)",
  "version": "string (3)",
  "estado": "string (1)",
  "logActivado": "string (1)",
  "ubicacion": "string (200)"
}
```

### MwServiciosRegionalesUpdateDto
```json
{
  "idServicio": "string (15)",
  "origen": "string (10)",
  "destino": "string (10)",
  "operacion": "string (60)",
  "version": "string (3)",
  "nuevoEstado": "string (1)",
  "nuevoLogActivado": "string (1)",
  "nuevaUbicacion": "string (200)",
  "nuevaOperacion": "string (60)",
  "nuevaVersion": "string (3)"
}
```

## Endpoints

### Consultar Servicios Regionales

```
GET /api/parametrizacion/regional
```

**Parámetros de Consulta:**
- `criteriosBusqueda`: Lista de criterios de búsqueda (opcional)
  - `idServicio`: Filtra por ID de servicio

**Respuesta Exitosa (200 OK):**
```json
[
  {
    "idServicio": "SERV001",
    "origen": "ORIGEN1",
    "destino": "DEST1",
    "operacion": "CONSULTA_SALDO",
    "version": "1.0",
    "estado": "A",
    "logActivado": "S",
    "ubicacion": "/ruta/servicio"
  }
]
```

### Crear Servicios Regionales

```
POST /api/parametrizacion/regional
```

**Cuerpo de la Solicitud:**
```json
{
  "carpetaQA": "/ruta/qa",
  "carpetaPRD": "/ruta/prd",
  "parametros": [
    {
      "idServicio": "SERV001",
      "origen": "ORIGEN1",
      "destino": "DEST1",
      "operacion": "CONSULTA_SALDO",
      "version": "1.0",
      "estado": "A",
      "logActivado": "S",
      "ubicacion": "/ruta/servicio"
    }
  ]
}
```

**Respuesta Exitosa (200 OK):**
```json
{
  "exitosos": 1,
  "fallidos": 0,
  "errores": []
}
```

### Actualizar Servicios Regionales

```
PUT /api/parametrizacion/regional/actualizar
```

**Cuerpo de la Solicitud:**
```json
{
  "carpetaQA": "/ruta/qa",
  "carpetaPRD": "/ruta/prd",
  "parametros": [
    {
      "idServicio": "SERV001",
      "origen": "ORIGEN1",
      "destino": "DEST1",
      "operacion": "CONSULTA_SALDO",
      "version": "1.0",
      "nuevoEstado": "I",
      "nuevoLogActivado": "N",
      "nuevaUbicacion": "/nueva/ruta",
      "nuevaOperacion": "CONSULTA_SALDO_ACT",
      "nuevaVersion": "2.0"
    }
  ]
}
```

### Eliminar Servicios Regionales

```
DELETE /api/parametrizacion/regional
```

**Cuerpo de la Solicitud:**
```json
{
  "carpetaQA": "/ruta/qa",
  "carpetaPRD": "/ruta/prd",
  "parametros": [
    {
      "idServicio": "SERV001",
      "origen": "ORIGEN1",
      "destino": "DEST1",
      "operacion": "CONSULTA_SALDO",
      "version": "1.0"
    }
  ]
}
```

## Manejo de Errores

La API devuelve códigos de estado HTTP estándar junto con mensajes de error descriptivos. La respuesta de error sigue el siguiente formato:

```json
{
  "tipo": "TIPO_ERROR",
  "error": "Mensaje de error descriptivo",
  "detalle": "Detalles adicionales del error",
  "parametro": "Valor del parámetro relacionado"
}
```

## Ejemplos de Uso

### Ejemplo 1: Crear un nuevo servicio regional

**Request:**
```http
POST /api/parametrizacion/regional
Content-Type: application/json

{
  "carpetaQA": "/ruta/qa",
  "carpetaPRD": "/ruta/prd",
  "parametros": [
    {
      "idServicio": "SERV002",
      "origen": "ORIGEN2",
      "destino": "DEST2",
      "operacion": "PAGO_SERVICIO",
      "version": "1.0",
      "estado": "A",
      "logActivado": "S",
      "ubicacion": "/ruta/pago"
    }
  ]
}
```

## Escenarios de Prueba

### 1. Creación Exitosa de Servicio Regional
- **Descripción**: Verificar que se puede crear un nuevo servicio regional correctamente.
- **Precondiciones**: No debe existir un servicio con el mismo idServicio, origen, destino, operación y versión.
- **Datos de Prueba**:
  ```json
  {
    "idServicio": "SERV_TEST",
    "origen": "TEST_ORIG",
    "destino": "TEST_DEST",
    "operacion": "TEST_OP",
    "version": "1.0",
    "estado": "A",
    "logActivado": "S",
    "ubicacion": "/test/path"
  }
  ```
- **Resultado Esperado**: Código 200 con 1 registro exitoso.

### 2. Actualización de Servicio Existente
- **Descripción**: Verificar que se puede actualizar un servicio existente.
- **Precondiciones**: El servicio debe existir en la base de datos.
- **Datos de Prueba**:
  ```json
  {
    "idServicio": "SERV_TEST",
    "origen": "TEST_ORIG",
    "destino": "TEST_DEST",
    "operacion": "TEST_OP",
    "version": "1.0",
    "nuevoEstado": "I",
    "nuevoLogActivado": "N"
  }
  ```
- **Resultado Esperado**: Código 200 con el estado actualizado a 'I'.

### 3. Eliminación de Servicio
- **Descripción**: Verificar que se puede eliminar un servicio existente.
- **Precondiciones**: El servicio debe existir en la base de datos.
- **Datos de Prueba**:
  ```json
  {
    "idServicio": "SERV_TEST",
    "origen": "TEST_ORIG",
    "destino": "TEST_DEST",
    "operacion": "TEST_OP",
    "version": "1.0"
  }
  ```
- **Resultado Esperado**: Código 200 con 1 eliminación exitosa.

### 4. Validación de Campos Obligatorios
- **Descripción**: Verificar que se validan los campos obligatorios.
- **Datos de Prueba**:
  ```json
  {
    "idServicio": "",
    "origen": "",
    "destino": "",
    "operacion": "",
    "version": ""
  }
  ```
- **Resultado Esperado**: Código 400 con mensajes de validación para cada campo obligatorio.

### 5. Generación de Archivos SQL
- **Descripción**: Verificar que se generan correctamente los archivos SQL.
- **Precondiciones**: Las carpetas de QA y PRD deben ser accesibles.
- **Verificación**: Comprobar que se crean los archivos SQL en las rutas especificadas.
