/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.time.LocalDate;
import java.util.Random;

/**
 *
 * @author Valentino
 */
public class SimulazioneStep {

    //static final int N_SEQUENZE = 150;
    static int bank = 0;
    static int stepTotali = 0;
    final static double QUOTA = 1.6;
    final static int VINCITA_PER_STEP = 50;
    final static int N_STEP = 2;
    final static LocalDate DATA_INIZIO = LocalDate.of(2018, 1, 1);
    final static LocalDate DATA_FINE = LocalDate.of(2019, 1, 1);

    static {
        System.out.println("Probabilità fallimento sequenza = " + Math.pow((1 - (1 / QUOTA)), (double) N_STEP));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SimulazioneStep sim = new SimulazioneStep();
        int fails = 0;

        int i = 1;
        int maxBank = 0, minBank = 0;
        LocalDate maxBankDate = LocalDate.now();
        LocalDate minBankDate = LocalDate.now();

        for (LocalDate currentDate = DATA_INIZIO; !(currentDate.isAfter(DATA_FINE)); i++) {
            System.out.println("Sequenza n. " + i + " del " + currentDate);
            System.out.println("Cassa corrente = " + bank);

            if (bank > maxBank) {
                maxBankDate = currentDate;
                maxBank = bank;
            } else {
                if (bank < minBank) {
                    minBankDate = currentDate;
                    minBank = bank;
                }
            }

            Sequenza seq = sim.generaSequenza(QUOTA, VINCITA_PER_STEP, N_STEP, currentDate);

            if (seq.getIsLost()) {
                System.out.println(seq.getStep() + "° step perso. Sequenza fallita.");
                fails++;
            }
            bank += seq.getVincita();
            stepTotali += seq.getStep();

            currentDate = currentDate.plusDays(seq.getStep());
            System.out.println("Cazzo = " + currentDate);
            System.out.println("Esposizione nella sequenza = " + seq.getEsposizione());

        }

        System.out.println("Registrate " + fails + " sequenze fallite");
        System.out.println("Cassa max = " + maxBank + " rilevata il " + maxBankDate);
        System.out.println("Cassa min = " + minBank + " rilevata il " + minBankDate);
        System.out.println("Cassa finale = " + bank);
        System.out.println("Eventi totali = " + stepTotali);
        System.out.println("Vincita media giornaliera = " + bank/stepTotali);
    }

    public static boolean getRandomBoolean(double p) { //generatore di infelicità
        Random random = new Random();
        return random.nextDouble() < p;
    }

    public static void provaEsitoRandom() {
        double quota = 10;
        System.out.println("Simulo 1000 eventi a quota " + quota);
        int t = 0, f = 0;
        for (int i = 0; i < 1000; i++) {
            if (getRandomBoolean(1 / (quota))) {
                t++; // se è uscito true aggiungo un caso vinto
            } else {
                f++;    // altrimenti aggiungo un caso perso             
            }
        }
        System.out.println("Vinti = " + t + " Persi = " + f);

    }

    /**
     *
     * @param quota
     * @param vincitaPerStep
     * @param numeroStep
     * @param currentDate
     * @return
     */
    public Sequenza generaSequenza(double quota, int vincitaPerStep, int numeroStep, LocalDate currentDate) {
        Sequenza seq = new Sequenza();

        int puntatePrecedenti;
        int puntata;
        int esposizione;
        int vincita = 0;

        for (int i = 1; i <= numeroStep; i++) {

            seq.setStep(i);
            puntata = (int) Math.round((vincitaPerStep * i + seq.getPuntatePrecedenti()) / (quota - 1));

            if (puntata < 2) {
                puntata = 2;
            }
            seq.setPuntata(puntata);

            seq.setEsposizione(seq.getPuntata() + seq.getPuntatePrecedenti());

            System.out.println("Gioco " + i + "° step, punto " + seq.getPuntata() + " per vincere " + vincitaPerStep + " a quota " + quota);

            if (getRandomBoolean(1 / (quota))) {
                seq.setVincita(vincitaPerStep * i);
                System.out.println("Vinto");
                seq.setLost(false);
                break;
            } else {
                currentDate = currentDate.plusDays(1);
                System.out.println("Perso");
                vincita -= seq.getPuntata();
                puntatePrecedenti = seq.getPuntatePrecedenti() + puntata;
                seq.setPuntatePrecedenti(puntatePrecedenti);
                seq.setVincita(vincita);

                if (seq.getStep() == numeroStep) {
                    seq.setLost(true);
                    return seq;
                }
                if (currentDate.isAfter(DATA_FINE)) {
                    seq.setLost(true);
                    break;
                }
            }

        }

        return seq;
    }

}

class Sequenza {

    private int puntatePrecedenti;
    private int puntata;
    private int vincita;
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

    public int getVincita() {
        return vincita;
    }

    public void setVincita(int vincita) {
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
