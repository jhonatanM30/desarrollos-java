package com.middleware.parametrizacion.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
public class SqlStatementGeneratorService {

    public String generateInsertStatement(String tableName, Map<String, String> columnValues) {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();
        
        columnValues.forEach((column, value) -> {
            if (columns.length() > 0) {
                columns.append(", ");
                values.append(", ");
            }
            columns.append(column);
            values.append("'").append(escapeValue(value)).append("'");
        });
        
        return String.format("INSERT INTO %s (%s) VALUES (%s);", 
                tableName, columns.toString(), values.toString());
    }

    public String generateUpdateStatement(String tableName, Map<String, String> columnValues, 
                                        Map<String, String> whereConditions) {
        StringBuilder setClause = new StringBuilder();
        columnValues.forEach((column, value) -> {
            if (setClause.length() > 0) {
                setClause.append(", ");
            }
            setClause.append(column).append(" = '").append(escapeValue(value)).append("'");
        });
        
        StringBuilder whereClause = new StringBuilder();
        whereConditions.forEach((column, value) -> {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(column).append(" = '").append(escapeValue(value)).append("'");
        });
        
        return String.format("UPDATE %s SET %s WHERE %s;", 
                tableName, setClause.toString(), whereClause.toString());
    }

    public String generateDeleteStatement(String tableName, Map<String, String> whereConditions) {
        StringBuilder whereClause = new StringBuilder();
        whereConditions.forEach((column, value) -> {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }
            whereClause.append(column).append(" = '").append(escapeValue(value)).append("'");
        });
        
        return String.format("DELETE FROM %s WHERE %s;", tableName, whereClause.toString());
    }

    public Map<String, String> createParameterMap(String nombreParametro, String valorParametro, 
                                                 String descripcionParametro) {

        return new LinkedHashMap<>(Map.of(
                "NOMBRE_PARAMETRO", nombreParametro,
                "VALOR_PARAMETRO", valorParametro,
                "DESCRIPCION_PARAMETRO", descripcionParametro
        ));
    }

    public Map<String, String> createParameterMapRegional(String idServicio, String origen, String destino, String operacion, String version, String estado, String logActivado, String ubicacion) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ID_SERVICIO", idServicio);
        map.put("ORIGEN", origen);
        map.put("DESTINO", destino);
        map.put("OPERACION", operacion);
        map.put("VERSION", version);
        map.put("ESTADO", estado);
        map.put("LOG_ACTIVADO", logActivado);
        map.put("UBICACION", ubicacion);
        return map;
    }

    public Map<String, String> convertToStringMap(Map<String, String> originalMap) {
        return new LinkedHashMap<>(originalMap);
    }
 

    private String escapeValue(String value) {
        return value != null ? value.replace("'", "''") : "";
    }
}
