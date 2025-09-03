package com.middleware.parametrizacion.service;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.entity.ParametrizacionMiddleware;
import com.middleware.parametrizacion.mapper.ParametrizacionMiddlewareMapper;
import com.middleware.parametrizacion.repository.ParametrizacionMiddlewareRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ParametrizacionMiddlewareService {

    private final ParametrizacionMiddlewareRepository repository;
    private final ParametrizacionMiddlewareMapper mapper;
    private final SqlFileGeneratorService sqlFileGeneratorService;
    private final SqlStatementGeneratorService sqlStatementGeneratorService;

    @Transactional
    public ParametrizacionResponseDto procesarParametrizacionMiddleware(ParametrizacionMiddlewareRequestDto request) {
        log.info("Iniciando procesamiento de parametrización middleware con {} parámetros", request.getParametros().size());

        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;

        for (ParametrizacionMiddlewareDto parametroDto : request.getParametros()) {
            try {
                // Validar si ya existe el parámetro
                if (repository.existsByNombreParametro(parametroDto.getNombreParametro())) {
                    log.warn("El parámetro {} ya existe en la base de datos", parametroDto.getNombreParametro());
                    
                    // Crear mapa con valores del parámetro usando el servicio reutilizable
                    Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMap(
                            parametroDto.getNombreParametro(),
                            parametroDto.getValorParametro(),
                            parametroDto.getDescripcionParametro()
                    );
                                                            
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("MIDDLEWARE")
                            .parametro(parametroDto.getNombreParametro())
                            .error("DUPLICADO")
                            .detalle("El parámetro ya existe en la base de datos")
                            .valoresParametro(sqlStatementGeneratorService.convertToStringMap(valoresParametro))                            
                            .build());
                    fallidos++;
                    continue;
                }

                // Convertir DTO a entidad y guardar
                ParametrizacionMiddleware entidad = mapper.toEntity(parametroDto);
                ParametrizacionMiddleware guardado = repository.save(entidad);
                
                // ✅ EXITOSO: Persistencia BD completada
                exitosos++;
                log.info("Parámetro {} persistido exitosamente en BD", parametroDto.getNombreParametro());

                // Intentar generar SQL statement (si falla, no afecta el conteo de exitosos)
                try {
                    Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMap(
                            guardado.getNombreParametro(),
                            guardado.getValorParametro(),
                            guardado.getDescripcionParametro()
                    );
                    String sqlStatement = sqlStatementGeneratorService.generateInsertStatement(
                            "PARAMETRIZACION_MIDDLEWARE", valoresParametro);
                    sqlStatements.add(sqlStatement);
                    
                    log.debug("SQL statement generado para parámetro {}", guardado.getNombreParametro());
                } catch (Exception sqlException) {
                    log.error("Error generando SQL statement para parámetro {}: {}", guardado.getNombreParametro(), sqlException.getMessage());
                    // Nota: No se decrementa exitosos, la persistencia BD fue exitosa
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("SISTEMA")
                            .parametro(guardado.getNombreParametro())
                            .error("ERROR_GENERACION_SQL")
                            .detalle("Parámetro guardado en BD exitosamente, pero falló generación de SQL: " + sqlException.getMessage())
                            .build());
                }

            } catch (Exception e) {
                log.error("Error procesando parámetro {}: {}", parametroDto.getNombreParametro(), e.getMessage(), e);
                
                // Crear mapa con valores del parámetro que falló usando el servicio reutilizable
                Map<String, String> valoresParametro = sqlStatementGeneratorService.createParameterMap(
                        parametroDto.getNombreParametro(),
                        parametroDto.getValorParametro(),
                        parametroDto.getDescripcionParametro()
                );
                
                // Generar INSERT que falló usando el servicio reutilizable
                String insertFallido = null;
                try {
                    insertFallido = sqlStatementGeneratorService.generateInsertStatement(
                            "PARAMETRIZACION_MIDDLEWARE", valoresParametro);
                } catch (Exception ex) {
                    log.warn("No se pudo generar INSERT para parámetro fallido {}", parametroDto.getNombreParametro());
                }
                
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("MIDDLEWARE")
                        .parametro(parametroDto.getNombreParametro())
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
                "INSERT_INTO", "PARAMETRIZACION_MIDDLEWARE", errores, exitosos, fallidos, request.getParametros().size());
    }

    @Transactional
    public ParametrizacionResponseDto actualizarParametrizacionMiddleware(ParametrizacionMiddlewareUpdateRequestDto request) {
        log.info("Iniciando actualización de parametrización middleware con {} parámetros", request.getParametros().size());

        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;

        for (ParametrizacionMiddlewareUpdateDto updateDto : request.getParametros()) {
            try {
                // Validar si existe el parámetro
                if (!repository.existsByNombreParametro(updateDto.getNombreParametro())) {
                    log.warn("El parámetro {} no existe en la base de datos", updateDto.getNombreParametro());
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("MIDDLEWARE")
                            .parametro(updateDto.getNombreParametro())
                            .error("NO_ENCONTRADO")
                            .detalle("El parámetro no existe en la base de datos")
                            .build());
                    fallidos++;
                    continue;
                }

                // Obtener entidad existente
                ParametrizacionMiddleware entidad = repository.findById(updateDto.getNombreParametro()).orElseThrow();
                
                // Actualizar solo los campos que no son null
                Map<String, String> camposActualizados = new HashMap<>();
                if (updateDto.getValorParametro() != null) {
                    entidad.setValorParametro(updateDto.getValorParametro());
                    camposActualizados.put("VALOR_PARAMETRO", updateDto.getValorParametro());
                }
                if (updateDto.getDescripcionParametro() != null) {
                    entidad.setDescripcionParametro(updateDto.getDescripcionParametro());
                    camposActualizados.put("DESCRIPCION_PARAMETRO", updateDto.getDescripcionParametro());
                }

                if (camposActualizados.isEmpty()) {
                    log.warn("No hay campos para actualizar en el parámetro {}", updateDto.getNombreParametro());
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("MIDDLEWARE")
                            .parametro(updateDto.getNombreParametro())
                            .error("SIN_CAMBIOS")
                            .detalle("No se especificaron campos para actualizar")
                            .build());
                    fallidos++;
                    continue;
                }

                // Guardar cambios
                repository.save(entidad);
                exitosos++;
                // Generar SQL statement para archivo
                try {
                Map<String, String> whereConditions = Map.of("NOMBRE_PARAMETRO", updateDto.getNombreParametro());
                String sqlStatement = sqlStatementGeneratorService.generateUpdateStatement(
                        "PARAMETRIZACION_MIDDLEWARE", camposActualizados, whereConditions);
                sqlStatements.add(sqlStatement);                              
                log.info("Parámetro {} actualizado exitosamente", updateDto.getNombreParametro());
                } catch (Exception e) {
                    log.error("Error generando SQL de actualización para parámetro: {}", updateDto.getNombreParametro(), e);
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("MIDDLEWARE")
                            .parametro(updateDto.getNombreParametro())
                            .error("ERROR_GENERACION_SQL")
                            .detalle("Error generando sentencia SQL de actualización: " + e.getMessage())
                            .build());                   
                }

            } catch (Exception e) {
                log.error("Error actualizando parámetro {}: {}", updateDto.getNombreParametro(), e.getMessage(), e);
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("MIDDLEWARE")
                        .parametro(updateDto.getNombreParametro())
                        .error("ERROR_PROCESAMIENTO")
                        .detalle(e.getMessage())
                        .build());
                fallidos++;
            }
        }

        return sqlFileGeneratorService.generarRespuestaConArchivos(request.getCarpetaQA(), request.getCarpetaPRD(), sqlStatements, 
                "UPDATE", "PARAMETRIZACION_MIDDLEWARE", errores, exitosos, fallidos, request.getParametros().size());
    }

    @Transactional
    public ParametrizacionResponseDto eliminarParametrizacionMiddleware(ParametrizacionDeleteRequestDto request) {
        log.info("Iniciando eliminación de parametrización middleware con {} parámetros", request.getClavesPrimarias().size());

        List<ParametrizacionResponseDto.ErrorDto> errores = new ArrayList<>();
        List<String> sqlStatements = new ArrayList<>();
        int exitosos = 0;
        int fallidos = 0;

        for (String nombreParametro : request.getClavesPrimarias()) {
            try {
                // Validar si existe el parámetro
                if (!repository.existsByNombreParametro(nombreParametro)) {
                    log.warn("El parámetro {} no existe en la base de datos", nombreParametro);
                    errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                            .tipo("MIDDLEWARE")
                            .parametro(nombreParametro)
                            .error("NO_ENCONTRADO")
                            .detalle("El parámetro no existe en la base de datos")
                            .build());
                    fallidos++;
                    continue;
                }

                // Eliminar parámetro
                repository.deleteById(nombreParametro);
                exitosos++;
                // Generar SQL statement para archivo
                Map<String, String> whereConditions = Map.of("NOMBRE_PARAMETRO", nombreParametro);
                String sqlStatement = sqlStatementGeneratorService.generateDeleteStatement(
                        "PARAMETRIZACION_MIDDLEWARE", whereConditions);
                sqlStatements.add(sqlStatement);                
                log.info("Parámetro {} eliminado exitosamente", nombreParametro);

            } catch (Exception e) {
                log.error("Error eliminando parámetro {}: {}", nombreParametro, e.getMessage(), e);
                errores.add(ParametrizacionResponseDto.ErrorDto.builder()
                        .tipo("MIDDLEWARE")
                        .parametro(nombreParametro)
                        .error("ERROR_PROCESAMIENTO")
                        .detalle(e.getMessage())
                        .build());
                fallidos++;
            }
        }

        return sqlFileGeneratorService.generarRespuestaConArchivos(request.getCarpetaQA(), request.getCarpetaPRD(), sqlStatements, 
                "DELETE_FROM", "PARAMETRIZACION_MIDDLEWARE", errores, exitosos, fallidos, request.getClavesPrimarias().size());
    }

    public List<ParametrizacionMiddlewareDto> consultarParametrizacionMiddleware(ParametrizacionQueryRequestDto request) {
        log.info("Iniciando consulta de parametrización middleware con {} criterios", request.getCriteriosBusqueda().size());

        List<ParametrizacionMiddleware> entidades = repository.findByNombreParametroIn(request.getCriteriosBusqueda());
        
        return entidades.stream()
                .map(mapper::toDto)
                .toList();
    }

   
   
}
