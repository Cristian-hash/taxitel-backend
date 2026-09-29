package com.taxitel.backend.repository;

import com.taxitel.backend.entity.HistorialViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HistorialViajeRepository extends JpaRepository<HistorialViaje, Long> {

    // 🌟 1. EL EFECTO ESPEJO (TARIFA UNIVERSAL): Busca la ruta sin importar la empresa
    @Query(value = "SELECT * FROM historial_viajes h " +
            "WHERE ( " +
            "  (h.origen = :puntoA AND h.destino = :puntoB) " +
            "  OR " +
            "  (h.origen = :puntoB AND h.destino = :puntoA) " +
            ") " +
            "ORDER BY h.id DESC LIMIT 1",
            nativeQuery = true)
    Optional<HistorialViaje> encontrarRutaEspejo(
            @Param("puntoA") String puntoA,
            @Param("puntoB") String puntoB
    );

    // 🌟 2. EL RECOLECTOR DE MEMORIA: Une orígenes y destinos eliminando duplicados
    @Query(value = "SELECT origen FROM historial_viajes UNION SELECT destino FROM historial_viajes", nativeQuery = true)
    List<String> findRutasUnicas();
}