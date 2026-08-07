package com.taxitel.backend.service;

import com.taxitel.backend.dto.CotizacionRequest;
import com.taxitel.backend.dto.CotizacionResponse;
import com.taxitel.backend.dto.NuevoTramoRequest;
import com.taxitel.backend.dto.Tramo;
import com.taxitel.backend.entity.HistorialViaje;
import com.taxitel.backend.repository.HistorialViajeRepository;
import com.taxitel.backend.exception.RutaNoEncontradaException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CotizacionService {

    private final HistorialViajeRepository historialRepository;

    public CotizacionService(HistorialViajeRepository historialRepository) {
        this.historialRepository = historialRepository;
    }

    public CotizacionResponse calcularCotizacion(CotizacionRequest request) {
        List<String> paradas = request.getParadas();
        List<Tramo> tramosDesglosados = new ArrayList<>();
        double tarifaBaseConsolidada = 0.0;

        // 1. CÁLCULO DE RUTAS
        for (int i = 0; i < paradas.size() - 1; i++) {
            String origenTramo = paradas.get(i);
            String destinoTramo = paradas.get(i + 1);

            HistorialViaje viajeGuardado = historialRepository.findFirstByOrigenAndDestinoOrderByIdDesc(origenTramo, destinoTramo)
                    .orElseThrow(() -> new RutaNoEncontradaException("Falta precio para: " + origenTramo + " a " + destinoTramo));

            double precioTramo = viajeGuardado.getTarifaBase();

            Tramo tramoDTO = new Tramo();
            tramoDTO.setOrigen(origenTramo);
            tramoDTO.setDestino(destinoTramo);
            tramoDTO.setTarifaBase(precioTramo);

            tramosDesglosados.add(tramoDTO);
            tarifaBaseConsolidada += precioTramo;
        }

        // 2. CÁLCULO DE MENSAJERÍA
        double recargoMensajeria = request.isTieneMensajeria() ? 2.00 : 0.00;

        // 3. LA NUEVA MAGIA: CÁLCULO DE TOLERANCIA DE ESPERA
        String nombreEmpresa = request.getEmpresa() != null ? request.getEmpresa().toUpperCase().trim() : "";
        int tolerancia = 5; // Tolerancia general por defecto (5 minutos)

        // Reglas de negocio VIP
        if (nombreEmpresa.equals("KOMATSU MITSUI")) {
            tolerancia = 15;
        } else if (nombreEmpresa.equals("RICO POLLO")) {
            tolerancia = 7;
        }

        // Restamos la tolerancia. (Si esperó 20 min y la tolerancia es 15, cobramos 5).
        // Usamos Math.max para que si esperó menos de la tolerancia, no salgan números negativos, sino 0.
        int minutosCobrables = Math.max(0, request.getMinutosEspera() - tolerancia);

        double bloquesEspera = Math.ceil((double) minutosCobrables / 3);
        double recargoEspera = bloquesEspera * 1.00;

        // 4. CONSOLIDACIÓN FINAL
        double totalAPagar = tarifaBaseConsolidada + recargoMensajeria + recargoEspera;

        CotizacionResponse response = new CotizacionResponse();
        response.setTramos(tramosDesglosados);
        response.setTarifaBaseTotal(tarifaBaseConsolidada);
        response.setRecargoMensajeria(recargoMensajeria);
        response.setRecargoEspera(recargoEspera);
        response.setTotal(totalAPagar);

        return response;
    }

    public void guardarNuevoTramo(NuevoTramoRequest request) {
        HistorialViaje nuevo = new HistorialViaje();
        nuevo.setEmpresa("GENERAL");
        nuevo.setOrigen(request.getOrigen());
        nuevo.setDestino(request.getDestino());
        nuevo.setTarifaBase(request.getTarifaBase());
        historialRepository.save(nuevo);
    }
}