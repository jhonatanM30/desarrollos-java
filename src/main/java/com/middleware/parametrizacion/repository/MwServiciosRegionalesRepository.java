package com.middleware.parametrizacion.repository;

import com.middleware.parametrizacion.entity.MwServiciosRegionales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MwServiciosRegionalesRepository extends 
        JpaRepository<MwServiciosRegionales, String>,
        JpaSpecificationExecutor<MwServiciosRegionales> {
      
    // Buscar por idServicio y origen (aun no se implementa)
    Optional<MwServiciosRegionales> findByIdServicioAndOrigen(String idServicio, String origen);
    
    
    // Métodos para búsqueda por clave compuesta
    @Query("SELECT e FROM MwServiciosRegionales e WHERE " +
           "e.idServicio = :idServicio AND " +
           "e.origen = :origen AND " +
           "e.destino = :destino AND " +
           "e.operacion = :operacion AND " +
           "e.version = :version")
    MwServiciosRegionales findByAllFields(
            @Param("idServicio") String idServicio,
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("operacion") String operacion,
            @Param("version") String version);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END FROM MwServiciosRegionales e WHERE " +
           "e.idServicio = :idServicio AND " +
           "e.origen = :origen AND " +
           "e.destino = :destino AND " +
           "e.operacion = :operacion AND " +
           "e.version = :version")
    boolean existsByAllFields(
            @Param("idServicio") String idServicio,
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("operacion") String operacion,
            @Param("version") String version);

            @Modifying
            @Query("DELETE FROM MwServiciosRegionales e WHERE " +
                   "e.idServicio = :idServicio AND " +
                   "e.origen = :origen AND " +
                   "e.destino = :destino AND " +
                   "e.operacion = :operacion AND " +
                   "e.version = :version")
   void deleteByAllFields(
            @Param("idServicio") String idServicio,
            @Param("origen") String origen,
            @Param("destino") String destino,
            @Param("operacion") String operacion,
            @Param("version") String version);

@Query("SELECT e FROM MwServiciosRegionales e WHERE e.idServicio IN :idServicio")
   List<MwServiciosRegionales> findByIdServicioIn(@Param("idServicio") List<String> idServicio);
}