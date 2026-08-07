package com.taxitel.backend.dto;

import java.util.List;

public class CotizacionRequest {

    private List<String> paradas;
    private boolean tieneMensajeria;
    private int minutosEspera;

    public CotizacionRequest() {
    }

    public List<String> getParadas() {
        return paradas;
    }

    public void setParadas(List<String> paradas) {
        this.paradas = paradas;
    }

    public boolean isTieneMensajeria() {
        return tieneMensajeria;
    }

    public void setTieneMensajeria(boolean tieneMensajeria) {
        this.tieneMensajeria = tieneMensajeria;
    }

    public int getMinutosEspera() {
        return minutosEspera;
    }

    public void setMinutosEspera(int minutosEspera) {
        this.minutosEspera = minutosEspera;
    }
}