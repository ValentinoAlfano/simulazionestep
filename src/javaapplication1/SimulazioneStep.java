/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication1;

import java.util.Random;

/**
 *
 * @author Valentino
 */
public class SimulazioneStep {

    //int puntata, puntatePrecedenti;
    //static final int vincita = 10; 
    //static final float QUOTA = 2.00F; 
    //static final float PROBABILITA = 1 / QUOTA;
    //static float cassa = 1000F;
    //static final byte N_STEP = 10;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here        
        //provaEsitoRandom();
        SimulazioneStep sim = new SimulazioneStep();
        int fails = 0;
        
        for(int i = 1; i <= 10; i++) { 
            if(!sim.generaSequenza(1.6, 10, 3, 1000)) {
                fails++;
            }
        }
        
        System.out.println("Registrate " + fails + " sequenze fallite");
    }

    public static boolean getRandomBoolean(double p) {
        Random random = new Random();
        return random.nextDouble() < p;
    }

    public static void provaEsitoRandom() {
        double quota = 1.60;
        System.out.println("Simulo 1000 eventi a quota " + quota);
        int t = 0, f = 0;
        for (int i = 0; i < 1000; i++) {
            if (getRandomBoolean(1 / quota)) {
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
     * @param vincita
     * @param numeroStep
     * @param cassa
     * @return 
     */
    public boolean generaSequenza(double quota, int vincita, int numeroStep, int cassa) {
        int esposizione = 0;
        int puntatePrecedenti = 0;
        int puntata;
        

        for (int i = 1; i <= numeroStep; i++) {

            puntata = (int) Math.round((vincita + puntatePrecedenti) / (quota - 1));
            System.out.println("Gioco " + i + "° step, punto " + puntata + " per vincere " + vincita + " a quota " + quota);
            
            if (getRandomBoolean(1 / quota)) {
                esposizione += puntata;
                cassa += vincita;
                System.out.println("Vinto");
                break;
            } else {
                System.out.println("Perso");
                cassa -= puntata;
                puntatePrecedenti += puntata;
                esposizione += puntata;
                if(i == numeroStep) {                    
                    return hasFailed(i);
                }
            }

        }
        System.out.println("Esposizione per la sequenza = " + esposizione);
        System.out.println("Cassa alla fine della sequenza = " + cassa);
        return true;

    }

    private boolean hasFailed(int i) {
       System.out.println(i + "° step perso. Sequenza fallita.");
       return false;
    }
}
