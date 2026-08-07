package com.taxitel.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "historial_viajes")
public class HistorialViaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "empresa", length = 100)
    private String empresa;

    @Column(name = "origen", nullable = false, length = 255)
    private String origen;

    @Column(name = "destino", nullable = false, length = 255)
    private String destino;

    @Column(name = "tarifa_base", nullable = false)
    private Double tarifaBase;

    // Constructor vacío obligatorio para Spring Boot
    public HistorialViaje() {
    }

    // Constructor con parámetros
    public HistorialViaje(String empresa, String origen, String destino, Double tarifaBase) {
        this.empresa = empresa;
        this.origen = origen;
        this.destino = destino;
        this.tarifaBase = tarifaBase;
    }

    // Getters y Setters (Para que el sistema pueda leer y escribir los datos)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Double getTarifaBase() {
        return tarifaBase;
    }

    public void setTarifaBase(Double tarifaBase) {
        this.tarifaBase = tarifaBase;
    }
}