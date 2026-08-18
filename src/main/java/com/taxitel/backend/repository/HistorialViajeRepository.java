package com.taxitel.backend.repository;

import com.taxitel.backend.entity.HistorialViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HistorialViajeRepository extends JpaRepository<HistorialViaje, Long> {

    // La magia de Spring: Lee el nombre del método y crea la consulta SQL automáticamente
    Optional<HistorialViaje> findFirstByOrigenAndDestinoOrderByIdDesc(String origen, String destino);
    // Consulta SQL nativa que une orígenes y destinos eliminando duplicados mágicamente
    @Query(value = "SELECT origen FROM historial_viajes UNION SELECT destino FROM historial_viajes", nativeQuery = true)
    List<String> findRutasUnicas();
}