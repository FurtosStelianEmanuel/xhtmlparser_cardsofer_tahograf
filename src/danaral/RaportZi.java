/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import static danaral.Danaral.kilometriRegex;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Manel
 */
public class RaportZi {

    /**
     * Efectiv data de reportului 'azi',format Feb 4 2019
     */
    String data;
    private List<String> text;
    List<ActivitateCondus> programCondus;
    private Elements zi;
    boolean ZI_INACTIVA = false;
    String kilometri;

    /**
     * @param zi este de fapt raportul in format {@link Elements} File input =
     * new File(locatieFisier); Document doc = Jsoup.parse(input, "UTF-8");
     * Elements lis = doc.select(LI_TOKEN); List<Element>liUri=new
     * ArrayList<>();
     *
     * for (Element li:lis){ if (li.text().contains(ACTIVITIES_TOKEN)){
     * liUri.add(li); } } liUri.remove(0);
     *
     * Elements lis2=liUri.get(11).select(LI_TOKEN); //System.out.println(lis2);
     * for (Element li:lis2){ if (li.text().contains(DRIVING_TOKEN)){
     * System.out.println(li.text()); } // System.out.println(li); }
     *
     * practic functia asta primeste ca parametru variabila lis2 din exemplul de
     * mai sus, liUri sunt toate <li> urile,
     */
    final boolean spuneKilometraj(String linie) {
        //li.text().contains("activityDayDistance")
        return (linie.contains("activityDayDistance"))
                && linie.length() > "activityDayDistance: 1 km".length() + 10;
    }

    public RaportZi() {
    }

    private String incepereProgram = "";
    private String incheiereProgram = "";
    private String totalOreConducere = "";
    private String totalOreAltaMunca = "";
    private String totalOreOdihna = "";
    private String totalOreLucrate = "";

    private String totalOreConduseNoaptea = "";
    private String totalOreAltaMuncaNoaptea = "";
    private String totalOreLucrateNoaptea = "";

    private String calculateOreConduseNoaptea(List<ActivitateCondus> activitati) {
        long total = 0;

        for (ActivitateCondus activitateCondus : activitati) {
            if (!activitateCondus.work && activitateCondus.nocturn) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        int ore = (int) TimeUnit.MILLISECONDS.toHours(total) % 24;
        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Integer.toString(ore);
        String minuteString = Integer.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateOreAltaMuncaNoaptea(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            if (activitateCondus.work && activitateCondus.nocturn) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total) % 24;
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateOreLucrateNoaptea(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            if (activitateCondus.nocturn) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total) % 24;
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateTotalOreLucrate(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total) % 24;
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateTotalOreConduse(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            if (!activitateCondus.work) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total) % 24;
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateTotalOre_AltaMunca(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            if (activitateCondus.work) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total) % 24;
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    private String calculateOdihna(List<ActivitateCondus> activitati) {
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        total = TimeUnit.HOURS.toMillis(24) - Math.abs(total);

        int ore = (int) TimeUnit.MILLISECONDS.toHours(total) % 24;
        int minute = (int) TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        if (ore < 0) {
            ore = 24 + ore;
        }
        if (minute < 0) {
            ore--;
            minute = 60 + minute;
        }

        String oreString = Integer.toString(ore);
        String minuteString = Integer.toString(minute);

        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }
        return oreString + ":" + minuteString + "h";

    }

    public String getIncepereProgram() {
        return incepereProgram;
    }

    public String getIncheiereProgram() {
        return incheiereProgram;
    }

    public String getTotalOreConduse() {
        return totalOreConducere;
    }

    public String getTotalOreAltaMunca() {
        return totalOreAltaMunca;
    }

    public String getTotalOreLucrate() {
        return !"".equals(totalOreLucrate) ? totalOreLucrate : calculateTotalOreLucrate(programCondus);
    }

    public String getTotalOreOdihna() {
        return !"".equals(totalOreOdihna) ? totalOreOdihna : calculateOdihna(programCondus);
    }

    public String getTotalOreConduseNoaptea() {
        return totalOreConduseNoaptea;
    }

    public String getTotalOreAltaMuncaNoaptea() {
        return totalOreAltaMuncaNoaptea;
    }

    public String getTotalOreLucrateNoaptea() {
        return totalOreLucrateNoaptea;
    }

    public RaportZi(RaportZi zi) {
        try {
            data = zi.data;
            incepereProgram = zi.programCondus.get(0).oraInceput;
            incheiereProgram = zi.programCondus.get(zi.programCondus.size() - 1).oraIncheiere;
            totalOreConducere = calculateTotalOreConduse(zi.programCondus);
            totalOreAltaMunca = calculateTotalOre_AltaMunca(zi.programCondus);
            totalOreLucrate = calculateTotalOreLucrate(zi.programCondus);
            totalOreOdihna = calculateOdihna(zi.programCondus);

            totalOreAltaMuncaNoaptea = calculateOreAltaMuncaNoaptea(zi.programCondus);
            totalOreConduseNoaptea = calculateOreConduseNoaptea(zi.programCondus);
            totalOreLucrateNoaptea = calculateOreLucrateNoaptea(zi.programCondus);

            ZI_INACTIVA = zi.ZI_INACTIVA;
            kilometri = zi.kilometri;
        } catch (Exception ex) {
            data = zi.data;
            incepereProgram = "";
            incheiereProgram = "";
            totalOreConducere = "00:00h";
            totalOreAltaMunca = "00:00h";
            totalOreLucrate = "00:00h";
            totalOreOdihna = "24:00h";

            totalOreAltaMuncaNoaptea = "00:00h";
            totalOreLucrateNoaptea = "00:00h";
            totalOreConduseNoaptea = "00:00h";

            ZI_INACTIVA = zi.ZI_INACTIVA;
            kilometri = zi.kilometri;
        }
        if (Danaral.DEBUG) {
            System.out.println("In data de " + zi.data + " a condus timp de " + totalOreConducere
                    + " a facut alta munca timp de " + totalOreAltaMunca + " rezulta ore lucrate: "
                    + totalOreLucrate);
        }
    }

    public RaportZi(Elements zi) {
        text = new ArrayList<>();
        programCondus = new ArrayList<>();
        for (Element li : zi) {
            if (spuneKilometraj(li.text())) {
                Matcher m = p.matcher(li.text());
                if (m.find()) {
                    kilometri = m.group(1);
                }
            }
            if (li.text().contains(Danaral.DRIVING_TOKEN)
                    || li.text().contains(Danaral.WORKING_TOKEN)) {
                text.add(li.text());
            }
        }
        this.zi = zi;
        if (text.isEmpty()) {
            ZI_INACTIVA = true;
            text.add(zi.get(0).text());
        }

    }

    private boolean condusZiuaSiNoaptea(ActivitateCondus activitate) {
        int ora_inc = Integer.valueOf(activitate.oraInceput.split(":")[0]);
        int ora_fin = Integer.valueOf(activitate.oraIncheiere.split(":")[0]);
        if (activitate.nocturn) {
            return false;
        }
        boolean condusZiua = true;
        boolean condusNoaptea = false;
        for (Integer ora : Danaral.oreNoapte) {
            if (ora_inc == ora) {
                condusZiua = false;
            }
            if (ora_fin == ora) {
                condusNoaptea = true;
            }
        }

        return condusZiua && condusNoaptea;
    }

    private boolean condusNoapteaSiZiua(ActivitateCondus activitate) {
        int ora_inc = Integer.valueOf(activitate.oraInceput.split(":")[0]);
        int ora_fin = Integer.valueOf(activitate.oraIncheiere.split(":")[0]);
        if (!activitate.nocturn) {
            return false;
        }
        boolean finalNocturn = false;
        for (Integer intreg : Danaral.oreNoapte) {
            if (intreg == ora_fin) {
                finalNocturn = true;
                break;
            }

        }
        return !finalNocturn;
    }

    public Date getDate() {
        try {
            Date d = new SimpleDateFormat("MM:DD:YYYY").parse(data);
            return d;
        } catch (ParseException ex) {
            Logger.getLogger(RaportZi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }
    Pattern p = Pattern.compile(kilometriRegex);

    public void create() {
        data = extrageData(text.get(0));
        if (!ZI_INACTIVA) {

            for (Element li : zi) {
                if (li.text().contains(Danaral.DRIVING_TOKEN)
                        || li.text().contains(Danaral.WORKING_TOKEN)) {
                    ActivitateCondus activitateCondus = new ActivitateCondus(li.text());
                    if (activitateCondus.eValida()) {
                        /*
                        int orainc = Integer.valueOf(activitateCondus.oraInceput.split(":")[0]);
                        orainc++;
                        orainc %= 24;
                        int orafin = Integer.valueOf(activitateCondus.oraIncheiere.split(":")[0]);
                        orafin++;
                        orafin %= 24;
                        String oraInceput = orainc + ":" + activitateCondus.oraInceput.split(":")[1]+"h";
                        String oraIncheiere = orafin + ":" + activitateCondus.oraIncheiere.split(":")[1]+"h";
                        System.out.println("caca "+data+" ->"+oraInceput+":"+oraIncheiere);
                        activitateCondus.oraInceput = oraInceput;
                        activitateCondus.oraIncheiere = oraIncheiere;
                         */

                        programCondus.add(activitateCondus);
                    }
                }
            }
            for (int i = programCondus.size() - 1; i >= 0; i--) {
                if (condusNoapteaSiZiua(programCondus.get(i))) {
                    // System.out.println("Am gasit caz cand conduce noaptea si il prinde ziua: "
                    //         + programCondus.get(i).toString());
                    ActivitateCondus[] separat = programCondus.get(i).desparteNoapteSiZi();
                    // System.out.println("Am format : " + separat[0].toString() + "si\n"
                    //          + separat[1].toString());
                    programCondus.set(i, separat[0]);
                    programCondus.add(i + 1, separat[1]);
                } else if (condusZiuaSiNoaptea(programCondus.get(i))) {
                    //System.out.println("Am gasit caz in care face sex " + programCondus.get(i).toString()+"\n"+data);
                    ActivitateCondus[] separat = programCondus.get(i).desparteZiSiNoapte();
                    // System.out.println("Am format : " + separat[0].toString() + "si\n"
                    //          + separat[1].toString());
                    programCondus.set(i, separat[1]);
                    programCondus.add(i + 1, separat[0]);
                    // System.out.println("Am format : " + separat[1].toString() + "si\n"
                    //    + separat[0].toString());
                }
            }
        }
    }

    public void show() {
        System.out.println("Raport in data de: " + data + (ZI_INACTIVA ? " Nu a fost detectata nicio activitate"
                + " in aceasta zi" : ""));
        for (ActivitateCondus activitateCondus : programCondus) {
            System.out.println(activitateCondus.toString());
        }
    }

    public String getKilometri() {
        return kilometri;
    }

    /**
     *
     * @param luna Ian, Feb, Mar ... etc
     * @return 01, 02, 03 ... etc
     */
    static String extrageLuna(String luna) throws DateTimeException {
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
        for (int i = 0; i < Danaral.ALTERNATIVE_LUNI.length; i++) {
            for (int j = 0; j < Danaral.ALTERNATIVE_LUNI[i].length; j++) {
                if (Danaral.ALTERNATIVE_LUNI[i][j].equals(luna)) {
                    String rezultat = ((i + 1) < 10 ? "0" : "")+(i+1);
                    return rezultat;
                }
            }
        }
        throw new DateTimeException("am primit ca luna textul " + luna);
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

    /**
     * Cam degeaba...
     *
     * @param an
     * @return
     */
    private String extrageAnul(String an) {
        String replace = an.replace(":", "");
        return replace;
    }

    /**
     *
     * @param linie formatul primit in aceasta variabila este acesta:'Activities
     * on Mon Feb 4 2019: activityRecordPreviousLength: 14 Bytes
     * activityRecordLength: 42 Bytes activityRecordDate: Mon'
     * @return data in format MM:DD:YYYY
     */
    private String extrageData(String linie) {
        String ar[] = linie.split(" ");
        String d = "";
        d += extrageLuna(ar[3]);
        d += ":";
        d += extrageZiua(ar[4]);
        d += ":";
        d += extrageAnul(ar[5]);
        return d;
    }
}
