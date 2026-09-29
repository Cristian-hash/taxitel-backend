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

        // 1. CÁLCULO DE RUTAS BIDIRECCIONAL (TARIFA UNIVERSAL)
        for (int i = 0; i < paradas.size() - 1; i++) {
            // 🌟 EL FILTRO DE TITANIO: Limpia espacios dobles y vacíos a los extremos antes de buscar
            String origenTramo = paradas.get(i).trim().replaceAll("\\s+", " ");
            String destinoTramo = paradas.get(i + 1).trim().replaceAll("\\s+", " ");
            
            // 🌟 BUSCAMOS LA RUTA (Solo nos importa Origen y Destino)
            java.util.Optional<HistorialViaje> viajeOpt = historialRepository.encontrarRutaEspejo(
                    origenTramo,
                    destinoTramo
            );

            // Si nadie en la historia de la empresa ha hecho esta ruta, pedimos precio
            HistorialViaje viajeGuardado = viajeOpt.orElseThrow(() ->
                    new RutaNoEncontradaException("Falta precio global para: " + origenTramo + " a " + destinoTramo)
            );

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

        // 3. CÁLCULO DIRECTO DE ESPERA
        double bloquesEspera = Math.ceil((double) request.getMinutosEspera() / 3);
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
        nuevo.setEmpresa(request.getEmpresa() != null ? request.getEmpresa().toUpperCase() : "GENERAL");
        nuevo.setOrigen(request.getOrigen());
        nuevo.setDestino(request.getDestino());
        nuevo.setTarifaBase(request.getTarifaBase());

        historialRepository.save(nuevo);
    }

    public List<String> obtenerRutasUnicas() {
        return historialRepository.findRutasUnicas();
    }
}