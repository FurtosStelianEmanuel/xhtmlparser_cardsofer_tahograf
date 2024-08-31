/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Manel
 */
public class Cititor {

    private String numeSofer;
    private String primaInregistrare;
    private String ultimaInregistrare;

    /**
     * Asta nu prea ar trebui sa il folosesti pt ca apar mai multe numere de
     * inmatriculare in fisier, mai bine foloseste {@link #numereInmatriculare}
     */
    private String numarInmatriculare;

    List<RaportZi> rapoarteZilnice = new ArrayList<>();
    List<RaportZiTahograf> rapoarteZilniceTahograf = new ArrayList<>();

    void sort(List<RaportZi> rapoarte) {
        rapoarte.sort((d1, d2) -> {
            return d1.getDate().compareTo(d2.getDate());
        });
    }

    static public String getKmCondusi(List<RaportZi> rapoarte) {
        int km = 0;
        for (RaportZi raport : rapoarte) {
            km += Integer.valueOf(raport.getKilometri());
        }
        return Integer.toString(km);
    }

    public static String getOreConduse(List<RaportZi> rapoarte) {
        /*
        long total = 0;
        for (ActivitateCondus activitateCondus : activitati) {
            if (!activitateCondus.work) {
                int ore = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[0]);
                int minute = Integer.valueOf(activitateCondus.timpTotal.replace("h", "").split(":")[1]);
                long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
                total += timp;
            }
        }
        long ore=TimeUnit.MILLISECONDS.toHours(total)%24;
        long minute=TimeUnit.MILLISECONDS.toMinutes(total) % 60;
        
        String oreString=Long.toString(ore);
        String minuteString=Long.toString(minute);
        
        if (ore<10)
            oreString="0"+oreString;
        if (minute<10)
            minuteString="0"+minuteString;
        
        return oreString+":"+minuteString+"h";
         */
        long total = 0;
        for (RaportZi raport : rapoarte) {
            int ore = Integer.valueOf(raport.getTotalOreConduse().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreConduse().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getOreAltaMunca(List<RaportZi> rapoarte) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            int ore = Integer.valueOf(raport.getTotalOreAltaMunca().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreAltaMunca().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getOreOdihna(List<RaportZi> rapoarte) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            int ore = Integer.valueOf(raport.getTotalOreOdihna().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreOdihna().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getTotalOreLucrate(List<RaportZi> rapoarte) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            int ore = Integer.valueOf(raport.getTotalOreLucrate().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreLucrate().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getOreConduseNoaptea(List<RaportZi> rapoarte, boolean isFromPdfContext) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            String[] totalOreLucrateNoapteaSplitted = raport.getTotalOreLucrateNoaptea().replace("h", "").split(":");
            long totalOreLucrateNoaptea = TimeUnit.HOURS.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[0])) + TimeUnit.MINUTES.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[1]));

            if (isFromPdfContext && totalOreLucrateNoaptea < RaportTuraNoapte.MINIM_ORE_CONDUSE_NOAPTEA) {
                continue;
            }

            int ore = Integer.valueOf(raport.getTotalOreConduseNoaptea().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreConduseNoaptea().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getOreAltaMuncaNoaptea(List<RaportZi> rapoarte, boolean isFromPdfContext) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            String[] totalOreLucrateNoapteaSplitted = raport.getTotalOreLucrateNoaptea().replace("h", "").split(":");
            long totalOreLucrateNoaptea = TimeUnit.HOURS.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[0])) + TimeUnit.MINUTES.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[1]));

            if (isFromPdfContext && totalOreLucrateNoaptea < RaportTuraNoapte.MINIM_ORE_CONDUSE_NOAPTEA) {
                continue;
            }

            String[] totalOreAltaMuncaSplitted = raport.getTotalOreAltaMuncaNoaptea().replace("h", "").split(":");
            int ore = Integer.valueOf(totalOreAltaMuncaSplitted[0]);
            int minute = Integer.valueOf(totalOreAltaMuncaSplitted[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
        long minute = TimeUnit.MILLISECONDS.toMinutes(total) % 60;

        String oreString = Long.toString(ore);
        String minuteString = Long.toString(minute);

        if (ore < 10 && ore > 0) {
            oreString = "0" + oreString;
        }
        if (minute < 10 && minute > 0) {
            minuteString = "0" + minuteString;
        }

        return oreString + ":" + minuteString + "h";
    }

    public static String getOreLucrateNoaptea(List<RaportZi> rapoarte, boolean isFromPdfContext) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            String[] totalOreLucrateNoapteaSplitted = raport.getTotalOreLucrateNoaptea().replace("h", "").split(":");
            long totalOreLucrateNoaptea = TimeUnit.HOURS.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[0])) + TimeUnit.MINUTES.toMillis(Integer.valueOf(totalOreLucrateNoapteaSplitted[1]));

            if (isFromPdfContext && totalOreLucrateNoaptea < RaportTuraNoapte.MINIM_ORE_CONDUSE_NOAPTEA) {
                continue;
            }

            int ore = Integer.valueOf(raport.getTotalOreLucrateNoaptea().replace("h", "").split(":")[0]);
            int minute = Integer.valueOf(raport.getTotalOreLucrateNoaptea().replace("h", "").split(":")[1]);
            long timp = TimeUnit.HOURS.toMillis(ore) + TimeUnit.MINUTES.toMillis(minute);
            total += timp;
        }
        long ore = TimeUnit.MILLISECONDS.toHours(total);
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

    public static String getKmTotaliCondusi(List<RaportZiTahograf> rapoarte) {
        int km = 0;
        for (RaportZiTahograf raport : rapoarte) {
            try {
                km += Integer.valueOf(raport.getKmTotali());
            } catch (Exception ex) {

            }
        }
        return Integer.toString(km);
    }

    public static String getTotalOreConduse(List<RaportZiTahograf> rapoarte) {
        String total = "00:00";
        long TOT = 0;
        for (RaportZiTahograf raport : rapoarte) {
            try {
                long tot = TimeUnit.HOURS.toMillis(Long.valueOf(raport.timpTotal.split(":")[0]))
                        + TimeUnit.MINUTES.toMillis(Long.valueOf(raport.timpTotal.split(":")[1]));
                TOT += tot;
            } catch (Exception ex) {

            }
        }
        int ore = (int) (TimeUnit.MILLISECONDS.toHours(TOT));
        int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(TOT));
        /*while (minute > 60) {
            ore++;
            minute /= 60;
        }*/
        minute %= 60;
        String oreString = ore + "";
        String minuteString = minute + "";
        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }
        total = oreString + ":" + minuteString;
        return total;
    }

    public static String getTotalOreOdihna(List<RaportZiTahograf> rapoarte) {
        String total = "00:00";
        long TOT = 0;
        for (RaportZiTahograf raport : rapoarte) {
            try {
                long tot = TimeUnit.HOURS.toMillis(Long.valueOf(raport.timpOdihna.split(":")[0]))
                        + TimeUnit.MINUTES.toMillis(Long.valueOf(raport.timpOdihna.split(":")[1]));
                TOT += tot;
            } catch (Exception ex) {

            }
        }
        int ore = (int) (TimeUnit.MILLISECONDS.toHours(TOT));
        int minute = (int) (TimeUnit.MILLISECONDS.toMinutes(TOT));
        /* while (minute > 60) {
            ore++;
            minute /= 60;
        }*/
        minute %= 60;
        String oreString = ore + "";
        String minuteString = minute + "";
        if (ore < 10) {
            oreString = "0" + oreString;
        }
        if (minute < 10) {
            minuteString = "0" + minuteString;
        }
        total = oreString + ":" + minuteString;
        return total;
    }

    void show(List<RaportZi> rapoarte) {
        for (RaportZi raport : rapoarte) {
            raport.show();
            System.out.println("");
        }
    }

    String getSofer() {
        return numeSofer;
    }

    String getPrimaInregistrare() {
        return primaInregistrare;
    }

    String getUltimaInregistrare() {
        return ultimaInregistrare;
    }

    String extrageNumeSofer(String line) {
        line = line.replace(",", "");
        String ar[] = line.split(" ");
        return ar[0] + " " + ar[1] + " " + (line.contains("-") ? "" : ar[2]);
    }

    String extragePrimaInregistrare(String line) {
        String ar[] = line.split(" ");
        try {
            return RaportZi.extrageLuna(ar[4]) + ":" + ar[5] + ":" + ar[6];
        } catch (DateTimeException ex) {
            return RaportZi.extrageLuna(ar[3]) + ":" + ar[4] + ":" + ar[5];
        } catch (Exception ex) {
            return "Eroare format";
        }
    }

    String extrageUltimaInregistrare(String line) {
        String ar[] = line.split(" ");
        try {
            return RaportZi.extrageLuna(ar[9]) + ":" + ar[10] + ":" + ar[11];
        } catch (DateTimeException ex) {
            return RaportZi.extrageLuna(ar[8]) + ":" + ar[9] + ":" + ar[10];
        } catch (Exception ex) {
            return "eroare format";
        }
    }

    public Cititor() {

    }
    List<String> numereInmatriculare;

    private List<Cititor> cititori = new ArrayList<>();
    ;
    private String soferiString;

    private RaportZi getRaportZiByDate(String date) {
        for (RaportZi raport : rapoarteZilnice) {
            if (raport.data.equals(date)) {
                return raport;
            }
        }
        return null;
    }

    int loadTahograf(Document doc) {
        List<Element> liUri = new ArrayList<>();
        Elements lis = doc.select("ul");
        Elements chiarLiuri = doc.select("li");
        numereInmatriculare = new ArrayList<>();
        for (Element li : lis) {
            if (li.text().contains("timeReal")) {
                liUri.add(li);
            }
        }

        Elements h3s = doc.select("li");
        Elements evenimente_vuEventRecords = null;
        Elements evenimente_vuFaultRecords = null;
        Elements evenimente_overSpeed = null;
        Elements vuEventRecordsElements = new Elements();
        Elements vuFaultRecordsElements = new Elements();
        Elements overSpeeds = new Elements();

        for (Element el : h3s) {
            if (el.text().contains("vuEventRecords") && evenimente_vuEventRecords == null) {
                evenimente_vuEventRecords = el.select("li");
            }
            if (el.text().contains("vuFaultRecords") && evenimente_vuFaultRecords == null) {
                evenimente_vuFaultRecords = el.select("li");
            }
            if (el.text().contains("vuOverspeedingEventRecords") && evenimente_overSpeed == null) {
                evenimente_overSpeed = el.select("li");
            }
        }
        for (int i = evenimente_vuEventRecords.size() - 1; i > 0; i--) {
            if (evenimente_vuEventRecords.get(i).siblingElements().size() >= 15) {
                vuEventRecordsElements.add(evenimente_vuEventRecords.get(i));
            }
        }
        for (int i = evenimente_vuFaultRecords.size() - 1; i > 0; i--) {
            if (evenimente_vuFaultRecords.get(i).siblingElements().size() >= 15) {
                vuFaultRecordsElements.add(evenimente_vuFaultRecords.get(i));
            }
        }
        for (int i = evenimente_overSpeed.size() - 1; i > 0; i--) {
            if (evenimente_overSpeed.get(i).siblingElements().size() >= 9) {
                overSpeeds.add(evenimente_overSpeed.get(i));
            }
        }

        List<Eveniment> vuFaultRecords = new ArrayList<>();
        List<Eveniment> vuEventRecords = new ArrayList<>();
        List<OverSpeed> vuOverspeedingEventRecords = new ArrayList<>();

        for (int i = 0; i < vuFaultRecordsElements.size(); i++) {
            vuFaultRecords.add(new Eveniment(vuFaultRecordsElements.get(i), i));
        }
        for (int i = 0; i < vuEventRecordsElements.size(); i++) {
            vuEventRecords.add(new Eveniment(vuEventRecordsElements.get(i), i));
        }
        for (int i = 0; i < overSpeeds.size(); i++) {
            //System.out.println(overSpeeds.get(i).text()+"\n\n\n");
            vuOverspeedingEventRecords.add(new OverSpeed(overSpeeds.get(i), i));
        }

        if (Danaral.DEBUG) {
            System.out.println("Printare evenimente");

            System.out.println("vuFaultRecords " + vuFaultRecords.size());
            for (Eveniment e : vuFaultRecords) {
                System.out.println(e);
            }
            System.out.println("\nvuEventRecords " + vuEventRecords.size());
            for (Eveniment e : vuEventRecords) {
                System.out.println(e);
            }
            System.out.println("\nvuOverspeedingEventRecords " + vuOverspeedingEventRecords.size());
            for (OverSpeed ev : vuOverspeedingEventRecords) {
                System.out.println(ev);
            }
        }
        //liUri.forEach(el -> System.out.println(el));

        List<String> soferi = new ArrayList<>();
        List<RaportZiTahograf> rapoarte = new ArrayList<>();
        for (Element el : liUri) {
            RaportZiTahograf raport = new RaportZiTahograf(el.select("li"));
            raport.create();
            if (!soferi.contains(raport.conducator) && raport.conducator != null) {
                soferi.add(raport.conducator);
            }
            rapoarte.add(raport);
        }
        soferiString = "";
        for (String s : soferi) {
            soferiString += s + "\n";
        }
        InterogareCardSoferForm interogareCardSoferForm = new InterogareCardSoferForm();

        soferi.forEach(sofer -> {
            JButton incarcSofer = new JButton("Incarca cardul lui " + sofer);
            incarcSofer.setFocusable(false);

            incarcSofer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    JFileChooser chooser = new JFileChooser();
                    FileNameExtensionFilter htmlfilter = new FileNameExtensionFilter(
                            "fisiere xhtml (*.xhtml)", "xhtml");
                    chooser.setFileFilter(htmlfilter);
                    int x = chooser.showDialog(interogareCardSoferForm, "Selecteaza");
                    if (x == JFileChooser.OPEN_DIALOG) {
                        Cititor cititor = new Cititor();
                        try {
                            cititor.loadSilent(chooser.getSelectedFile().toString());
                            if (!cititori.contains(cititor)) {
                                cititori.add(cititor);
                            }
                            incarcSofer.setBackground(java.awt.Color.GREEN);
                            System.out.println("Am citit cardul lui " + cititor.numeSofer);
                        } catch (IOException ex) {
                            Logger.getLogger(Cititor.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            });
            interogareCardSoferForm.add(incarcSofer);
        });
        JButton afisare = new JButton("Vezi raportul tahografului");
        afisare.setFocusable(false);

        afisare.addActionListener(new ActionListener() {
            public int getNrDiagrama(String data, int indexSofer) {
                int nr = indexSofer % 2 == 0 ? Danaral.DIAGRAMA_1_INIT_VAL : Danaral.DIAGRAMA_2_INIT_VAL;
                String vreme[] = data.split(":");

                int luna = Integer.valueOf(vreme[0]);
                int zi = Integer.valueOf(vreme[1]);
                int an = Integer.valueOf(vreme[2]);
                an *= 100;
                an += luna;
                an *= 100;
                an += zi;

                nr += an;
                return nr;
            }

            @Override
            public void actionPerformed(ActionEvent ae) {

                Danaral.DIAGRAMA_1 = getNrDiagrama(rapoarte.get(0).data, 0);
                Danaral.DIAGRAMA_2 = getNrDiagrama(rapoarte.get(0).data, 0);
                List<RaportZiTahograf> listaFinala = new ArrayList<>();
                for (RaportZiTahograf raport : rapoarte) {
                    if (raport.soferi.size() == 1) {
                        for (Cititor cititor : cititori) {
                            if (soferi.get(0).trim().equals(cititor.numeSofer.trim())) {
                                for (RaportZi raportSofer : cititor.rapoarteZilnice) {
                                    if (raportSofer.data.equals(raport.data)) {
                                        try {
                                            raport.oraInceput = raportSofer.programCondus.get(0).oraInceput;
                                            raport.oraIncheiere = raportSofer.programCondus.get(raportSofer.programCondus.size() - 1).oraIncheiere;
                                        } catch (Exception ex) {
                                            raport.oraInceput = "";
                                            raport.oraIncheiere = "";
                                        }
                                        raport.kilometriInceput = Integer.valueOf(raport.kilometriFinali) - Integer.valueOf(raportSofer.getKilometri()) + "";
                                        raport.kilometriCondusi_sofer = raportSofer.getKilometri();
                                        raport.nrDiagrama = Danaral.DIAGRAMA_1;
                                        raport.oreConducere_sofer = raportSofer.getTotalOreLucrate();
                                        raport.oreOdihna_sofer = raportSofer.getTotalOreOdihna();
                                        raport.oreOdihna_sofer = raport.oreOdihna_sofer.equals("00:00h") ? "24:00h" : raport.oreOdihna_sofer;
                                        Danaral.DIAGRAMA_1++;
                                        listaFinala.add(raport);

                                    }
                                }
                            }
                        }
                    } else if (raport.soferi.size() == 2) {
                        System.out.println("Atentiune, doi soferi " + raport.data);
                        if (cititori.size() < 2) {
                            int x = JOptionPane.showConfirmDialog(null, "Nu s au citit cardurile ambilor soferi, in data de "
                                    + raport.data + " au condus " + soferi);
                            if (x == JOptionPane.CANCEL_OPTION) {
                                break;
                            }
                        } else if (cititori.size() > 2) {
                            int x = JOptionPane.showConfirmDialog(null, "S au citit prea multi soferi");
                            if (x == JOptionPane.CANCEL_OPTION) {
                                break;
                            }
                        } else {
                            Cititor cititorSofer1 = cititori.get(0);
                            Cititor cititorSofer2 = cititori.get(1);
                            RaportZiTahograf raportTahografSofer1 = new RaportZiTahograf();
                            RaportZiTahograf raportTahografSofer2 = new RaportZiTahograf();

                            RaportZi ziCardS1 = cititorSofer1.getRaportZiByDate(raport.data);
                            raportTahografSofer1.data = raport.data;
                            if (ziCardS1 != null) {
                                raportTahografSofer1.oraInceput = ziCardS1.programCondus.get(0).oraInceput;
                                raportTahografSofer1.oraIncheiere = ziCardS1.programCondus.get(ziCardS1.programCondus.size() - 1).oraIncheiere;
                                raportTahografSofer1.kilometriFinali = raport.kilometriFinali;
                                raportTahografSofer1.kilometriInceput = Integer.valueOf(raport.kilometriFinali) - Integer.valueOf(ziCardS1.kilometri) + "";
                                raportTahografSofer1.kilometriCondusi_sofer = ziCardS1.kilometri;
                                raportTahografSofer1.oreConducere_sofer = ziCardS1.getTotalOreLucrate();
                                raportTahografSofer1.oreOdihna_sofer = ziCardS1.getTotalOreOdihna();
                                raportTahografSofer1.conducator = cititorSofer1.numeSofer;
                            } else {
                                int x = JOptionPane.showConfirmDialog(null, "Nu am gasit data" + raport.data + " in cardul soferului "
                                        + cititorSofer1.numeSofer + " card dorit:" + soferi.get(0));
                                if (x == JOptionPane.CANCEL_OPTION) {
                                    break;
                                }
                            }

                            raportTahografSofer2.data = raport.data;
                            RaportZi ziCardS2 = cititorSofer2.getRaportZiByDate(raport.data);

                            if (ziCardS2 != null) {
                                raportTahografSofer2.oraInceput = ziCardS2.programCondus.get(0).oraInceput;
                                raportTahografSofer2.oraIncheiere = ziCardS2.programCondus.get(ziCardS2.programCondus.size() - 1).oraIncheiere;
                                raportTahografSofer2.kilometriFinali = raport.kilometriFinali;
                                raportTahografSofer2.kilometriInceput = Integer.valueOf(raport.kilometriFinali) - Integer.valueOf(ziCardS2.kilometri) + "";
                                raportTahografSofer2.kilometriCondusi_sofer = ziCardS2.kilometri;
                                raportTahografSofer2.oreConducere_sofer = ziCardS2.getTotalOreLucrate();
                                raportTahografSofer2.oreOdihna_sofer = ziCardS2.getTotalOreOdihna();
                                raportTahografSofer2.conducator = cititorSofer2.numeSofer;
                            } else {
                                int x = JOptionPane.showConfirmDialog(null, "Nu am gasit data" + raport.data + " in cardul soferului "
                                        + cititorSofer2.numeSofer + " card dorit:" + soferi.get(1));
                                if (x == JOptionPane.CANCEL_OPTION) {
                                    break;
                                }
                            }
                            if (ziCardS2 != null && ziCardS1 != null) {
                                System.out.println("Am creat raportul pe data de " + raport.data + raportTahografSofer1 + "\n"
                                        + raportTahografSofer2);
                            }
                            raportTahografSofer1.nrDiagrama = Danaral.DIAGRAMA_1++;
                            raportTahografSofer2.nrDiagrama = Danaral.DIAGRAMA_2++;
                            listaFinala.add(raportTahografSofer1);
                            listaFinala.add(raportTahografSofer2);
                        }
                    } else if (raport.soferi.size() > 2) {
                        JOptionPane.showMessageDialog(null, "Au condus mai mult de 2 soferi in " + raport.data);
                    }
                }
                Danaral.getDanaral().tahografForm.updateUI(rapoarte);
                Danaral.getDanaral().tahografForm.setVisible(true);
                Danaral.getDanaral().evenimenteForm.updateUI(vuFaultRecords, vuEventRecords, vuOverspeedingEventRecords);
                Danaral.getDanaral().evenimenteForm.setVisible(true);
                listaFinala.forEach(el -> System.out.println(el));
            }
        });

        interogareCardSoferForm.add(afisare);
        JOptionPane.showMessageDialog(null, "Soferii care au condus acest vehicul in perioada "
                + rapoarte.get(0).data + " " + rapoarte.get(rapoarte.size() - 1).data + " : " + soferi.size() + "\n" + soferiString);
        interogareCardSoferForm.setVisible(true);

        for (Element li : chiarLiuri) {
            Pattern numarInmatricularePattern = Pattern.compile(" vehicleRegistrationIdentification: (.....?.?.?.?.?.?.?.?.?.?) "
                    + "\\(.?.?.?.?.?.?.?.?.?.?.?.?.?\\)");
            Matcher m = numarInmatricularePattern.matcher(li.text());
            while (m.find()) {

                numarInmatriculare = m.group(1);

                if (!numereInmatriculare.contains(numarInmatriculare)) {
                    numereInmatriculare.add(numarInmatriculare);
                }
            }
        }
        numarInmatriculare = numereInmatriculare.get(numereInmatriculare.size() - 1);
        /*
        Danaral.getDanaral().tahografForm.updateUI(rapoarteZilniceTahograf);
        Danaral.getDanaral().tahografForm.setVisible(true);
        Danaral.getDanaral().evenimenteForm.updateUI(vuFaultRecords,vuEventRecords,vuOverspeedingEventRecords);
        Danaral.getDanaral().evenimenteForm.setVisible(true);
         */
        return 2;
    }
    CerereRaportLunar cerereRaportLunar;

    List<RaportZi> getRaportZiCard() {
        return rapoarteZilnice;
    }

    List<RaportZiTahograf> getRaportZiTahograf() {
        return rapoarteZilniceTahograf;
    }

    int loadSilent(String locatieFisier) throws IOException {
        /*try{
            rapoarteZilnice.clear();
        }catch(Exception ex){
            
        }*/
        File input = new File(locatieFisier);
        Document doc = Jsoup.parse(input, "UTF-8");
        try {

            Elements lis = doc.select(Danaral.LI_TOKEN);
            Elements titluri = doc.select(Danaral.TITLE_TOKEN);
            List<Element> liUri = new ArrayList<>();

            numeSofer = extrageNumeSofer(titluri.get(0).text());
            primaInregistrare = extragePrimaInregistrare(titluri.get(0).text());
            try {
                ultimaInregistrare = extrageUltimaInregistrare(titluri.get(0).text());
            } catch (ArrayIndexOutOfBoundsException ex) {
                ultimaInregistrare = "Nu am putut extrage data ultimei inregistrari";
            }

            for (Element li : lis) {

                if (li.text().contains(Danaral.ACTIVITIES_TOKEN)) {
                    liUri.add(li);
                }
            }

            liUri.remove(0);

            for (Element element : liUri) {
                RaportZi raport = new RaportZi(element.select(Danaral.LI_TOKEN));
                raport.create();
                rapoarteZilnice.add(raport);
            }
            if (Danaral.DEBUG) {
                for (RaportZi raport : rapoarteZilnice) {
                    raport.show();
                    System.out.println("");
                }
            }

            return 1;
        } catch (Exception ex) {
            Logger.getLogger(Cititor.class.getSimpleName()).log(Level.SEVERE, null, ex);
            System.out.println("Exceptie cand am incercat sa citim date de tip card sofer, incercam sa citim date tahograf");
            return loadTahograf(doc);
        }

    }

    boolean fisierTahograf(Document doc) {
        Elements lis = doc.select(Danaral.LI_TOKEN);
        for (Element el : lis) {
            if (el.text().contains("vehicleIdentificationNumber")) {
                return true;
            }
        }
        return false;
    }

    int load(String locatieFisier) throws IOException {
        /*try{
            rapoarteZilnice.clear();
        }catch(Exception ex){
            
        }*/

        File input = new File(locatieFisier);
        Document doc = Jsoup.parse(input, "UTF-8");

        if (fisierTahograf(doc)) {
            return loadTahograf(doc);
        }

        try {
            rapoarteZilnice.clear();
        } catch (Exception ex) {

        }

        Elements lis = doc.select(Danaral.LI_TOKEN);
        Elements titluri = doc.select(Danaral.TITLE_TOKEN);
        List<Element> liUri = new ArrayList<>();

        numeSofer = extrageNumeSofer(titluri.get(0).text());
        primaInregistrare = extragePrimaInregistrare(titluri.get(0).text());
        try {
            ultimaInregistrare = extrageUltimaInregistrare(titluri.get(0).text());
        } catch (ArrayIndexOutOfBoundsException ex) {
            ultimaInregistrare = "Nu am putut extrage ultima inregistrare";
        }
        for (Element li : lis) {

            if (li.text().contains(Danaral.ACTIVITIES_TOKEN)) {
                liUri.add(li);
            }
        }

        liUri.remove(0);

        for (Element element : liUri) {
            RaportZi raport = new RaportZi(element.select(Danaral.LI_TOKEN));
            raport.create();
            rapoarteZilnice.add(raport);
        }
        if (Danaral.DEBUG) {
            for (RaportZi raport : rapoarteZilnice) {
                raport.show();
                System.out.println("");
            }
        }

        try {
            cerereRaportLunar.dispose();
        } catch (Exception ex) {

        }
        cerereRaportLunar = new CerereRaportLunar();

        return 1;
    }

    boolean aceeasiLuna_SiAn(String d1, String d2) {
        String ar1[] = d1.split(":");
        String ar2[] = d2.split(":");
        return (ar1[0].equals(ar2[0]) && ar1[2].equals(ar2[2]));
    }

    public int[] getAni() {
        List<Integer> ani = new ArrayList<>();
        for (RaportZi raport : rapoarteZilnice) {
            String anS = raport.data.split(":")[2];
            int an = 2020;
            try {
                an = Integer.valueOf(anS);
            } catch (Exception ex) {

            }
            if (!ani.contains(an)) {
                ani.add(an);
            }
        }
        int ar[] = new int[ani.size()];
        for (int i = 0; i < ani.size(); i++) {
            ar[i] = ani.get(i);
        }
        return ar;
    }

    List<RaportZi> getRaportPeLuna(String data) {
        List<RaportZi> peZile = getRaportPeZile();
        List<RaportZi> deReturnat = new ArrayList<>();
        for (RaportZi raport : peZile) {
            if (deReturnat.size() > 32) {
                break;
            }
            if (aceeasiLuna_SiAn(data, raport.data)) {
                deReturnat.add(raport);
            }
        }
        return deReturnat;
    }

    List<RaportZi> getRaportPeZile() {
        List<RaportZi> raportCondensat = new ArrayList<>();
        for (int i = 0; i < rapoarteZilnice.size(); i++) {
            RaportZi raport = new RaportZi(rapoarteZilnice.get(i));
            raportCondensat.add(raport);
        }
        return raportCondensat;
    }
}

//<editor-fold desc="dump" defaultstate="collapsed">
/*Elements lis2=liUri.get(11).select(Danaral.LI_TOKEN);
        RaportZi raportZi=new RaportZi(lis2);
        raportZi.create();
        raportZi.show();
        Elements lis3=liUri.get(12).select(Danaral.LI_TOKEN);
        RaportZi raportZi2=new RaportZi(lis3);
        raportZi2.create();
        raportZi2.show();
 */
//System.out.println(liUri.get(0).select(Danaral.LI_TOKEN));
/*Elements lis2=liUri.get(0).select(Danaral.LI_TOKEN);
        RaportZi raportZi=new RaportZi(lis2);
        raportZi.create();
        raportZi.show();
 */
//</editor-fold>
