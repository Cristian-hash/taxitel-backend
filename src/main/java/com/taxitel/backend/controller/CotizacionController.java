package com.taxitel.backend.controller;

import com.taxitel.backend.dto.CotizacionRequest;
import com.taxitel.backend.dto.CotizacionResponse;
import com.taxitel.backend.dto.NuevoTramoRequest;
import com.taxitel.backend.exception.RutaNoEncontradaException;
import com.taxitel.backend.service.CotizacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cotizaciones")
@CrossOrigin(origins = "*") // Permiso vital para que Angular pueda hablarle
public class CotizacionController {

    private final CotizacionService cotizacionService;

    // Inyectamos el "Cerebro" (Service) en el Recepcionista
    public CotizacionController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    // Endpoint principal: Escucha en http://localhost:8080/api/cotizaciones/calcular
    @PostMapping("/calcular")
    public CotizacionResponse calcular(@RequestBody CotizacionRequest request) {
        // CORRECCIÓN: Cambiamos 'procesarCotizacion' por 'calcularCotizacion'
        return cotizacionService.calcularCotizacion(request);
    }

    // La nueva puerta que recibe los S/ 10 desde el Modal de Angular
    @PostMapping("/nuevo-tramo")
    public void guardarNuevoTramo(@RequestBody NuevoTramoRequest request) {
        cotizacionService.guardarNuevoTramo(request);
    }

    // EL PARACAÍDAS: Atrapa el error y le avisa a Angular
    @ExceptionHandler(RutaNoEncontradaException.class)
    public ResponseEntity<String> manejarRutaNoEncontrada(RutaNoEncontradaException ex) {
        // Si el Service lanza la excepción, este método la atrapa mágicamente.
        // Responde con un código 404 (No Encontrado) y el mensaje de qué tramo falta.
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

}