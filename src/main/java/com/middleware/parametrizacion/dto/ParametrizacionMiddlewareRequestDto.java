package com.middleware.parametrizacion.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacionMiddlewareRequestDto {

    @NotBlank(message = "La carpeta QA es obligatoria")
    private String carpetaQA;

    @NotBlank(message = "La carpeta PRD es obligatoria")
    private String carpetaPRD;

    @NotEmpty(message = "Debe incluir al menos un parámetro")
    @Valid
    private List<ParametrizacionMiddlewareDto> parametros;
}
