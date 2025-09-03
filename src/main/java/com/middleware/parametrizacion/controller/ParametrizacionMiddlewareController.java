package com.middleware.parametrizacion.controller;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.service.ParametrizacionMiddlewareService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parametrizacion/middleware")
@RequiredArgsConstructor
@Slf4j
public class ParametrizacionMiddlewareController {

    private final ParametrizacionMiddlewareService parametrizacionMiddlewareService;

    @PostMapping
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

    @PutMapping
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

    @DeleteMapping
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

    @PostMapping("/consultar")
    public ResponseEntity<List<ParametrizacionMiddlewareDto>> consultarParametrizacionMiddleware(
            @Valid @RequestBody ParametrizacionQueryRequestDto request) {
        
        log.info("Recibida solicitud de consulta middleware con {} criterios", 
                request.getCriteriosBusqueda().size());
        
        List<ParametrizacionMiddlewareDto> response = parametrizacionMiddlewareService
                .consultarParametrizacionMiddleware(request);
        
        log.info("Consulta completada - {} registros encontrados", response.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Parametrización Middleware Service is running");
    }
}
