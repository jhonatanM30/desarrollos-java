package com.middleware.parametrizacion.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PARAMETRIZACION_MIDDLEWARE")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacionMiddleware {

    @Id
    @Column(name = "NOMBRE_PARAMETRO", nullable = false, length = 100)
    private String nombreParametro;

    @Column(name = "VALOR_PARAMETRO", nullable = false, length = 500)
    private String valorParametro;

    @Column(name = "DESCRIPCION_PARAMETRO", nullable = false, length = 1000)
    private String descripcionParametro;
}
