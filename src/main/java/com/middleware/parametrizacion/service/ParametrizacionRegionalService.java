package com.middleware.parametrizacion.service;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.entity.MwServiciosRegionales;
import com.middleware.parametrizacion.mapper.MwServiciosRegionalesMapper;
import com.middleware.parametrizacion.repository.MwServiciosRegionalesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParametrizacionRegionalService {
    private final MwServiciosRegionalesRepository repository;
    private final MwServiciosRegionalesMapper mapper;
    private final SqlFileGeneratorService sqlFileGeneratorService;
    private final SqlStatementGeneratorService sqlStatementGeneratorService;    

    public List<MwServiciosRegionalesDto> consultarParametrizacionRegional(
            ParametrizacionQueryRequestDto request) {
        log.info("Consultando servicios regionales con criterios: {}", request.getCriteriosBusqueda());
               
        List<MwServiciosRegionales> entidades = repository.findByIdServicioIn(request.getCriteriosBusqueda());
              
        
        return entidades.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ParametrizacionResponseDto procesarParametrizacionRegional(
            ParametrizacionRegionalRequestDto request) {
        log.info("Iniciando procesamiento de {} servicios regionales", request.getParametros().size());
                
        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;
        
        for (MwServiciosRegionalesDto dto : request.getParametros()) {
            String paramKey = dto.getIdServicio() + ", " + dto.getOrigen() + ", " + dto.getDestino() + ", " + dto.getOperacion() + ", " + dto.getVersion();
            try {
                
                if(repository.existsByAllFields(dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion())){
                    log.warn("El registro {}  existe en la base de datos", paramKey);
                   
                    // Crear mapa con valores del parámetro usando el servicio reutilizable
                    Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMapRegional(
                            dto.getIdServicio(),
                            dto.getOrigen(),
                            dto.getDestino(),
                            dto.getOperacion(),
                            dto.getVersion(),
                            dto.getEstado(),
                            dto.getLogActivado(),
                            dto.getUbicacion()
                    );                
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("DUPLICADO")
                            .detalle("El registro ya existe en la base de datos con los valores clave: " + paramKey)
                            .valoresParametro(sqlStatementGeneratorService.convertToStringMap(valoresParametro))                            
                            .build());
                    fallidos++;
                    continue;
                }

                // Guardar entidad
                MwServiciosRegionales entidad = mapper.toEntity(dto);
                MwServiciosRegionales guardado = repository.save(entidad);
                exitosos++;
                try {
                    Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMapRegional(
                            guardado.getIdServicio(),
                            guardado.getOrigen(),
                            guardado.getDestino(),
                            guardado.getOperacion(),
                            guardado.getVersion(),
                            guardado.getEstado(),
                            guardado.getLogActivado(),
                            guardado.getUbicacion()
                    );
                    String sqlStatement = sqlStatementGeneratorService.generateInsertStatement(
                            "PARAMETRIZACION_REGIONAL", valoresParametro);
                    sqlStatements.add(sqlStatement);
                    
                    log.debug("SQL statement generado para servicio {}", guardado.getIdServicio());
                } catch (Exception sqlException) {
                    log.error("Error generando SQL statement para servicio {}: {}", guardado.getIdServicio(), sqlException.getMessage());
                    // Nota: No se decrementa exitosos, la persistencia BD fue exitosa
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("SISTEMA")
                            .parametro(paramKey)
                            .error("ERROR_GENERACION_SQL")
                            .detalle("Servicio guardado en BD exitosamente, pero falló generación de SQL: " + sqlException.getMessage())
                            .build());
                }
                                                                
            } catch (Exception e) {
                log.error("Error procesando servicio regional: {}", dto, e);                
                // Crear mapa con valores del parámetro que falló usando el servicio reutilizable
                Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMapRegional(
                        dto.getIdServicio(),
                        dto.getOrigen(),
                        dto.getDestino(),
                        dto.getOperacion(),
                        dto.getVersion(),
                        dto.getEstado(),
                        dto.getLogActivado(),
                        dto.getUbicacion()
                );
                
                // Generar INSERT que falló usando el servicio reutilizable
                String insertFallido = null;
                try {
                    insertFallido = sqlStatementGeneratorService.generateInsertStatement(
                            "PARAMETRIZACION_REGIONAL", valoresParametro);
                } catch (Exception ex) {
                    log.warn("No se pudo generar INSERT para servicio regional fallido {}", dto.getIdServicio());
                }
                
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("REGIONAL")
                        .parametro(dto.getIdServicio())
                        .error("ERROR_PERSISTENCIA")
                        .detalle(e.getMessage())
                        .valoresParametro(sqlStatementGeneratorService.convertToStringMap(valoresParametro))
                        .insertFallido(insertFallido)
                        .build());
                fallidos++;
            }
        }
        
         //  Usar método genérico para desacoplar lógica de construcción de respuesta
         return sqlFileGeneratorService.generarRespuestaConArchivos(request.getCarpetaQA(), request.getCarpetaPRD(), sqlStatements, 
         "INSERT_INTO", "PARAMETRIZACION_REGIONAL", errores, exitosos, fallidos, request.getParametros().size());
    }

    @Transactional
    public ParametrizacionResponseDto actualizarParametrizacionRegional(
            ParametrizacionRegionalUpdateRequestDto request) {
        log.info("Iniciando actualización de {} servicios regionales", 
                request.getParametros().size());
              

        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;
        
        for (MwServiciosRegionalesUpdateDto dto : request.getParametros()) {
            String paramKey = dto.getIdServicio() + ", " + dto.getOrigen() + ", " + dto.getDestino() + ", " + dto.getOperacion() + ", " + dto.getVersion();
            try {                
                if(!repository.existsByAllFields(dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion())){                    
                    log.warn("El registro no existe en la base de datos con los valores clave: {}", paramKey);                                                   
                    
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("REGISTRO_NO_ENCONTRADO")
                            .detalle("El registro no existe en la base de datos con los valores clave: " + paramKey)                            
                            .build());
                    fallidos++;
                    continue;
                }
                
                // Buscar entidad existente
                MwServiciosRegionales entidad = repository.findByAllFields(dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion());
                
                // Actualizar campos
                boolean cambios = actualizarCampos(dto, entidad);
                
                if (!cambios) {
                    log.warn("No hay campos para actualizar {}", dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion());
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("SIN_CAMBIOS")
                            .detalle("No se especificaron campos para actualizar")
                            .build());
                    fallidos++;
                    continue;
                }
                               
                 repository.save(entidad);
                 exitosos++;
                // Generar SQL de actualización
                try {
                    String updateSql = generarSqlActualizacion(dto);
                    sqlStatements.add(updateSql);
                    log.debug("SQL UPDATE generado: {}", updateSql);
                } catch (Exception e) {
                    log.error("Error generando SQL de actualización para servicio: {}/{}", 
                            dto.getIdServicio(), dto.getOrigen(), e);
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("ERROR_GENERACION_SQL")
                            .detalle("Error generando sentencia SQL de actualización: " + e.getMessage())
                            .build());
                }
                               
                
            } 
             catch (Exception e) {
                log.error("Error inesperado al actualizar servicio regional: {}", dto, e);
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("REGIONAL")
                        .parametro(paramKey)
                        .error("ERROR_ACTUALIZACION")
                        .detalle("Error al actualizar el servicio: " + e.getMessage())
                        .build());
                fallidos++;
            }
        }
                       
        return sqlFileGeneratorService.generarRespuestaConArchivos(request.getCarpetaQA(), request.getCarpetaPRD(), sqlStatements, 
                "UPDATE", "PARAMETRIZACION_REGIONAL", errores, exitosos, fallidos, request.getParametros().size());
    }

    @Transactional
    public ParametrizacionResponseDto eliminarParametrizacionRegional(
            ParametrizacionRegionalRequestDto request) {
        log.info("Iniciando eliminación de {} servicios regionales", 
                request.getParametros() != null ? request.getParametros().size() : 0);
            

        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;
        
        for (MwServiciosRegionalesDto dto : request.getParametros()) {   
            String paramKey = dto.getIdServicio() + ", " + dto.getOrigen() + ", " + dto.getDestino() + ", " + dto.getOperacion() + ", " + dto.getVersion();                
            try {                                
                // Verificar existencia
                if (!repository.existsByAllFields(dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion())) {
                   log.warn("Los parámetros {} no existen en la base de datos", paramKey);
                   errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("REGISTRO_NO_ENCONTRADO")
                            .detalle("El registro no existe en la base de datos con los valores clave: " + paramKey)
                            .build());
                   fallidos++;
                   continue;
                }

                // Eliminar entidad
                repository.deleteByAllFields(dto.getIdServicio(), dto.getOrigen(), dto.getDestino(), dto.getOperacion(), dto.getVersion());
                exitosos++;
                
                // Generar SQL de eliminación antes de borrar
                try {
                    Map<String, String> whereConditions = new HashMap<>();
                    whereConditions.put("ID_SERVICIO", dto.getIdServicio());
                    whereConditions.put("ORIGEN", dto.getOrigen());
                    whereConditions.put("DESTINO", dto.getDestino());
                    whereConditions.put("OPERACION", dto.getOperacion());
                    whereConditions.put("VERSION", dto.getVersion());
                    
                    String deleteSql = sqlStatementGeneratorService.generateDeleteStatement(
                            "MW_SERVICIOS_REGIONALES", whereConditions);
                    sqlStatements.add(deleteSql);
                    log.debug("SQL DELETE generado: {}", deleteSql);
                } catch (Exception e) {
                    log.error("Error generando SQL de eliminación para servicio: {}/{}", e);
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("ERROR_GENERACION_SQL")
                            .detalle("Error generando sentencia SQL de eliminación: " + e.getMessage())
                            .build());                    
                    continue;
                }                                                
            } 
             catch (Exception e) {
                log.error("Error inesperado al eliminar servicio regional: {}/{}", e);
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("REGIONAL")
                            .parametro(paramKey)
                            .error("ERROR_ELIMINACION")
                            .detalle("Error al eliminar el servicio: " + e.getMessage())
                            .build());
                fallidos++;
            }
        }
                
        return sqlFileGeneratorService.generarRespuestaConArchivos(request.getCarpetaQA(), request.getCarpetaPRD(), sqlStatements, 
                "DELETE_FROM", "PARAMETRIZACION_REGIONAL", errores, exitosos, fallidos, request.getParametros().size());
    }

    
    private boolean actualizarCampos(MwServiciosRegionalesUpdateDto dto, MwServiciosRegionales entidad) {
        boolean cambios = false;
                
        if (dto.getNuevaUbicacion() != null && !dto.getNuevaUbicacion().equals(entidad.getUbicacion())) {
            entidad.setUbicacion(dto.getNuevaUbicacion());
            cambios = true;
        }
        if (dto.getNuevoEstado() != null && !dto.getNuevoEstado().equals(entidad.getEstado())) {
            entidad.setEstado(dto.getNuevoEstado());
            cambios = true;
        }
        if (dto.getNuevoLogActivado() != null && !dto.getNuevoLogActivado().equals(entidad.getLogActivado())) {
            entidad.setLogActivado(dto.getNuevoLogActivado());
            cambios = true;
        }
        
        return cambios;
    }
    
    private String generarSqlActualizacion(MwServiciosRegionalesUpdateDto dto) {
        Map<String, String> setValues = new HashMap<>();        
        if (dto.getNuevaUbicacion() != null) setValues.put("UBICACION", dto.getNuevaUbicacion());
        if (dto.getNuevoEstado() != null) setValues.put("ESTADO", dto.getNuevoEstado());
        if (dto.getNuevoLogActivado() != null) setValues.put("LOG_ACTIVADO", dto.getNuevoLogActivado());
        
        Map<String, String> whereConditions = new HashMap<>();
        whereConditions.put("ID_SERVICIO", dto.getIdServicio());
        whereConditions.put("ORIGEN", dto.getOrigen());
        whereConditions.put("DESTINO", dto.getDestino());
        whereConditions.put("OPERACION", dto.getOperacion());
        whereConditions.put("VERSION", dto.getVersion());
        
        return sqlStatementGeneratorService.generateUpdateStatement(
                "MW_SERVICIOS_REGIONALES", setValues, whereConditions);
    }

}
    