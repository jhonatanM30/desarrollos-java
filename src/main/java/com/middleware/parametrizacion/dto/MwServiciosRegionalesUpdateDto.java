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
public class MwServiciosRegionalesUpdateDto {
    @NotBlank(message = "El ID del servicio es obligatorio")
    @Size(max = 15, message = "El ID del servicio no puede exceder 15 caracteres")
    private String idServicio;

    @NotBlank(message = "El origen es obligatorio")
    @Size(max = 5, message = "El origen no puede exceder 5 caracteres")
    private String origen;

    @NotBlank(message = "El destino es obligatorio")
    @Size(max = 5, message = "El destino no puede exceder 5 caracteres")
    private String destino;

    @NotBlank(message = "La operación es obligatoria")
    @Size(max = 60, message = "La operación no puede exceder 60 caracteres")
    private String operacion;

    @NotBlank(message = "La versión es obligatoria")
    @Size(min = 1, max = 3, message = "La versión debe tener entre 1 y 3 caracteres")
    private String version;
    
    @Size(max = 200, message = "La nueva ubicación no puede exceder 200 caracteres")
    private String nuevaUbicacion;
    
    @Size(max = 2, message = "El nuevo estado no puede exceder 2 caracteres")
    private String nuevoEstado;
    
    @Size(max = 2, message = "El nuevo log activado no puede exceder 2 caracteres")
    private String nuevoLogActivado;
}