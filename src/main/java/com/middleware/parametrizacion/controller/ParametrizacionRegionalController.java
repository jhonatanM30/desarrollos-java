package com.middleware.parametrizacion.controller;

import com.middleware.parametrizacion.dto.*;
import com.middleware.parametrizacion.service.ParametrizacionRegionalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/parametrizacion/regional")
public class ParametrizacionRegionalController {
    
    private final ParametrizacionRegionalService service;

    @PostMapping("/crear")
    public ResponseEntity<ParametrizacionResponseDto> crearParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalRequestDto request) {
        log.info("Recibida solicitud para crear parametrización regional");
        return ResponseEntity.ok(service.procesarParametrizacionRegional(request));
    }

    @PostMapping("/consultar")
    public ResponseEntity<List<MwServiciosRegionalesDto>> consultarParametrizacion(
            @Valid @RequestBody ParametrizacionQueryRequestDto request
           ) {
        log.info("Consultando parametrización regional con criterios: {}", request);
        return ResponseEntity.ok(service.consultarParametrizacionRegional(request));
    }

    @PutMapping("/actualizar")
    public ResponseEntity<ParametrizacionResponseDto> actualizarParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalUpdateRequestDto request) {
        log.info("Recibida solicitud para actualizar parametrización regional");
        return ResponseEntity.ok(service.actualizarParametrizacionRegional(request));
    }

    @DeleteMapping("/eliminar")
    public ResponseEntity<ParametrizacionResponseDto> eliminarParametrizacion(
            @Valid @RequestBody ParametrizacionRegionalRequestDto request) {
        log.info("Recibida solicitud para eliminar parametrización regional");
        return ResponseEntity.ok(service.eliminarParametrizacionRegional(request));
    }
}