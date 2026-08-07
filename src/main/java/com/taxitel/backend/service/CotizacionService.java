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
        // Usamos la clase Tramo exacta que Angular espera
        List<Tramo> tramosDesglosados = new ArrayList<>();
        double tarifaBaseConsolidada = 0.0;

        for (int i = 0; i < paradas.size() - 1; i++) {
            String origenTramo = paradas.get(i);
            String destinoTramo = paradas.get(i + 1);

            HistorialViaje viajeGuardado = historialRepository.findFirstByOrigenAndDestinoOrderByIdDesc(origenTramo, destinoTramo)
                    .orElseThrow(() -> new RutaNoEncontradaException("Falta precio para: " + origenTramo + " a " + destinoTramo));

            double precioTramo = viajeGuardado.getTarifaBase();

            // Armamos el molde de Lego exacto para Angular
            Tramo tramoDTO = new Tramo();
            tramoDTO.setOrigen(origenTramo);
            tramoDTO.setDestino(destinoTramo);
            tramoDTO.setTarifaBase(precioTramo);

            tramosDesglosados.add(tramoDTO);
            tarifaBaseConsolidada += precioTramo;
        }

        double recargoMensajeria = request.isTieneMensajeria() ? 2.00 : 0.00;
        double bloquesEspera = Math.ceil((double) request.getMinutosEspera() / 3);
        double recargoEspera = bloquesEspera * 1.00;
        double totalAPagar = tarifaBaseConsolidada + recargoMensajeria + recargoEspera;

        // Construimos el empaque completo para Angular
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