package com.taxitel.backend.repository;

import com.taxitel.backend.entity.HistorialViaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HistorialViajeRepository extends JpaRepository<HistorialViaje, Long> {

    // La magia de Spring: Lee el nombre del método y crea la consulta SQL automáticamente
    Optional<HistorialViaje> findFirstByOrigenAndDestinoOrderByIdDesc(String origen, String destino);
}