package org.joselopez.bean;


public class Cargo {
    private int codigoCargo;
    private String nombreDepartamento;

    public Cargo() {
    }

    public Cargo(int codigoCargo, String nombreDepartamento) {
        this.codigoCargo = codigoCargo;
        this.nombreDepartamento = nombreDepartamento;
    }

    public int getCodigoCargo() {
        return codigoCargo;
    }

    public void setCodigoCargo(int codigoCargo) {
        this.codigoCargo = codigoCargo;
    }

    public String getNombreDepartamento() {
        return nombreDepartamento;
    }

    public void setNombreDepartamento(String nombreDepartamento) {
        this.nombreDepartamento = nombreDepartamento;
    }
    
    
}
