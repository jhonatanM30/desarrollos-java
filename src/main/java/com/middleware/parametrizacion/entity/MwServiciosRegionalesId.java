package com.middleware.parametrizacion.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data  // Genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Constructor vacío
@AllArgsConstructor // Constructor con todos los campos
public class MwServiciosRegionalesId implements Serializable {
    private String idServicio;
    private String origen;
    private String destino;
    private String operacion;
    private String version;
}
