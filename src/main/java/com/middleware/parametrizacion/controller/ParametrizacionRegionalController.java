package com.middleware.parametrizacion.controller;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.service.ParametrizacionRegionalService;
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
@Tag(name = "Parametrización Regional", description = "API para la gestión de parametrización regional")
@RequestMapping(value = "/parametrizacion/regional", produces = MediaType.APPLICATION_JSON_VALUE)
public class ParametrizacionRegionalController {
    
    private final ParametrizacionRegionalService service;

    @Operation(summary = "Crear una nueva parametrización regional", 
               description = "Crea una nueva configuración de parametrización regional con los datos proporcionados")
    @PostMapping(value = "/crear", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> crearParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalRequestDto request) {
        log.info("Recibida solicitud para crear parametrización regional");
        return ResponseEntity.ok(service.procesarParametrizacionRegional(request));
    }

    @Operation(summary = "Consultar parametrizaciones regionales", 
               description = "Consulta las parametrizaciones regionales según los criterios de búsqueda")
    @PostMapping(value = "/consultar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MwServiciosRegionalesDto>> consultarParametrizacion(
            @Valid @RequestBody ParametrizacionQueryRequestDto request) {
        log.info("Consultando parametrización regional con criterios: {}", request);
        return ResponseEntity.ok(service.consultarParametrizacionRegional(request));
    }

    @Operation(summary = "Actualizar una parametrización regional existente", 
               description = "Actualiza una configuración de parametrización regional existente")
    @PutMapping(value = "/actualizar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> actualizarParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalUpdateRequestDto request) {
        log.info("Recibida solicitud para actualizar parametrización regional");
        return ResponseEntity.ok(service.actualizarParametrizacionRegional(request));
    }

    @Operation(summary = "Eliminar una parametrización regional", 
               description = "Elimina una configuración de parametrización regional existente")
    @DeleteMapping(value = "/eliminar", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParametrizacionResponseDto> eliminarParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalRequestDto request) {
        log.info("Recibida solicitud para eliminar parametrización regional");
        return ResponseEntity.ok(service.eliminarParametrizacionRegional(request));
    }
}