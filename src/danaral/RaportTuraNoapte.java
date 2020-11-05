/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manel
 */
public class RaportTuraNoapte extends javax.swing.JFrame {

    /**
     * Creates new form RaportTuraNoapte
     */
    public RaportTuraNoapte() {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(formPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        initComponents();
        jMenuItem1.setIcon(
                new ImageIcon(new ImageIcon(getClass().getResource("pdf.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH))
        );
        jMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                /*
                extractLuna(rapoarte.get(0).data) + " a anului "
                + rapoarte.get(0).data.split(":")[2]
                 */
                try {
                    savePDF(Danaral.getDanaral().cititor.getSofer() + " tura de noapte " + extractLuna(rapoarte.get(0).data)
                            + " " + rapoarte.get(0).data.split(":")[2]);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RaportTuraNoapte.this,
                            "Nu am putut salva fisierul, poate mai e deschis in Adobe Acrobat sau alt program");
                }
            }
        });
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);

                return c;
            }
        };
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);

        jTable1.setDefaultRenderer(Object.class, centerRenderer);

    }
    List<RaportZi> rapoarte;

    String extractLuna(String data) {
        String ar[] = data.split(":");
        String luni[] = {
            "Daca apare asta, sunati-l pe manel",
            "ianuarie",
            "februarie",
            "martie",
            "aprilie",
            "mai",
            "iunie",
            "iulie",
            "august",
            "septembrie",
            "octombrie",
            "noiembrie",
            "decembrie"
        };
        int index;
        try {
            index = Integer.valueOf(ar[0]);
        } catch (Exception ex) {
            index = 0;
        }
        return luni[index];
    }

   /* public String getTotalOreConduseNoaptea_LunaAceasta(List<RaportZi> rapoarte) {
        long total = 0;
        for (RaportZi raport : rapoarte) {
            long ore = TimeUnit.HOURS.toMillis(Integer.valueOf(raport.getTotalOreConduseNoaptea().replace("h", "").split(":")[0]));
            long minute = TimeUnit.MINUTES.toMillis(Integer.valueOf(raport.getTotalOreConduseNoaptea().replace("h", "").split(":")[1]));
            total += ore + minute;
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
        return "";
    }*/
    private void addTableHeader(PdfPTable table) {
        Stream.of("Data", "Ore conducere", "Ore alta munca","Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, List<RaportZi> rapoarte) {
        for (int i = 0; i < rapoarte.size(); i++) {

            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(i%2==0 ? BaseColor.WHITE:BaseColor.LIGHT_GRAY);
            header.setPhrase(new Phrase(rapoarte.get(i).data));
            table.addCell(header);
            
            header.setPhrase(new Phrase(rapoarte.get(i).getTotalOreConduseNoaptea()));
            table.addCell(header);
            
            header.setPhrase(new Phrase(rapoarte.get(i).getTotalOreAltaMuncaNoaptea()));
            table.addCell(header);
            
            header.setPhrase(new Phrase(rapoarte.get(i).getTotalOreLucrateNoaptea()));
            table.addCell(header);

            /*table.addCell(raport.data);
            table.addCell(raport.getTotalOreConduseNoaptea());
            table.addCell(raport.getTotalOreAltaMuncaNoaptea());
            table.addCell(raport.getTotalOreLucrateNoaptea());*/
        }
    }
        private void addRows(PdfPTable table, String data,String totalCondus,String totalAltaMunca,String totalLucru) {
        

            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(BaseColor.GRAY);
            header.setPhrase(new Phrase(data));
            table.addCell(header);
            
            header.setPhrase(new Phrase(totalCondus));
            table.addCell(header);
            
            header.setPhrase(new Phrase(totalAltaMunca));
            table.addCell(header);
            
            header.setPhrase(new Phrase(totalLucru));
            table.addCell(header);

            /*table.addCell(raport.data);
            table.addCell(raport.getTotalOreConduseNoaptea());
            table.addCell(raport.getTotalOreAltaMuncaNoaptea());
            table.addCell(raport.getTotalOreLucrateNoaptea());*/
        
    }
    
    void savePDF(String numeFisier){
        try {
            Document document = new Document();
            FileOutputStream fileOutputStream = new FileOutputStream(Danaral.PATH+numeFisier+".pdf");
            PdfWriter.getInstance(document, fileOutputStream);
            document.open();

            Paragraph preface = new Paragraph("Activitatile soferului "+Danaral.getDanaral().cititor.getSofer() +" pe tura de noapte - 22:00 - 06:00 \nDe la data: "
                    +rapoarte.get(0).data+"     Pana la data: "+rapoarte.get(rapoarte.size()-1).data);

            preface.setSpacingAfter(20f);
            preface.setAlignment(Element.ALIGN_CENTER);
            document.add(preface);
            PdfPTable table = new PdfPTable(4);

            addTableHeader(table);
            addRows(table, rapoarte);

            addRows(table, "Sumar", Cititor.getOreConduseNoaptea(rapoarte), Cititor.getOreAltaMuncaNoaptea(rapoarte), Cititor.getOreLucrateNoaptea(rapoarte));
            document.add(table);
            document.close();

            try {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RaportTuraNoapte.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println(Danaral.PATH + numeFisier + ".pdf");
                Runtime.getRuntime().exec("explorer.exe /select," + Danaral.PATH + numeFisier + ".pdf");
            } catch (IOException ex) {
                Logger.getLogger(RaportTuraNoapte.class.getName()).log(Level.SEVERE, null, ex);
            }
            //JOptionPane.showMessageDialog(RaportTuraNoapte.this, "Fisier salvat "+Danaral.PATH+numeFisier+".pdf");
        } catch (FileNotFoundException | DocumentException ex) {
            Logger.getLogger(RaportTuraNoapte.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void set(List<RaportZi> rapoarte) {
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        setTitle("Luna " + extractLuna(rapoarte.get(0).data) + " a anului "
                + rapoarte.get(0).data.split(":")[2] + " Tura de noapte");
        this.rapoarte = rapoarte;
        for (RaportZi raport : rapoarte) {
            model.addRow(new Object[]{
                raport.data,
                raport.getTotalOreConduseNoaptea(),
                raport.getTotalOreAltaMuncaNoaptea(),
                raport.getTotalOreLucrateNoaptea()
            });
        }
        model.addRow(new Object[]{
            "Sumar",
            Cititor.getOreConduseNoaptea(rapoarte),
            Cititor.getOreAltaMuncaNoaptea(rapoarte),
            Cititor.getOreLucrateNoaptea(rapoarte)

        });
        jTable1.setModel(model);
        setSize(getWidth(), (rapoarte.size() + 3) * jTable1.getRowHeight() + 60);

        
        



        setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setFont(new java.awt.Font("DialogInput", 0, 14)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Data", "Ore conducere", "Ore altă muncă", "Total "
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionForeground(new java.awt.Color(255, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("Optiuni");

        jMenuItem1.setText("Generează PDF");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 277, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RaportTuraNoapte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RaportTuraNoapte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RaportTuraNoapte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RaportTuraNoapte.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RaportTuraNoapte().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
