package com.urna.bean.UrnaBean;

public class UrnaBean {
    private String codigoUrna;
    private boolean habilitada;

    public UrnaBean(String codigoUrna) {
        this.codigoUrna = codigoUrna;
        this.habilitada = false;
    }

    public boolean habilitarUrna(String codigoAcesso) {
        if (codigoAcesso.equals("005005")) {
            this.habilitada = true;
            return true;
        }
        return false;
    }

    public boolean isHabilitada() {
        return habilitada;
    }

    public String getCodigoUrna() {
        return codigoUrna;
    }

    public void setCodigoUrna(String codigoUrna) {
        this.codigoUrna = codigoUrna;
    }
}