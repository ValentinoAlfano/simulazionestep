/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

/**
 *
 * @author Valentino
 */
class Sequenza {

    private int puntatePrecedenti;
    private int puntata;
    private double vincita;
    private int step;
    private int esposizione;

    public int getEsposizione() {
        return esposizione;
    }

    public void setEsposizione(int esposizione) {
        this.esposizione = esposizione;
    }
    private boolean isLost;

    public int getPuntatePrecedenti() {
        return puntatePrecedenti;
    }

    public void setPuntatePrecedenti(int puntatePrecedenti) {
        this.puntatePrecedenti = puntatePrecedenti;
    }

    public int getPuntata() {
        return puntata;
    }

    public void setPuntata(int puntata) {
        this.puntata = puntata;
    }

    public double getVincita() {
        return vincita;
    }

    public void setVincita(double vincita) {
        this.vincita = vincita;
    }
    

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public boolean getIsLost() {
        return isLost;
    }

    public void setLost(boolean isLost) {
        this.isLost = isLost;
    }

}
