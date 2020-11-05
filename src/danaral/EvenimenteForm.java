/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package danaral;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author Manel
 */
public class EvenimenteForm extends javax.swing.JFrame {

    /**
     * Creates new form vuFaultRecords
     */
    public EvenimenteForm() {
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
        setLocation(300, 100);
                DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                        if (row != faultIndex && row != eventIndex && row != overSpeedIndex) {
                            c.setBackground(row % 2 == 0 ? Color.LIGHT_GRAY : Color.WHITE);
                        }else{
                            c.setBackground(Color.RED);
                        }

                        return c;
                    }
        };
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        
        //jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        jTable1.setDefaultRenderer(Object.class, centerRenderer);

    }
    public List<Eveniment>evenimente;
    public void resizeColumnWidth(JTable table) {
        final TableColumnModel columnModel = table.getColumnModel();
        for (int column = 0; column < table.getColumnCount(); column++) {
            int width = 50; // Min width
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width + 1, width);
            }
            if (width > 300) {
                width = 300;
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    int faultIndex = 0;
    int eventIndex = 0;
    int overSpeedIndex = 0;

    public void updateUI(List<Eveniment> faultRecords, List<Eveniment> eventRecords, List<OverSpeed> overSpeeds) {
        
        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        faultRecords.sort((d1, d2) -> {
            return d1.getDate().compareTo(d2.getDate());
        });
        eventRecords.sort((d1, d2) -> {
            return d1.getDate().compareTo(d2.getDate());
        });
        overSpeeds.sort((d1, d2) -> {
            return d1.getDate().compareTo(d2.getDate());
        });

        List<Eveniment> evenimente = new ArrayList<>();
        faultRecords.forEach((ev) -> {
            evenimente.add(ev);
        });
        eventRecords.forEach((ev) -> {
            evenimente.add(ev);
        });
        overSpeeds.forEach((ev) -> {
            evenimente.add(ev);
        });
        this.evenimente=evenimente;
        evenimente.sort((e1, e2) -> {
            return e1.getDate().compareTo(e2.getDate());
        });
        
        
        setTitle("Evenimente tahograf din "+evenimente.get(0).timpIncepere.split(" ")[0]+" pana in "+
                evenimente.get(evenimente.size()-1).timpIncheiere.split(" ")[0]);
        
        model.addRow(new Object[]{"Erori", "interne", "tahograf", faultRecords.size()});
        for (Eveniment e : faultRecords) {
            String afisareTip = e.tip;
            if (Danaral.dictionar.get(e.tip) != null) {
                afisareTip = (String) Danaral.dictionar.get(e.tip);
            }
            String afisareMotiv = e.motivInregistrare;
            if (Danaral.dictionar.get(afisareMotiv) != null) {
                afisareMotiv = (String) Danaral.dictionar.get(afisareMotiv);
            }
            model.addRow(new Object[]{
                e.timpIncepere, e.timpIncheiere, e.durata.replace("days", "zile"), afisareTip, "", "", e.cantitate, afisareMotiv
            });
        }
        model.addRow(new Object[]{"Evenimente","si","nereguli",eventRecords.size()});
        eventIndex=model.getRowCount()-1;
        for (Eveniment e : eventRecords) {
            String afisareTip=e.tip;
            if (Danaral.dictionar.get(e.tip)!=null){
                afisareTip=(String)Danaral.dictionar.get(e.tip);
            }
            
            String afisareMotiv=e.motivInregistrare;
            if (Danaral.dictionar.get(afisareMotiv)!=null){
                afisareMotiv=(String)Danaral.dictionar.get(afisareMotiv);
            }
            
            model.addRow(new Object[]{
                e.timpIncepere, e.timpIncheiere, e.durata.replace("days", "zile"), afisareTip, "", "", e.cantitate, afisareMotiv
            });
        }
        model.addRow(new Object[]{"Depasire","viteza",overSpeeds.size()});
        overSpeedIndex=model.getRowCount()-1;
        for (OverSpeed e : overSpeeds) {
            String afisareTip=e.tip;
            if (Danaral.dictionar.get(e.tip)!=null){
                afisareTip=(String)Danaral.dictionar.get(e.tip);
            }
             String afisareMotiv=e.motivInregistrare;
            if (Danaral.dictionar.get(afisareMotiv)!=null){
                afisareMotiv=(String)Danaral.dictionar.get(afisareMotiv);
            }
            model.addRow(new Object[]{
                e.timpIncepere, e.timpIncheiere, e.durata, afisareTip, e.maxSpeed, e.averageSpeed, e.cantitate, afisareMotiv
            });
        }
        resizeColumnWidth(jTable1);
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "De la", "Până la", "Durată", "Eveniment", "Viteză maximă", "Viteză medie", "Cantitate", "Scopul înregistrării"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, true, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setSelectionForeground(new java.awt.Color(255, 0, 0));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setFont(new java.awt.Font("Dialog", 1, 13)); // NOI18N
        jButton1.setText("Raport pe trei luni");
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 914, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        InterogareEvenimente interogare=new InterogareEvenimente();
        interogare.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(EvenimenteForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EvenimenteForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EvenimenteForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EvenimenteForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EvenimenteForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
