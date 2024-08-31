/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Manel
 */
public class ActivitateCondus {

    boolean nocturn = false;
    String timpTotal;
    String oraInceput;
    String oraIncheiere;
    boolean valid = true;
    boolean work = false;
    String dataCalendaristica;

    private boolean eIntValid(String x) {
        try {
            int i = Integer.valueOf(x);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    boolean eValida() {
        return valid;
    }

    private boolean eNocturna() {
        int oraInc = Integer.valueOf(oraInceput.split(":")[0]);
        int oraFin = Integer.valueOf(oraIncheiere.split(":")[0]);

        boolean inceputNocturn = false;
        boolean finalizareNocturna = false;

        for (int i = 0; i < Danaral.oreNoapte.length; i++) {
            if (Danaral.oreNoapte[i] == oraInc) {
                inceputNocturn = true;
            }
            if (Danaral.oreNoapte[i] == oraFin) {
                finalizareNocturna = true;
            }
        }

        return inceputNocturn || finalizareNocturna;
    }

    /**
     *
     * @param linie format : driving for 0:10 h: driving, from 3:42 to 3:52
     * (0:10 h): Card inserted, driver slot, single
     * @param dataCalendaristica
     */
    public ActivitateCondus(String linie, String dataCalendaristica) {
        String ar[] = linie.split(" ");
        /* if (eIntValid(ar[0])) {
            String nou = "";
            for (int i = 3; i < ar.length; i++) {
                nou += ar[i] + " ";
            }
            ar = nou.split(" ");
        }*/
        this.dataCalendaristica = dataCalendaristica;

        if (ar[0].contains("driving")) {
            timpTotal = ar[2] + "h";
            oraInceput = Danaral.getDanaral().GetOraConvertedToRomanianTime(ar[6], dataCalendaristica);
            oraIncheiere = Danaral.getDanaral().GetOraConvertedToRomanianTime(ar[8], dataCalendaristica);
            nocturn = eNocturna();
            work = false;
        } else if (ar[0].contains("work")) {
            timpTotal = ar[2] + "h";
            oraInceput = Danaral.getDanaral().GetOraConvertedToRomanianTime(ar[6], dataCalendaristica);
            oraIncheiere = Danaral.getDanaral().GetOraConvertedToRomanianTime(ar[8], dataCalendaristica);
            nocturn = eNocturna();
            work = true;
        } else {
            valid = false;
        }
    }

    /**
     * Iti returneaza un tablou care contine pe prima pozitie
     * {@link ActivitateCondus} in tura de noapte !!! de la inceperea turei pana
     * la ora 6 dimineata!!! si pe a doua pozitie {@link ActivitateCondus} in
     * tura de zi incepand cu ora 6 pana la finalizarea turei Sa fie apelat doar
     * daca a fost prima data verificat
     * {@link RaportZi#condusNoapteaSiZiua(danaral.ActivitateCondus)} ca fiind
     * {@link Boolean#TRUE}
     *
     * @return am explicat mai sus
     */
    public ActivitateCondus[] desparteNoapteSiZi() {
        ActivitateCondus[] ar = new ActivitateCondus[2];
        ActivitateCondus noaptea = new ActivitateCondus(this.oraInceput, "07:00",
                true, this.work, dataCalendaristica);
        ActivitateCondus ziua = new ActivitateCondus("07:00", this.oraIncheiere,
                false, this.work, dataCalendaristica);
        ar[0] = noaptea;
        ar[1] = ziua;
        return ar;
    }

    public ActivitateCondus[] desparteZiSiNoapte() {
        ActivitateCondus[] ar = new ActivitateCondus[2];
        ActivitateCondus ziua = new ActivitateCondus(this.oraInceput, "24:00",
                false, this.work, dataCalendaristica);
        ActivitateCondus noaptea = new ActivitateCondus("00:00", this.oraIncheiere,
                true, this.work, dataCalendaristica);

        ar[0] = noaptea;
        ar[1] = ziua;
        return ar;
    }

    final String calculeazaTimp(String oraIncepere, String oraIncheiere) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date1 = format.parse(oraIncepere);
        Date date2 = format.parse(oraIncheiere);
        long difference = date2.getTime() - date1.getTime();
        int ore = (int) (TimeUnit.MILLISECONDS.toHours(difference) % 24);
        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(difference) % 60;
        if (ore < 0) {
            ore = 24 + ore;
        }
        if (minute < 0) {
            minute = 60 + minute;
            ore--;
        }
        String minuteString = minute + "";
        if (minute < 10) {
            minuteString = "0" + minute;
        }

        return Long.toString(ore) + ":"
                + minuteString;
    }

    public ActivitateCondus(String oraInceput, String oraIncheiere,
            boolean nocturn, boolean work, String dataCalendaristica) {
        this.oraInceput = oraInceput;
        this.oraIncheiere = oraIncheiere;
        this.nocturn = nocturn;
        this.work = work;
        this.valid = true;
        this.dataCalendaristica = dataCalendaristica;
        try {
            this.timpTotal = calculeazaTimp(oraInceput, oraIncheiere) + "h";
        } catch (ParseException ex) {
            System.out.println("Eroare cand am incercat sa calculez timpul intre orele"
                    + oraInceput + " " + oraIncheiere);
        }
    }

    String getActivitate() {
        return (work ? "Alta munca" : "Condus");
    }

    String getTura() {
        return (nocturn ? "noapte" : "zi");
    }

    String getTimp() {
        return (timpTotal);
    }

    String getOraInceput() {
        return oraInceput;
    }

    String getOraIncheiere() {
        return oraIncheiere;
    }

    @Override
    public String toString() {
        return "Activitate : " + (work ? "Alta munca" : "Condus") + ": Tura de " + (nocturn ? "noapte" : "zi") + " | "
                + "Timp : " + timpTotal + " | " + "Ora start " + oraInceput + " | Ora incheiere : "
                + oraIncheiere;
    }
}
