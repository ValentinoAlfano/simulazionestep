/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package betting;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Random;

/**
 *
 * @author Valentino
 */
public class SimulazioneStep {

// 1 - SEQUENZE CLASSICHE
// 2 - TROTTO
//3 - TROTTO QUOTA VARIABILE
    static int INDICE = 3;
    static double bank = 0;
    static int stepTotali = 0;
    final static double QUOTA = 3.00;
    final static double OFFSET = 0.1;
    final static double QUOTA_MAX = 2.00;
    final static double QUOTA_MIN = 1.50;
    final static int VINCITA_PER_STEP = 50;
    final static int N_STEP = 2;
    final static LocalDate DATA_INIZIO = LocalDate.of(2018, 1, 1);
    final static LocalDate DATA_FINE = LocalDate.of(2019, 1, 1);
    final static DecimalFormat DF = new DecimalFormat("####0.00");

    static {

        System.out.println("Probabilità fallimento sequenza = " + DF.format(Math.pow((1 - (1 / QUOTA)), (double) N_STEP)));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        switch (INDICE) {
            case 1:
                lanciaSequenzeClassiche();
                break;
            case 2:
                lanciaTrotto();
                break;   
            case 3 : 
                lanciaTrottoQuotaVariabile();
                break;
            case 4 : 
                confrontaMetodi();
                break;
        }

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

            if (getRandomBoolean(1 / (QUOTA - OFFSET))) {
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

    public Sequenza generaSequenzaTrotto(double quota, LocalDate currentDate) {

        Sequenza seq = new Sequenza();
        seq.setEsposizione(2 + 6 + 16 + 45 + 65);
        //int puntatePrecedenti;
        int puntata;
        int esposizione;
        double vincita = 0;

        for (int i = 0; i < 5; i++) {

            seq.setStep(i + 1);
            int[] puntateArr = {2, 6, 16, 45, 65};

            seq.setPuntata(puntateArr[i]);

            System.out.println("Gioco " + (i + 1) + "° step, punto " + seq.getPuntata() + " u a quota " + quota);

            if (getRandomBoolean(1 / (QUOTA - OFFSET))) {
                vincita = calcolaVincita(puntateArr, i, QUOTA);
                seq.setVincita(vincita);
                System.out.println("Vinto");
                seq.setLost(false);
                break;
            } else {
                currentDate = currentDate.plusDays(1);
                System.out.println("Perso");
                vincita -= seq.getPuntata();
                //puntatePrecedenti = seq.getPuntatePrecedenti() + puntateArr[i];
                //seq.setPuntatePrecedenti(puntatePrecedenti);
                seq.setVincita(vincita);

                if (currentDate.isAfter(DATA_FINE)) {
                    seq.setLost(true);
                    break;
                }
            }
            seq.setLost(true);
        }

        return seq;
    }

    public Sequenza generaSequenzaTrottoQuotaVariabile(LocalDate currentDate) {

        Sequenza seq = new Sequenza();
        seq.setEsposizione(2 + 6 + 16 + 45 + 65);

        int puntata;
        double quota;
        double vincita = 0;

        for (int i = 0; i < 5; i++) {

            seq.setStep(i + 1);
            int[] puntateArr = {2, 6, 16, 45, 65};

            quota = Math.random() * (QUOTA_MAX - QUOTA_MIN) + QUOTA_MIN;

            seq.setPuntata(puntateArr[i]);

            System.out.println("Gioco " + (i + 1) + "° step, punto " + seq.getPuntata() + " u a quota " + quota);

            if (getRandomBoolean(1 / (quota - OFFSET))) {
                vincita = calcolaVincita(puntateArr, i, quota);
                seq.setVincita(vincita);
                System.out.println("Vinto");
                seq.setLost(false);
                break;
            } else {
                currentDate = currentDate.plusDays(1);
                System.out.println("Perso");
                vincita -= seq.getPuntata();

                seq.setVincita(vincita);

                if (currentDate.isAfter(DATA_FINE)) {
                    seq.setLost(true);
                    break;
                }
            }
            seq.setLost(true);
        }

        return seq;
    }

    public double calcolaVincita(int[] puntateArr, int i, double quota) {

        int sommaPuntate = 0;
        double vincita;
        int j = i;
        while (j > -1) {
            sommaPuntate += puntateArr[j];
            j--;
        }

        vincita = puntateArr[i] * quota - sommaPuntate;

        return vincita;

    }

    public static void lanciaSequenzeClassiche() {

        SimulazioneStep sim = new SimulazioneStep();
        int fails = 0;
        int i = 1;
        double maxBank = 0, minBank = 0;
        LocalDate maxBankDate = LocalDate.now();
        LocalDate minBankDate = LocalDate.now();

       
            for (LocalDate currentDate = DATA_INIZIO; !(currentDate.isAfter(DATA_FINE)); i++) {
                System.out.println("Sequenza n. " + i + " inizia il " + currentDate);
                System.out.println("Cassa = " + bank + " u");

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
                System.out.println("Data attuale = " + currentDate);
                System.out.println("Esposizione nella sequenza = " + seq.getEsposizione());

            }

            System.out.println("Registrate " + fails + " sequenze fallite");
            System.out.println("Cassa max = " + DF.format(maxBank) + " rilevata il " + maxBankDate);
            System.out.println("Cassa min = " + DF.format(minBank) + " rilevata il " + minBankDate);
            System.out.println("Cassa finale = " + DF.format(bank));
            System.out.println("Eventi totali giocati = " + stepTotali);
            System.out.println("Vincita media giornaliera = " + DF.format(bank / stepTotali) + " u");

        

           
        

    }
    
    public static void lanciaTrotto() {
        
        SimulazioneStep sim = new SimulazioneStep();
        int fails = 0;
        int i = 1;
        double maxBank = 0, minBank = 0;
        LocalDate maxBankDate = LocalDate.now();
        LocalDate minBankDate = LocalDate.now();

        
            for (LocalDate currentDate = DATA_INIZIO; !(currentDate.isAfter(DATA_FINE)); i++) {
                System.out.println("Sequenza n. " + i + " inizia il " + currentDate);
                System.out.println("Cassa = " + bank + " u");

                if (bank > maxBank) {
                    maxBankDate = currentDate;
                    maxBank = bank;
                } else {
                    if (bank < minBank) {
                        minBankDate = currentDate;
                        minBank = bank;
                    }
                }
                
                Sequenza seq = sim.generaSequenzaTrotto(QUOTA, currentDate);
                
                if (seq.getIsLost()) {
                    System.out.println(seq.getStep() + "° step perso. Sequenza fallita.");
                    fails++;
                }
                bank += seq.getVincita();
                stepTotali += seq.getStep();

                currentDate = currentDate.plusDays(seq.getStep());
                System.out.println("Data attuale = " + currentDate);
                System.out.println("Esposizione nella sequenza = " + seq.getEsposizione());

            }

            System.out.println("Registrate " + fails + " sequenze fallite");
            System.out.println("Cassa max = " + DF.format(maxBank) + " rilevata il " + maxBankDate);
            System.out.println("Cassa min = " + DF.format(minBank) + " rilevata il " + minBankDate);
            System.out.println("Cassa finale = " + DF.format(bank));
            System.out.println("Eventi totali giocati = " + stepTotali);
            System.out.println("Vincita media giornaliera = " + DF.format(bank / stepTotali) + " u");

        

        
    }
    
    public static void lanciaTrottoQuotaVariabile() {
        
        

        SimulazioneStep sim = new SimulazioneStep();
        int fails = 0;
        int i = 1;
        double maxBank = 0, minBank = 0;
        LocalDate maxBankDate = LocalDate.now();
        LocalDate minBankDate = LocalDate.now();

        
            for (LocalDate currentDate = DATA_INIZIO; !(currentDate.isAfter(DATA_FINE)); i++) {
                System.out.println("Sequenza n. " + i + " inizia il " + currentDate);
                System.out.println("Cassa = " + bank + " u");

                if (bank > maxBank) {
                    maxBankDate = currentDate;
                    maxBank = bank;
                } else {
                    if (bank < minBank) {
                        minBankDate = currentDate;
                        minBank = bank;
                    }
                }

                Sequenza seq = sim.generaSequenzaTrottoQuotaVariabile(currentDate);

                if (seq.getIsLost()) {
                    System.out.println(seq.getStep() + "° step perso. Sequenza fallita.");
                    fails++;
                }
                bank += seq.getVincita();
                stepTotali += seq.getStep();

                currentDate = currentDate.plusDays(seq.getStep());
                System.out.println("Data attuale = " + currentDate);
                System.out.println("Esposizione nella sequenza = " + seq.getEsposizione());

            }

            System.out.println("Registrate " + fails + " sequenze fallite");
            System.out.println("Cassa max = " + DF.format(maxBank) + " rilevata il " + maxBankDate);
            System.out.println("Cassa min = " + DF.format(minBank) + " rilevata il " + minBankDate);
            System.out.println("Cassa finale = " + DF.format(bank));
            System.out.println("Eventi totali giocati = " + stepTotali);
            System.out.println("Vincita media giornaliera = " + DF.format(bank / stepTotali) + " u");

        

        
    }
    
    public static void confrontaMetodi() {
        
        
        
    }
}
