/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Manel
 */
public class RaportZiTahograf {

    List<String> text;
    Elements zi;
    List<ActivitateCondus>programCondus;
    String kilometriFinali;
    String oraInceput;
    String oraIncheiere;
    String total;
    String kilometriInceput;
    String timpTotal;
    String timpOdihna;
    String nrInmatriculare;
    String conducator;
    String data;
    int nrDiagrama;
    boolean ZI_INACTIVA=false;
    public RaportZiTahograf(Elements zi) {
        text = new ArrayList<>();
        programCondus = new ArrayList<>();
        for (Element li : zi) {
            text.add(li.text());
        }
        this.zi = zi;
        if (text.isEmpty()) {
            ZI_INACTIVA = true;
            text.add(zi.get(0).text());
        }
        //text.forEach(el -> System.out.println(el));
    }

    public RaportZiTahograf() {
    }
    

    /**
     *
     * @param luna Ian, Feb, Mar ... etc
     * @return 01, 02, 03 ... etc
     */
    static String extrageLuna(String luna) throws DateTimeException{
        switch (luna.toLowerCase()) {
            case (Danaral.IANUARIE):
                return "01";
            case (Danaral.FEBRUARIE):
                return "02";
            case (Danaral.MARTIE):
                return "03";
            case (Danaral.APRILIE):
                return "04";
            case (Danaral.MAI):
                return "05";
            case (Danaral.IUNIE):
                return "06";
            case (Danaral.IULIE):
                return "07";
            case (Danaral.AUGUST):
                return "08";
            case (Danaral.SEPTEMBRIE):
                return "09";
            case (Danaral.OCTOMBRIE):
                return "10";
            case (Danaral.NOIEMBRIE):
                return "11";
            case (Danaral.DECEMBRIE):
                return "12";
        }
        throw new DateTimeException("Eroare in procesarea "+luna);
    }

    /**
     *
     * @param ziua 8,9,10 etc...
     * @return 08,09,10 etc...
     */
    private String extrageZiua(String ziua) {
        if (Integer.valueOf(ziua) < 10) {
            ziua = "0" + ziua;
        }
        return ziua;
    }

    private String extrageData(String s) {
        String ar[] = s.split(" ");
        String luna = extrageLuna(ar[2]);
        String ziua = extrageZiua(ar[3]);
        String an = ar[4];
        if (an.contains(":")){
            an=ar[5];
        }
            
        return luna + ":" + ziua + ":" + an;
    }

    private String extrageKilometri(String t) {
        String ar[] = t.split(" ");
        return ar[1];
    }
    public String kilometriCondusi_sofer;
    public String oreConducere_sofer;
    public String oreOdihna_sofer;
    String getKmTotali() {
        try {
            int inc = Integer.valueOf(kilometriInceput);
            int fin = Integer.valueOf(kilometriFinali);
            int rez = Math.abs(fin - inc);
            return Integer.toString(rez);
        }catch(NumberFormatException ex){
            return "0";
        }
    }

    List<String>soferi=new ArrayList<>();
    
    public void create() {
        data = extrageData(text.get(0));
        kilometriFinali = extrageKilometri(text.get(1));
        Pattern p = Pattern.compile("driving for (\\d?\\d:\\d\\d) h: driving, from (\\d?\\d:\\d\\d) to (\\d?\\d:\\d\\d)");
        String total = "";
        for (String x : text) {
            total += x + "\n";
        }
        //System.out.println(total);
        Matcher m = p.matcher(total);
        int counter = 0;
        class Pereche {

            String inc, fin;
            String tot;

            public Pereche() {
            }

            public Pereche(String inc, String fin, String tot) {
                this.inc = inc;
                this.fin = fin;
                this.tot = tot;
            }

        }
        List<Pereche> perechi = new ArrayList<>();
        long tot = 0;
        while (m.find()) {
            perechi.add(new Pereche(m.group(2), m.group(3), m.group(1)));
        }
        for (int i = 0; i < perechi.size() / 2; i++) {
            tot += TimeUnit.HOURS.toMillis(Long.valueOf(perechi.get(i).tot.split(":")[0])) + TimeUnit.MINUTES.toMillis(Long.valueOf(perechi.get(i).tot.split(":")[1]));
        }
        
        try {
            oraInceput = perechi.get(0).inc;
            oraIncheiere = perechi.get(perechi.size() - 1).fin;
        } catch (Exception ex) {
            ZI_INACTIVA = true;
        }

        Pattern conducatorRegex = Pattern.compile(" card holder: (.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?.?) ");
        m = conducatorRegex.matcher(total);
        while (m.find()) {
            conducator = m.group(1).replace(",", "").replace("(show)", "");
            if (!soferi.contains(conducator)) {
                soferi.add(conducator);
            }
        }
        if (!ZI_INACTIVA) {
            String oreLucrateString = TimeUnit.MILLISECONDS.toHours(tot) % 24 + "";
            int ora = (int) (TimeUnit.MILLISECONDS.toHours(tot) % 24);
            if (ora < 10) {
                oreLucrateString = "0" + oreLucrateString;
            }
            String minuteLucrateString = TimeUnit.MILLISECONDS.toMinutes(tot) % 60 + "";
            int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(tot) % 60);
            if (minute < 10) {
                minuteLucrateString = "0" + minuteLucrateString;
            }

            timpTotal = oreLucrateString + ":" + minuteLucrateString;

            long odh = TimeUnit.HOURS.toMillis(24) - tot;
            String oraOdihnaString = TimeUnit.MILLISECONDS.toHours(odh) % 24 + "";
            ora = (int) (TimeUnit.MILLISECONDS.toHours(odh) % 24);
            if (ora < 10) {
                oraOdihnaString = "0" + oraOdihnaString;
            }
            String minuteOdihnaString = TimeUnit.MILLISECONDS.toMinutes(odh) % 60 + "";
            minute = (int) (TimeUnit.MILLISECONDS.toMinutes(odh) % 60);
            if (minute < 10) {
                minuteOdihnaString = "0" + minuteOdihnaString;
            }
            timpOdihna = oraOdihnaString + ":" + minuteOdihnaString;
            Pattern kilometri = Pattern.compile("vehicleOdometerValue: (\\d\\d?\\d?\\d?\\d?\\d?\\d?\\d?\\d?\\d?\\d?\\d?\\d?) km");
            m = kilometri.matcher(total);
            if (m.find()) {
                kilometriInceput = m.group(1);
            } else {
                kilometriInceput = kilometriFinali;
            }
        } else {

            kilometriInceput = kilometriFinali;
            oraInceput = "";
            oraIncheiere = "";
            timpOdihna = "24:00";
            timpTotal = "00:00";
        }
        // System.out.println("Am creat raport cu data " + data + ", kmFinali : " + kilometriFinali + " ,ora inceput " + oraInceput + " ,ora incheiere " + oraIncheiere
        //       + " , kilometri inceput " + kilometriInceput + " , kilometri parcursi " + getKmTotali() + " , timp total lucru " + timpTotal
        //    + " ,timp odihna " + timpOdihna);
    }

    @Override
    public String toString() {
        return "Raport tahograf in data de " + data +"\n Nr diagrama: "+nrDiagrama+ "\n Ora incepere: " + oraInceput + "\n Ora incheiere: " + oraIncheiere
                + "\n Conducator: " + conducator + "\n Inceput odometru: " + kilometriInceput + "\n Final odometru: " + kilometriFinali
                + "\n Kilometri: " + kilometriCondusi_sofer + "\n Ore conducere: " + oreConducere_sofer + "\n Ore odihna: "
                + oreOdihna_sofer;
    }
}
