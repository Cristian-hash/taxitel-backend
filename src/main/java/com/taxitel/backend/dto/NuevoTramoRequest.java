package com.taxitel.backend.dto;

public class NuevoTramoRequest {
    private String empresa; // <-- NUEVO COMPARTIMIENTO
    private String origen;
    private String destino;
    private Double tarifaBase;
    public String getEmpresa() { return empresa; }
    public void setEmpresa(String empresa) { this.empresa = empresa; }

    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }

    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }

    public Double getTarifaBase() { return tarifaBase; }
    public void setTarifaBase(Double tarifaBase) { this.tarifaBase = tarifaBase; }
}