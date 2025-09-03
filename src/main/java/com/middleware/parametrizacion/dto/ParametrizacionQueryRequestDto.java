package com.middleware.parametrizacion.dto;

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
public class ParametrizacionQueryRequestDto {

    @NotEmpty(message = "Debe incluir al menos un criterio de búsqueda")
    private List<String> criteriosBusqueda; // Para middleware: nombreParametro, para regional: idServicio
}
