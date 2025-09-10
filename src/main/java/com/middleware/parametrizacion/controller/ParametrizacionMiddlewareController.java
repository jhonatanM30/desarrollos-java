package com.middleware.parametrizacion.controller;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.service.ParametrizacionMiddlewareService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Parametrización Middleware", description = "API para la gestión de parametrización de middleware")
@RequestMapping(value = "/parametrizacion/middleware", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParametrizacionMiddlewareController {

    private final ParametrizacionMiddlewareService parametrizacionMiddlewareService;

    @Operation(summary = "Procesar parametrización de middleware",
              description = "Crea una nueva configuración de parametrización de middleware con los parámetros proporcionados")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> procesarParametrizacionMiddleware(
            @Valid @RequestBody ParametrizacionMiddlewareRequestDto request) {
        
        log.info("Recibida solicitud de parametrización middleware con {} parámetros", 
                request.getParametros().size());
        
        ParametrizacionResponseDto response = parametrizacionMiddlewareService
                .procesarParametrizacionMiddleware(request);
        
        log.info("Procesamiento completado - Exitosos: {}, Fallidos: {}", 
                response.getExitosos(), response.getFallidos());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar parametrización de middleware",
              description = "Actualiza una configuración existente de parametrización de middleware")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> actualizarParametrizacionMiddleware(
            @Valid @RequestBody ParametrizacionMiddlewareUpdateRequestDto request) {
        
        log.info("Recibida solicitud de actualización middleware con {} parámetros", 
                request.getParametros().size());
        
        ParametrizacionResponseDto response = parametrizacionMiddlewareService
                .actualizarParametrizacionMiddleware(request);
        
        log.info("Actualización completada - Exitosos: {}, Fallidos: {}", 
                response.getExitosos(), response.getFallidos());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar parametrización de middleware",
              description = "Elimina una configuración existente de parametrización de middleware")
    @DeleteMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> eliminarParametrizacionMiddleware(
            @Valid @RequestBody ParametrizacionDeleteRequestDto request) {
        
        log.info("Recibida solicitud de eliminación middleware con {} parámetros", 
                request.getClavesPrimarias().size());
        
        ParametrizacionResponseDto response = parametrizacionMiddlewareService
                .eliminarParametrizacionMiddleware(request);
        
        log.info("Eliminación completada - Exitosos: {}, Fallidos: {}", 
                response.getExitosos(), response.getFallidos());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Consultar parametrización de middleware",
              description = "Consulta una configuración existente de parametrización de middleware")
    @PostMapping(value = "/consultar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParametrizacionMiddlewareDto>> consultarParametrizacionMiddleware(
            @Valid @RequestBody ParametrizacionQueryRequestDto request) {
        
        log.info("Recibida solicitud de consulta middleware con {} criterios", 
                request.getCriteriosBusqueda().size());
        
        List<ParametrizacionMiddlewareDto> response = parametrizacionMiddlewareService
                .consultarParametrizacionMiddleware(request);
        
        log.info("Consulta completada - {} registros encontrados", response.size());
        
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Estado de salud del servicio",
              description = "Verifica el estado de salud del servicio de parametrización de middleware")
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Parametrización Middleware Service is running");
    }
}
