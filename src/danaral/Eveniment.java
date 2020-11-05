/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Manel
 */
public class Eveniment {

    public String codTip;
    public String tip;
    public String data;
    public String timpIncepere;
    public String timpIncheiere;
    public String durata;
    public String motivInregistrare;
    public String cantitate;
    private String eventTime;
    
    int index;

    private String extrageCodTip(String t) {
        Pattern p = Pattern.compile(" ?eventType: (\\d\\d?\\d?\\d?) -");
        Matcher m = p.matcher(t);
        String rez = "";
        int cate = 0;
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate > 1) {
                System.out.println("Hmm, ceva nu e bine cand extrag tipul evenimentului");
            } else if (cate == 0) {
                System.out.println("Nu am gasit match in " + t);
            }
        }
        return rez;
    }
    public Date getDate(){
        try {
            Date d = new SimpleDateFormat("MM:DD:YYYY").parse(timpIncepere.split(" ")[0]);
            return d;
        } catch (ParseException ex) {
            Logger.getLogger(RaportZi.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new Date();
    }
    private String extrageTip(String t) {
        Pattern p = Pattern.compile(" ?eventType: \\d\\d?\\d?\\d? - (.*)");
        Matcher m = p.matcher(t);
        String rez = "";
        int cate = 0;
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate > 1) {
                System.out.println("Hmm, ceva nu e bine cand extrag tipul evenimentului");
            } else if (cate == 0) {
                System.out.println("Nu am gasit match in " + t);
            }
        }
        return rez;
    }

    private String extrageMotiv(String t) {
        Pattern p = Pattern.compile(" ?eventRecordPurpose: (.*)");
        Matcher m = p.matcher(t);
        String rez = "";
        int cate = 0;
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate > 1) {
                System.out.println("Hmm, ceva nu e bine cand extrag tipul evenimentului");
            } else if (cate == 0) {
                System.out.println("Nu am gasit match in " + t);
            }
        }
        return rez;
    }

    private String extrageEventTime(String t) {
        /*
        eventTime: 
         */
        Pattern p = Pattern.compile(" ?eventTime: (.*)");
        Matcher m = p.matcher(t);
        String rez = "";
        int cate = 0;
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate > 1) {
                System.out.println("Hmm, ceva nu e bine cand extrag tipul evenimentului");
            } else if (cate == 0) {
                System.out.println("Nu am gasit match in " + t);
            }
        }
        return rez;
    }

    private String extrageTimpIncepere(String t) {
        t=t.replace(".", "");
        Pattern p = Pattern.compile(" from (\\d\\d:\\d\\d:\\d\\d) ");
        Matcher m = p.matcher(t);
        int cate = 0;
        String rez = "";
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (cate == 0) {
            if (Danaral.DEBUG) {
                System.out.println("Nu am putut extrage timp incepere, cautare de tip singular " + t);
            }
            p = Pattern.compile("From [a-zA-Z]{3} ([a-zA-Z]{3}) (\\d\\d?) (\\d\\d:\\d\\d:\\d\\d) (\\d{4})");
            m = p.matcher(t);
            String fin = "eroare";
            while (m.find()) {
                cate++;
                rez = m.group(1) + " " + m.group(2) + " " + m.group(4);
                fin = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez) + " " + m.group(3);
            }
            if (cate == 0) {
                if (Danaral.DEBUG){
                System.out.println("Incercam sa citim cu alt tip de format pt timp incepere");
                }
                p = Pattern.compile("From [a-zA-Z]{3} (\\d\\d?) ([a-zA-Z]{3}) (\\d\\d:\\d\\d:\\d\\d) (\\d{4})");
                m = p.matcher(t);
                if (m.find()) {
                    rez = m.group(2) + " " + m.group(1) + " " + m.group(4);
                    fin = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez) + " " + m.group(3);
                    cate++;
                } else {
                    rez = "01:01:1901";
                }
                if (cate == 0) {
                    p = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)");
                    m = p.matcher(t);
                    while (m.find()) {
                        cate++;
                        rez = m.group(1);
                    }
                    if (cate == 0) {
                        System.out.println("mega bai, nu am putut extrage inceperea evenimentului din " + t);
                    } else {
                        return data + " " + rez;
                    }
                }
            }
            return fin;
        } else if (cate > 1) {
            if (Danaral.DEBUG) {
                System.out.println("Hmm, ceva bai cu extragere timp incepere,avem mai mult match-uri " + t);
            }
        }
        return data+" "+rez;
    }

    private String extrageTimpIncheiere(String t) {
        t=t.replace(".", "");
        Pattern p = Pattern.compile(" from \\d\\d:\\d\\d:\\d\\d to (\\d\\d:\\d\\d:\\d\\d) ");
        Matcher m = p.matcher(t);
        int cate = 0;
        String rez = "";
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (cate == 0) {
            if (Danaral.DEBUG) {
                System.out.println("Nu am putut extrage timp incheiere, cautare de tip singular" + t);
            }
            p = Pattern.compile("to [a-zA-Z]{3} ([a-zA-Z]{3}) (\\d\\d?) (\\d\\d:\\d\\d:\\d\\d) (\\d{4})");
            m = p.matcher(t);
            String fin = "";
            while (m.find()) {
                cate++;
                rez = m.group(1) + " " + m.group(2) + " " + m.group(4);
                fin = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez) + " " + m.group(3);
            }
            if (cate == 0) {
                if (Danaral.DEBUG) {
                    System.out.println("Incercam sa citim cu alt tip de format pt timp incheiere");
                }
                p = Pattern.compile("to [a-zA-Z]{3} (\\d\\d?) ([a-zA-Z]{3}) (\\d\\d:\\d\\d:\\d\\d) (\\d{4})");
                m = p.matcher(t);
                if (m.find()) {
                    rez = m.group(2) + " " + m.group(1) + " " + m.group(4);
                    fin = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez) + " " + m.group(3);
                    cate++;
                } else {
                    rez = "01:01:1901";
                }
                if (cate == 0) {
                    p = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)");
                    m = p.matcher(t);
                    while (m.find()) {
                        cate++;
                        rez = m.group(1);
                    }
                    if (cate == 0) {
                        System.out.println("Super mega bai, nu am putut extrage incheierea evenimentului din " + t);
                    } else {
                        return data + " " + rez;
                    }
                }
            }
            return fin;
        } else if (cate > 1) {
            if (Danaral.DEBUG) {
                System.out.println("Hmm, ceva bai cu extragere timp incheiere,avem mai mult match-uri " + t);
            }
        }
        return data+" "+rez;
    }

    private String extrageDurata(String t) {
        Pattern p = Pattern.compile(" from \\d\\d:\\d\\d:\\d\\d to \\d\\d:\\d\\d:\\d\\d \\((\\d\\d?:\\d\\d:\\d\\d)\\)");
        Matcher m = p.matcher(t);
        int cate = 0;
        String rez = "";
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (cate == 0) {
            if (Danaral.DEBUG) {
                System.out.println("Nu am putut extrage durata, cautare de tip singular" + t);
            }
            p = Pattern.compile("(\\d\\d?\\d? days \\d\\d?:\\d\\d?:\\d\\d?)");
            m = p.matcher(t);
            while (m.find()) {
                cate++;
                rez = m.group(1);
            }
            if (cate == 0) {
                if (Danaral.DEBUG) {
                    System.out.println("Nu am putut extrage nicio durata din " + t);
                }
                return "00:00:00";
            } else {
                return rez;
            }

        } else if (cate > 1) {
            if (Danaral.DEBUG) {
                System.out.println("Hmm, ceva bai cu extragere durata,avem mai mult match-uri " + t);
            }
        }
        return rez;
    }

    private String extrageLuna(String data) {
        String ar[] = data.split(" ");
        return RaportZiTahograf.extrageLuna(ar[0]);
    }

    private String extrageZiua(String data) {
        String ar[] = data.split(" ");
        int zi = Integer.valueOf(ar[1]);
        String ziString = Integer.toString(zi);
        if (zi < 10) {
            ziString = "0" + ziString;
        }
        return ziString;
    }

    private String extrageAn(String data) {
        String ar[] = data.split(" ");
        return ar[2];
    }

    private String extrageData(String t) {
        //eventTime: On Thu Mar 23 2017, from 11:00:38 to 11:12:17 (0:11:39)
        Pattern p = Pattern.compile("On [a-zA-Z]{3} ([a-zA-Z]{3} \\d\\d? \\d\\d\\d\\d)");
        Matcher m = p.matcher(t);
        int cate = 0;
        
        //Sat Jan 25 10:17:03 2020
        Pattern dateCalc=Pattern.compile("[a-zA-Z]{3} ([a-zA-Z]{3} \\d\\d?) \\d\\d:\\d\\d:\\d\\d (\\d{4})");
        Matcher mat=dateCalc.matcher(t);
        String rez = "Mai multe zile";
        if (mat.find()){
            String tot=mat.group(1)+" "+mat.group(2);
            rez=extrageLuna(tot)+":"+extrageZiua(tot)+":"+extrageAn(tot);
        }
        
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (cate == 0) {
            if (Danaral.DEBUG) {
                System.out.println("Nu am putut extrage data, cautare de tip singular" + t);
            }
            p = Pattern.compile("[a-zA-Z]{3} (\\d\\d?).? ([a-zA-Z]{3}) \\d\\d:\\d\\d:\\d\\d (\\d\\d\\d\\d)");
            m = p.matcher(t);
            while (m.find()) {
                cate++;
                rez = m.group(2) + " " + m.group(1) + " " + m.group(3);
            }

            if (cate == 0) {
                if (Danaral.DEBUG) {
                    System.out.println("Tot nu am putut extrage nimic din " + t);
                }
            } else {
                rez = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez);
            }
        } else if (cate > 1) {
            if (Danaral.DEBUG) {
                System.out.println("Hmm, ceva bai cu extragere data,avem mai mult match-uri " + t);
            }
        } else {
            rez = extrageLuna(rez) + ":" + extrageZiua(rez) + ":" + extrageAn(rez);
        }
        return rez;
    }

    private String extrageCantitate(String t){
        Pattern p=Pattern.compile("(\\d\\d?\\d?\\d?)");
        Matcher m=p.matcher(t);
        if (m.find()){
            return m.group(1);
        }
        return "0.";
    }
    
    public Eveniment(Element el, int index) {
        Elements lis = el.select("li");
        this.index = index;
        for (Element li : lis) {
            if (li.siblingElements().size() < 15) {
                //System.out.println("Event " + li.text() + "gata " + index);
                if (li.text().contains("eventType")) {
                    this.codTip = extrageCodTip(li.text());
                    this.tip = extrageTip(li.text());
                } else if (li.text().contains("eventRecordPurpose")) {
                    this.motivInregistrare = extrageMotiv(li.text());
                } else if (li.text().contains("eventTime")) {
                    this.eventTime = extrageEventTime(li.text());
                    this.data = extrageData(this.eventTime);
                    this.timpIncepere = extrageTimpIncepere(this.eventTime);
                    this.timpIncheiere = extrageTimpIncheiere(this.eventTime);
                    this.durata = extrageDurata(this.eventTime);

                }else if (li.text().contains("similarEventsNumber")){
                    this.cantitate=extrageCantitate(li.text());
                }
            }
        }
    }

    @Override
    public String toString() {
        String vitezaMaxima = "69";
        String vitezaMedie = "69";
        boolean overspeed = false;
        if (this instanceof OverSpeed) {
            OverSpeed ov = (OverSpeed) this;
            vitezaMaxima = ov.maxSpeed;
            vitezaMedie = ov.averageSpeed;
            overspeed = true;
        }
        String x = "Eveniment: \n Cod: " + codTip + "\n Tip: " + tip + "\n Motiv: " + motivInregistrare
                + "\n eventTime: " + eventTime + "\n Timp incepere: " + timpIncepere + "\n Timp incheiere: " + timpIncheiere
                + (overspeed ? "\n Viteza maxima: " + vitezaMaxima + "\n Viteza medie: " + vitezaMedie
                        : "")
                + "\n Durata: " + durata
                + "\n Data: " + data;
        return x;
    }

}
