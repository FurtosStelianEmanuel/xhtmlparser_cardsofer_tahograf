/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Manel
 */
public class formPrincipal extends javax.swing.JFrame {

    String spacing = "            ";

    /*
    DefaultListModel listModel;
        listModel=new DefaultListModel();
        listModel.addElement("caca");
        jList1.setModel(listModel);
     */
    /**
     * Creates new form formPrincipal
     */
    JFileChooser chooser;
    JPopupMenu pop;

    /**
     *
     * @param data 01:24:2020
     * @return Februarie
     */
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

    String getLuna_anToken(String data) {
        String an = data.split(":")[2];
        return "Generează raport pe luna "
                + extractLuna(data) + " a anului "
                + an;
    }

    String getLuna_anToken_Noaptea(String data) {
        String an = data.split(":")[2];
        return "Generează raport pe tura de noapte din "
                + extractLuna(data) + " "
                + an;
    }

    void updateUI() {
        try {
            int returnVal = Danaral.getDanaral().cititor.load(chooser.getSelectedFile().toString());
            System.out.println("chemat cu fisier "+chooser.getSelectedFile().toString());
            if (returnVal == 1) {
                numeSofer.setText(Danaral.getDanaral().cititor.getSofer());
                primaInregistrare.setText(Danaral.getDanaral().cititor.getPrimaInregistrare());
                ultimaInregistrare.setText(Danaral.getDanaral().cititor.getUltimaInregistrare());
                List<RaportZi> rapoarte = Danaral.getDanaral().cititor.rapoarteZilnice;
                Danaral.getDanaral().cititor.sort(rapoarte);
                DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
                try {
                    int rowCount = model.getRowCount();
                    for (int i = rowCount - 1; i >= 0; i--) {
                        model.removeRow(i);
                    }
                } catch (Exception ex) {

                }
                jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (table.getValueAt(row, column) != "") {
                            c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                        } else {
                            c.setBackground(Color.GREEN);
                        }
                        return c;
                    }
                });

                for (RaportZi raport : rapoarte) {
                    //raport.show();
                    for (int i = 0; i < raport.programCondus.size(); i++) {
                        model.addRow(new Object[]{
                            raport.data,
                            raport.programCondus.get(i).getActivitate(),
                            raport.programCondus.get(i).getTura(),
                            raport.programCondus.get(i).getTimp(),
                            raport.programCondus.get(i).getOraInceput(),
                            raport.programCondus.get(i).getOraIncheiere()
                        });
                    }
                    if (raport.programCondus.isEmpty()) {
                        model.addRow(new Object[]{
                            raport.data,
                            "",
                            "",
                            "",
                            "",
                            ""
                        });
                    }
                }
                jTable1.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        pop.setVisible(false);
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            try {
                                generareRaportSelectie.setText(
                                        getLuna_anToken(
                                                (String) jTable1.getValueAt(
                                                        jTable1.getSelectedRow(), 0)));
                                generareRaportTuraNoapte.setText(
                                        getLuna_anToken_Noaptea(
                                                (String) jTable1.getValueAt(
                                                        jTable1.getSelectedRow(), 0)
                                        )
                                );
                                pop.setLocation(e.getXOnScreen(), e.getYOnScreen());
                                pop.setVisible(true);
                            } catch (Exception ex) {
                                System.out.println("Erorica la click " + ex.toString());

                            }
                        }
                    }
                });
            }else{
                System.out.println("Nu a ales fisier de tip card sofer asa ca nu updatam form principal");
            }
        } catch (IOException ex) {
            Logger.getLogger(formPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void updateUIFromBatch() {
        numeSofer.setText(Danaral.getDanaral().cititor.getSofer());
        primaInregistrare.setText(Danaral.getDanaral().cititor.getPrimaInregistrare());
        ultimaInregistrare.setText(Danaral.getDanaral().cititor.getUltimaInregistrare());
        List<RaportZi> rapoarte = Danaral.getDanaral().cititor.rapoarteZilnice;
        Danaral.getDanaral().cititor.sort(rapoarte);
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (table.getValueAt(row, column) != "") {
                    c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                } else {
                    c.setBackground(Color.GREEN);
                }
                return c;
            }
        });
        for (RaportZi raport : rapoarte) {
            //raport.show();
            for (int i = 0; i < raport.programCondus.size(); i++) {
                model.addRow(new Object[]{
                    raport.data,
                    raport.programCondus.get(i).getActivitate(),
                    raport.programCondus.get(i).getTura(),
                    raport.programCondus.get(i).getTimp(),
                    raport.programCondus.get(i).getOraInceput(),
                    raport.programCondus.get(i).getOraIncheiere()
                });
            }
            if (raport.programCondus.isEmpty()) {
                model.addRow(new Object[]{
                    raport.data,
                    "",
                    "",
                    "",
                    "",
                    ""
                });
            }
        }
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                pop.setVisible(false);
                if (e.getButton() == MouseEvent.BUTTON3) {
                    try {
                        generareRaportSelectie.setText(
                                getLuna_anToken(
                                        (String) jTable1.getValueAt(
                                                jTable1.getSelectedRow(), 0)));
                        generareRaportTuraNoapte.setText(
                                getLuna_anToken_Noaptea(
                                        (String) jTable1.getValueAt(
                                                jTable1.getSelectedRow(), 0)
                                )
                        );
                        pop.setLocation(e.getXOnScreen(), e.getYOnScreen());
                        pop.setVisible(true);
                    } catch (Exception ex) {
                        System.out.println("Erorica la click " + ex.toString());

                    }
                }
            }
        });
    }
    JMenuItem generareRaportSelectie;
    JMenuItem generareRaportTuraNoapte;

    public formPrincipal() {
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
        pop = new JPopupMenu();
        generareRaportSelectie
                = new JMenuItem("Generare raport lunar, daca apare asta contactati-l pe manel");
        generareRaportTuraNoapte
                = new JMenuItem("Generare raport tura de noapte, daca apare asta contactati-l pe emanoil");
        generareRaportSelectie.setIcon(
                new ImageIcon(
                        new ImageIcon(getClass().getResource("calendar-512.png"))
                                .getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)
                )
        );
        generareRaportTuraNoapte.setIcon(
                new ImageIcon(
                        new ImageIcon(getClass().getResource("noapte.png")).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)
                )
        );
        generareRaportTuraNoapte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop.setVisible(false);
                List<RaportZi> act = Danaral.getDanaral().cititor.
                        getRaportPeLuna((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                RaportTuraNoapte raportNoapte = new RaportTuraNoapte();
                raportNoapte.set(act);
            }
        });
        generareRaportSelectie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                pop.setVisible(false);
                List<RaportZi> act = Danaral.getDanaral().cititor.
                        getRaportPeLuna((String) jTable1.getValueAt(jTable1.getSelectedRow(), 0));
                ActivitatiDetaliate raportMare = new ActivitatiDetaliate();
                raportMare.set(act);

            }
        });
        pop.add(generareRaportTuraNoapte);
        pop.add(generareRaportSelectie);
        chooser = new JFileChooser();
        FileNameExtensionFilter htmlfilter = new FileNameExtensionFilter(
                "fisiere xhtml (*.xhtml)", "xhtml");
        chooser.setFileFilter(htmlfilter);
        jMenuItem1.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("open-512.png")).getImage().
                getScaledInstance(25, 25, Image.SCALE_SMOOTH)));

        jMenuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int x = chooser.showDialog(formPrincipal.this, "Selecteaza");
                if (x == JFileChooser.OPEN_DIALOG) {
                    updateUI();
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        numeSofer = new javax.swing.JLabel();
        primaInregistrare = new javax.swing.JLabel();
        ultimaInregistrare = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Program pentru citirea cardului soferului si a tahografului");

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jLabel1.setText("Nume șofer: ");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jLabel2.setText("Prima înregistrare:");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 13)); // NOI18N
        jLabel3.setText("Ultima înregistrare:");

        numeSofer.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N

        primaInregistrare.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N

        ultimaInregistrare.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Dată", "Activitate", "Tură", "Durată", "Oră începere", "Oră încheiere"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionForeground(new java.awt.Color(255, 0, 0));
        jTable1.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTable1);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jButton1.setFocusable(false);
        jButton1.setText("Raport lunar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton1.setFocusable(false);
        jButton2.setText("Raport ture de noapte");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jMenuItem1.setText("Deschide fisier .html");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(numeSofer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(ultimaInregistrare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(primaInregistrare, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(numeSofer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(primaInregistrare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ultimaInregistrare, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            Danaral.getDanaral().cititor.cerereRaportLunar.setVisible(true);
            CerereRaportLunar.RAPORT_SELECTAT = Danaral.RAPORT_TIP_1;
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Incarcati prima data fisierul");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            Danaral.getDanaral().cititor.cerereRaportLunar.setVisible(true);
            CerereRaportLunar.RAPORT_SELECTAT = Danaral.RAPORT_NOAPTE;
        } catch (NullPointerException ex) {
            JOptionPane.showMessageDialog(this, "Incarcati prima data fisierul");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new formPrincipal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel numeSofer;
    private javax.swing.JLabel primaInregistrare;
    private javax.swing.JLabel ultimaInregistrare;
    // End of variables declaration//GEN-END:variables
}
