# API Documentation - Parametrización Middleware

## Descripción General

Esta API proporciona operaciones CRUD completas para la parametrización de middleware y servicios regionales, con generación automática de archivos SQL siguiendo la convención: `<OPERACION>_<ENTORNO>_<NOMBRETABLA>.sql`

## Endpoints Disponibles

### Parametrización Middleware

#### 1. Crear Parametrización (INSERT)
**POST** `/parametrizacion/middleware`

```json
{
  "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
    {
      "nombreParametro": "TIMEOUT_CONEXION",
      "valorParametro": "30000",
      "descripcionParametro": "Tiempo de espera para conexiones en milisegundos"
    },
    {
      "nombreParametro": "TIMEOUT",
      "valorParametro": "30000",
      "descripcionParametro": "Tiempo de espera para conexiones en milisegundos"
    }  
  ]
}
```
```json RESPONSE
{
   "exitoso": true,
   "mensaje": "OperaciÃ³n completada exitosamente. 2 registros procesados.",
   "totalProcesados": 2,
   "exitosos": 2,
   "fallidos": 0,
   "errores": [],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql"
}

```

**Archivos generados:**
- `INSERT_INTO_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `INSERT_INTO_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)

#### 2. Actualizar Parametrización (UPDATE)
**PUT** `/parametrizacion/middleware`

```json
{
  "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
   {
      "nombreParametro": "TIMEOUT_CONEXION",
      "valorParametro": "500",
      "descripcionParametro": "Tiempo de espera para conexiones en milisegundos"
    },
    {
      "nombreParametro": "TIMEOUT",
      "valorParametro": "200",
      "descripcionParametro": "Tiempo de espera para conexiones en milisegundos"
    }  
  ]
}
```

```json RESPONSE

{
   "exitoso": true,
   "mensaje": "OperaciÃ³n completada exitosamente. 2 registros procesados.",
   "totalProcesados": 2,
   "exitosos": 2,
   "fallidos": 0,
   "errores": [],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\UPDATE_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\UPDATE_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql"
}

```

**Archivos generados:**
- `UPDATE_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `UPDATE_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)

**Nota:** Solo se actualizan los campos que no son `null`. El `nombreParametro` es obligatorio como clave primaria.

#### 3. Consultar Parametrización (SELECT)
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



#### 4. Eliminar Parametrización (DELETE)
**DELETE** `/parametrizacion/middleware`

```json
{
"carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "clavesPrimarias": [
    "TIMEOUT_CONEXION",
    "MAX_REINTENTOS",
    "TIMEOUT"
  ]
}
```

```json Response cuando se elimina unos existentes y otros no

{
   "exitoso": false,
   "mensaje": "OperaciÃ³n completada con errores. 2 exitosos, 1 fallidos.",
   "totalProcesados": 3,
   "exitosos": 2,
   "fallidos": 1,
   "errores": [   {
      "tipo": "MIDDLEWARE",
      "parametro": "MAX_REINTENTOS",
      "error": "NO_ENCONTRADO",
      "detalle": "El parÃ¡metro no existe en la base de datos",
      "valoresParametro": null,
      "insertFallido": null
   }],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\DELETE_FROM_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\DELETE_FROM_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql"
}
```

**Archivos generados:**
- `DELETE_FROM_MIDDLEWARE2_PARAMETRIZACION_MIDDLEWARE.sql` (QA)
- `DELETE_FROM_MIDDLEWARE_PARAMETRIZACION_MIDDLEWARE.sql` (PRD)



### Parametrización Regional

#### 1. Crear Servicios Regionales (INSERT)
**POST** `/parametrizacion/regional/crear`

```json
{
"carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
    {
      "idServicio": "SERV003",
      "origen": "NI01",
      "destino": "NI01",
      "operacion": "ConsultaCliente",
      "version": "V1",
      "estado": "A",
      "ubicacion": "/ruta/servicio",
      "logActivado": "S"
    }
  ]
}

```

```json Response
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
**PUT** `/parametrizacion/regional/actualizar`

```json
{
  "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
    {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "NI01",
      "operacion": "ConsultaCliente",
      "version": "V1",
      "nuevoEstado": "I",
      "nuevaUbicacion": "/nueva/ruta/servicio",
      "nuevoLogActivado": "N"
    }
  ]
}
```

```json Response

{
   "exitoso": true,
   "mensaje": "OperaciÃ³n completada exitosamente. 1 registros procesados.",
   "totalProcesados": 1,
   "exitosos": 1,
   "fallidos": 0,
   "errores": [],
   "archivoSqlQa": "C:\\FICOHSA\\SoportePRD\\QA\\UPDATE_MIDDLEWARE2_PARAMETRIZACION_REGIONAL.sql",
   "archivoSqlPrd": "C:\\FICOHSA\\SoportePRD\\PRD\\UPDATE_MIDDLEWARE_PARAMETRIZACION_REGIONAL.sql"
}
```

**Archivos generados:**
- `UPDATE_MIDDLEWARE2_MW_SERVICIOS_REGIONALES.sql` (QA)
- `UPDATE_MIDDLEWARE_MW_SERVICIOS_REGIONALES.sql` (PRD)

#### 3. Eliminar Servicios Regionales (DELETE)
**DELETE** `/parametrizacion/regional/eliminar`

```json
{
 "carpetaQA": "C:/FICOHSA/SoportePRD/QA",
  "carpetaPRD": "C:/FICOHSA/SoportePRD/PRD",
  "parametros": [
    {
      "idServicio": "SERV001",
      "origen": "HN01",
      "destino": "NI01",
      "operacion": "ConsultaCliente",
      "version": "V1"
    }
  ]
}
 
 ```

```json Response
{
   "exitoso": true,
   "mensaje": "OperaciÃ³n completada exitosamente. 1 registros procesados.",
   "totalProcesados": 1,
   "exitosos": 1,
   "fallidos": 0,
   "errores": [],
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
  "criteriosBusqueda": ["SERV001", "SERV002"]
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

