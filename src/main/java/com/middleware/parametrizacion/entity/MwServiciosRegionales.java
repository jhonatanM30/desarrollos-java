package com.middleware.parametrizacion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MW_SERVICIOS_REGIONALES_OSB")
@IdClass(MwServiciosRegionalesId.class)  // Agregar esta anotación
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MwServiciosRegionales {
    @Id
    @Column(name = "ID_SERVICIO", nullable = false, length = 15)
    private String idServicio;
    
    @Id
    @Column(name = "ORIGEN", nullable = false, length = 10)
    private String origen;
    
    @Id
    @Column(name = "DESTINO", nullable = false, length = 10)
    private String destino;
    
    @Id
    @Column(name = "OPERACION", nullable = false, length = 60)
    private String operacion;
    
    @Column(name = "UBICACION", nullable = false, length = 200)
    private String ubicacion;
    
    @Id
    @Column(name = "VERSION", nullable = false, length = 3)
    private String version;
    
    @Column(name = "ESTADO", length = 2)
    private String estado;
    
    @Column(name = "LOG_ACTIVADO", length = 2)
    private String logActivado;
    
}