package com.taxitel.backend.dto;

import java.util.List;

public class CotizacionResponse {

    // Usamos el bloque Tramo externo que Angular y tu Service ya conocen
    private List<Tramo> tramos;

    // Añadimos todas las variables matemáticas que el Service está intentando guardar
    private double tarifaBaseTotal;
    private double recargoMensajeria;
    private double recargoEspera;
    private double total;

    // --- GETTERS Y SETTERS --- //

    public List<Tramo> getTramos() {
        return tramos;
    }
    public void setTramos(List<Tramo> tramos) {
        this.tramos = tramos;
    }

    public double getTarifaBaseTotal() {
        return tarifaBaseTotal;
    }
    public void setTarifaBaseTotal(double tarifaBaseTotal) {
        this.tarifaBaseTotal = tarifaBaseTotal;
    }

    public double getRecargoMensajeria() {
        return recargoMensajeria;
    }
    public void setRecargoMensajeria(double recargoMensajeria) {
        this.recargoMensajeria = recargoMensajeria;
    }

    public double getRecargoEspera() {
        return recargoEspera;
    }
    public void setRecargoEspera(double recargoEspera) {
        this.recargoEspera = recargoEspera;
    }

    public double getTotal() {
        return total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
}