package com.middleware.parametrizacion.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.middleware.parametrizacion.dto.ParametrizacionResponseDto;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class SqlFileGeneratorService {

    @Value("${app.sql-generation.qa-prefix}")
    private String qaPrefix;

    @Value("${app.sql-generation.prd-prefix}")
    private String prdPrefix;


    public SqlFileResult generateSqlFiles(String carpetaQA, String carpetaPRD, List<String> sqlStatements, 
                                         String operacion, String tableName) {
        try {
            // Crear directorios si no existen
            Path qaDir = Paths.get(carpetaQA);
            Path prdDir = Paths.get(carpetaPRD);
            Files.createDirectories(qaDir);
            Files.createDirectories(prdDir);

            // Generar nombres de archivos según nueva convención: <OPERACION>_<ENTORNO>_<NOMBRETABLA>.sql
            String qaFileName = String.format("%s_MIDDLEWARE2_%s.sql", operacion, tableName);
            String prdFileName = String.format("%s_MIDDLEWARE_%s.sql", operacion, tableName);

            Path qaFilePath = qaDir.resolve(qaFileName);
            Path prdFilePath = prdDir.resolve(prdFileName);

            // Contenido para QA (con prefijo MIDDLEWARE2)
            List<String> qaStatements = sqlStatements.stream()
                    .map(sql -> addSchemaPrefix(sql, qaPrefix, operacion))
                    .toList();

            // Contenido para PRD (con prefijo MIDDLEWARE)
            List<String> prdStatements = sqlStatements.stream()
                    .map(sql -> addSchemaPrefix(sql, prdPrefix, operacion))
                    .toList();

            // Escribir archivos
            Files.write(qaFilePath, generateFileContent(qaStatements, "QA", operacion));
            Files.write(prdFilePath, generateFileContent(prdStatements, "PRD", operacion));

            log.info("Archivos SQL generados exitosamente: QA={}, PRD={}", qaFilePath, prdFilePath);
            
            return SqlFileResult.builder()
                    .archivoQa(qaFilePath.toString())
                    .archivoPrd(prdFilePath.toString())
                    .build();

        } catch (IOException e) {
            log.error("Error generando archivos SQL", e);
            throw new RuntimeException("Error al generar archivos SQL: " + e.getMessage(), e);
        }
    }

    // Método legacy para mantener compatibilidad
    public SqlFileResult generateSqlFiles(String carpetaQA, String carpetaPRD, List<String> sqlStatements, String tipoParametrizacion) {
        String tableName = tipoParametrizacion.equals("MIDDLEWARE") ? "PARAMETRIZACION_MIDDLEWARE" : "MW_SERVICIOS_REGIONALES";
        return generateSqlFiles(carpetaQA, carpetaPRD, sqlStatements, "INSERT_INTO", tableName);
    }

    private String addSchemaPrefix(String sql, String schemaPrefix, String operacion) {
        switch (operacion.toUpperCase()) {
            case "INSERT_INTO":
                return sql.replace("INSERT INTO ", "INSERT INTO " + schemaPrefix + ".");
            case "UPDATE":
                return sql.replace("UPDATE ", "UPDATE " + schemaPrefix + ".");
            case "DELETE_FROM":
                return sql.replace("DELETE FROM ", "DELETE FROM " + schemaPrefix + ".");
            default:
                return sql;
        }
    }

    private List<String> generateFileContent(List<String> sqlStatements, String environment, String operacion) {
        String operacionDesc = getOperacionDescription(operacion);
        
        return List.of(
                "-- Archivo SQL generado automáticamente para " + environment,
                "-- Fecha: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "-- Operación: " + operacionDesc,
                "-- Parametrización Middleware",
                "",
                "SET DEFINE OFF;",
                "",
                String.join("\n", sqlStatements),
                "",
                "COMMIT;",
                "",
                "-- Fin del archivo"
        );
    }

    private String getOperacionDescription(String operacion) {
        return switch (operacion.toUpperCase()) {
            case "INSERT_INTO" -> "Inserción de registros";
            case "UPDATE" -> "Actualización de registros";
            case "DELETE_FROM" -> "Eliminación de registros";
            default -> "Operación SQL";
        };
    }

    public ParametrizacionResponseDto generarRespuestaConArchivos(String carpetaQA, String carpetaPRD, 
            List<String> sqlStatements, String operacion, String tableName,
            List<ParametrizacionResponseDto.ErrorDto> errores, int exitosos, int fallidos, int totalProcesados) {
        
        String archivoQa = null;
        String archivoPrd = null;
        
        if (!sqlStatements.isEmpty()) {
            try {                
                SqlFileResult result = generateSqlFiles(
                        carpetaQA, carpetaPRD, sqlStatements, operacion, tableName);
                archivoQa = result.getArchivoQa();
                archivoPrd = result.getArchivoPrd();
                log.info("Archivos SQL generados: QA={}, PRD={}", archivoQa, archivoPrd);
            } catch (Exception e) {
                log.error("Error generando archivos SQL: {}", e.getMessage(), e);
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("SISTEMA")
                        .parametro("GENERACION_ARCHIVOS")
                        .error("ERROR_ARCHIVO")
                        .detalle(String.format("Se procesaron %d parámetros exitosamente en BD, pero falló la generación de archivos SQL: %s", 
                                exitosos, e.getMessage()))
                        .build());
            }
        }

        boolean exitoso = fallidos == 0;
        String mensaje = exitoso 
                ? String.format("Operación completada exitosamente. %d registros procesados.", exitosos)
                : String.format("Operación completada con errores. %d exitosos, %d fallidos.", exitosos, fallidos);

        return ParametrizacionResponseDto.builder()
                .exitoso(exitoso)
                .mensaje(mensaje)
                .totalProcesados(totalProcesados)
                .exitosos(exitosos)
                .fallidos(fallidos)
                .errores(errores)
                .archivoSqlQa(archivoQa)
                .archivoSqlPrd(archivoPrd)
                .build();
    }
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class SqlFileResult {
        private String archivoQa;
        private String archivoPrd;
    }
}
