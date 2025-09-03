package com.middleware.parametrizacion.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacionResponseDto {

    private boolean exitoso;
    private String mensaje;
    private int totalProcesados;
    private int exitosos;
    private int fallidos;
    private List<ErrorDto> errores;
    private String archivoSqlQa;
    private String archivoSqlPrd;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDto {
        private String tipo;
        private String parametro;
        private String error;
        private String detalle;
        private Map<String, String> valoresParametro;
        private String insertFallido;
    }
}
