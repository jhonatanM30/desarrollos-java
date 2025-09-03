package com.middleware.parametrizacion.repository;

import com.middleware.parametrizacion.entity.ParametrizacionMiddleware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametrizacionMiddlewareRepository extends JpaRepository<ParametrizacionMiddleware, String> {
    
    boolean existsByNombreParametro(String nombreParametro);
    
    @Query("SELECT p FROM ParametrizacionMiddleware p WHERE p.nombreParametro IN :nombres")
    List<ParametrizacionMiddleware> findByNombreParametroIn(@Param("nombres") List<String> nombres);
}
