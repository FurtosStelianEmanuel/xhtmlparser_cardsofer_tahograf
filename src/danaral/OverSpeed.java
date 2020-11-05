/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Manel
 */
public class OverSpeed extends Eveniment {

    String maxSpeed;
    String averageSpeed;

    private String extrageMaxSpeed(String t) {
        Pattern p=Pattern.compile(" ?maxSpeedValue: (\\d\\d?\\d?\\d?) km/h");
        Matcher m = p.matcher(t);
        int cate = 0;
        String rez = "";
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate == 0) {
                System.out.println("Nu am putut extrage viteza maxima din " + t);
            } else if (cate > 1) {
                System.out.println("Hmm, am avut mai multe match-uri la viteza maxima pt " + t);
            }
        }
        return rez;
    }

    private String extrageAverageSpeed(String t) {
        /*
        averageSpeedValue: 
         */
        Pattern p = Pattern.compile(" ?averageSpeedValue: (\\d\\d?\\d?\\d?) km/h");
        Matcher m = p.matcher(t);
        int cate = 0;
        String rez = "";
        while (m.find()) {
            cate++;
            rez = m.group(1);
        }
        if (Danaral.DEBUG) {
            if (cate == 0) {
                System.out.println("Nu am putut extrage viteza medie din " + t);
            } else if (cate > 1) {
                System.out.println("Hmm, am avut mai multe match-uri la viteza medie pt " + t);
            }
        }
        return rez;
    }

    public OverSpeed(Element el, int index) {
        super(el, index);
        Elements lis = el.select("li");
        for (Element li : lis) {
            if (li.siblingElements().size() < 9) {
                if (li.text().contains("maxSpeedValue")) {
                    this.maxSpeed = extrageMaxSpeed(li.text());
                }
                if (li.text().contains("averageSpeedValue")) {
                    this.averageSpeed = extrageAverageSpeed(li.text());
                }
            }
        }
    }

}
