package com.middleware.parametrizacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacionMiddlewareDto {

    @NotBlank(message = "El nombre del parámetro es obligatorio")
    @Size(max = 200, message = "El nombre del parámetro no puede exceder 200 caracteres")
    private String nombreParametro;

    @NotBlank(message = "El valor del parámetro es obligatorio")
    @Size(max = 200, message = "El valor del parámetro no puede exceder 200 caracteres")
    private String valorParametro;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcionParametro;
}
