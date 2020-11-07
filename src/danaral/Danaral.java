/**
 * Ultima modificare 04/14/2020
 * Am adaugat optiunea de a alege ce zile din luna sa apara in raportul final din {@link CerereRaportLunar}
 */
package danaral;

import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 *
 * @author Manel
 */
public class Danaral {

    Cititor cititor;
    static Danaral ref;

   
    static String ACTIVITIES_TOKEN = "Activities on";
    static String WORKING_TOKEN = "work for";
    static String LI_TOKEN = "li";
    static String DRIVING_TOKEN = "driving for";
    static boolean DEBUG = false;
    static String TITLE_TOKEN = "title";
    static final String IANUARIE = "jan";
    static final String FEBRUARIE = "feb";
    static final String MARTIE = "mar";
    static final String APRILIE = "apr";
    static final String MAI = "may";
    static final String IUNIE = "jun";
    static final String IULIE = "jul";
    static final String AUGUST = "aug";
    static final String SEPTEMBRIE = "sep";
    static final String OCTOMBRIE = "oct";
    static final String NOIEMBRIE = "nov";
    static final String DECEMBRIE = "dec";
    
    static final String[][] ALTERNATIVE_LUNI=new String[][]{
        {"ian."},
        {"feb."},
        {"mar."},
        {"apr."},
        {"mai.","mai"},
        {"iun."},
        {"iul."},
        {"aug."},
        {"sept."},
        {"oct."},
        {"nov."},
        {"dec."}
    }; 
    
    formPrincipal guiPrincipal;
    RaportTahografForm tahografForm;
    EvenimenteForm evenimenteForm;
    static String RAPORT_NOAPTE="rapnoapte";
    static String RAPORT_TIP_1="rt1";
    
    static int oreNoapte[] = {22, 23, 24,0, 1, 2, 3, 4, 5};

    static Danaral getDanaral() {
        return ref;
    }
    
    public  Danaral() throws IOException {
        cititor = new Cititor();
        guiPrincipal=new formPrincipal();
        ref = this;
        tahografForm=new RaportTahografForm();
        evenimenteForm=new EvenimenteForm();
        /*List<RaportZi> rapoarte = cititor.load("C:\\Users\\Manel\\Desktop\\exemplu.html");

        cititor.sort(rapoarte);*/
    }
    
    void openGUI(){
        guiPrincipal.setLocationRelativeTo(null);
        guiPrincipal.setVisible(true);
    }

    public static String getPathToJar() throws URISyntaxException {
        String x = Danaral.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        StringBuilder b = new StringBuilder(x);
        b.delete(0, 1);
        return b.toString();
    }

    public static String get_path(String finalName) throws URISyntaxException {
        String full = getPathToJar();
        StringTokenizer b = new StringTokenizer(full, "/\\");
        String x = "";
        while (b.hasMoreElements()) {
            String y = b.nextToken();
            if (!(y.toLowerCase().equals(finalName.toLowerCase())) && !(y.toLowerCase().equals("store"))) {
                x += y + "\\";
            }
        }
        return x;
    }
    static String PATH="";
    static String kilometriRegex="activityDayDistance: ?(\\d\\d?\\d?\\d?\\d?\\d?) km";
    
    static Dictionary dictionar=new Hashtable();
    static String erori[]={"cel mai grav eveniment într-una din ultimele 10 zile în care a survenit",
    "unul dintre cele mai grave 5 evenimente care au survenit în ultimele 365 de zile",
    "evenimentul cu cea mai mare durată într-una din ultimele 10 zile în care a survenit",
    "unul dintre cele mai recente (sau ultimele) 10 evenimente sau anomalii",
    "primul eveniment sau prima anomalie care a survenit după ultima calibrare",
    "ultimul eveniment într-una din ultimele 10 zile în care a survenit"};
    
    
    final static int DIAGRAMA_2_INIT_VAL=80000;
    final static int DIAGRAMA_1_INIT_VAL=90000;
    static int DIAGRAMA_1=DIAGRAMA_1_INIT_VAL;
    static int DIAGRAMA_2=DIAGRAMA_2_INIT_VAL;
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     * @throws com.itextpdf.text.DocumentException
     * @throws java.net.URISyntaxException
     */
    public static void main(String[] args) throws IOException, URISyntaxException, DocumentException {
        
        dictionar.put("General events: Motion data error", "Eroare senzori miscare");
        dictionar.put("General events: Power supply interruption", "Intreruperea alimentarii");
        dictionar.put("General events: Driving without an appropriate card", "Conducere fara card");
        dictionar.put("General events: Over speeding", "Depasirea vitezei legale");
        dictionar.put("General events: Over speeding", "Depasirea vitezei legale");
        dictionar.put("Recording equipment faults: Downloading fault", "defectiune incarcare date");
        dictionar.put("Recording equipment faults: VU internal fault", "eroare interna");
        dictionar.put("Recording equipment faults: No further details", "eroare echipament de inregistrare fara detalii");
        dictionar.put("Recording equipment faults: Sensor fault", "problema senzor");
        dictionar.put("General events: Card insertion while driving", "inserare card in timpul condusului");
        dictionar.put("Vehicle unit related security breach attempt events: Hardware sabotage", 
                "Securitatea vehiculului: sabotaj hardware");
        dictionar.put("General events: Last card session not correctly closed", "ultima sesiune a cardului nu a fost incheiata corect");
        
        
        
        dictionar.put("the most serious event for one of the last 10 days of occurrence", 
                "cel mai grav eveniment într-una din ultimele 10 zile în care a survenit");
        dictionar.put("one of the 5 longest events over the last 365 days", 
                "unul dintre cele mai grave 5 evenimente care au survenit în ultimele 365 de zile");
        dictionar.put("the longest event for one of the last 10 days of occurrence", 
                "evenimentul cu cea mai mare durată într-una din ultimele 10 zile în care a survenit");
        dictionar.put("one of the 10 most recent (or last) events or faults", 
                "unul dintre cele mai recente (sau ultimele) 10 evenimente sau anomalii");
        dictionar.put("the first event or fault having occurred after the last calibration", 
                "primul eveniment sau prima anomalie care a survenit după ultima calibrare");
        dictionar.put("the last event for one of the last 10 days of occurrence", 
                "ultimul eveniment într-una din ultimele 10 zile în care a survenit");
        
        
        Danaral danaral = new Danaral();
        danaral.openGUI();
        System.out.println(Arrays.toString(args));
        if (args.length==1){
            System.out.println(args[0]);
            danaral.cititor.load(args[0]);
            danaral.guiPrincipal.updateUIFromBatch();
            System.out.println("Am incarcat fisierul");
        }
        PATH = get_path("Program.jar");
       
        
        
        /*Danaral danaral = new Danaral();
        String l1="C:\\Users\\Manel\\Desktop\\data danaral\\BH-25-RLC Mon Dec 16 2019.xhtml";
        String l2="C:\\Users\\Manel\\Desktop\\data danaral\\ES HF 758 Sat Feb 15 2020.xhtml";
        String l3="C:\\Users\\Manel\\Desktop\\data danaral\\cazuri doi soferi\\BH-25-RLC Tue Feb 13 2018.xhtml";
        danaral.cititor.load(l1);
        System.out.println("gata cititu");*/
        
    }

    //<editor-fold desc="dump" defaultstate="collapsed">
            /*
        java.nio.file.Path path = Paths.get(Danaral.class.getResource("noapte.png").toURI());

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("iTextImageExample.pdf"));
        document.open();
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);

        document.close();*/


       /* System.out.println(Pattern.matches(kilometriRegex, 
                                            "Activities on Fri Jan 25 2019: activityRecordPreviousLength: 14 Bytes activityRecordLength: 44 Bytes activityRecordDate: Fri Jan 25 2019 activityPresenceCounter: 2 activityDayDistance: 425 km Visualization: break/rest, from 0:0"));
        */
        
       

        /*
        RaportZi oziulica = rapoarte.get(11);
        Object [][]data=new Object[oziulica.programCondus.size()][6];
        for (int i=0;i<data.length;i++){
            data[i][0]=oziulica.programCondus.get(i).getActivitate();
            data[i][1]=oziulica.programCondus.get(i).getTura();
            data[i][2]=oziulica.programCondus.get(i).getTimp();
            data[i][3]=oziulica.programCondus.get(i).getOraInceput();
            data[i][4]=oziulica.programCondus.get(i).getOraIncheiere();
        }
        oziulica.show();
        JFrame f = new JFrame();
        f.setTitle("Program in data de " + oziulica.data);
        f.setSize(600, 400);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);
        JTable table = new JTable(data, new Object[]{"Activitate", "Tura", "Durata", "Inceput", "Incheiere"});
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        f.add(new JScrollPane(table));
        f.setVisible(true);
         */
    //</editor-fold>

}
