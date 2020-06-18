
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Saygat
 */
public class AnaEkran extends javax.swing.JFrame {
    HashMap<String, Double> kategoriler = new HashMap<>();
    HashMap<String, Double> aylar = new HashMap<>();
    HashMap<String, Double> gunler = new HashMap<>();
    double haftaici = 0;
    double haftasonu = 0;
    /**
     * Creates new form AnaEkran
     */
    public AnaEkran() {
        initComponents();
        try {
            //Dosya okuma işlemleri
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/harcamalar.txt")));
            int sira = 0;
            double toplamPara = 0;
            for (String satir; (satir = bufferedReader.readLine()) != null;) {
                if (sira != 0) {
                    ((DefaultTableModel) jTable1.getModel()).addRow(new Object[]{satir.split(";")[0], satir.split(";")[1], satir.split(";")[2], satir.split(";")[3]});
                    toplamPara += Double.valueOf(satir.split(";")[3]);
                    
                    if (kategoriler.containsKey(satir.split(";")[2])) {                    
                        kategoriler.put(satir.split(";")[2], kategoriler.get(satir.split(";")[2]) + Double.valueOf(satir.split(";")[3]));
                    } else {
                        kategoriler.put(satir.split(";")[2], Double.valueOf(satir.split(";")[3]));
                    }
                    
                    try {
                        Date tarih = new SimpleDateFormat("d.M.yyyy").parse(satir.split(";")[1]);
                        
                        if (aylar.containsKey(tarih.toString().split(" ")[1])) {                    
                            aylar.put(tarih.toString().split(" ")[1], aylar.get(tarih.toString().split(" ")[1]) + Double.valueOf(satir.split(";")[3]));
                        } else {
                            aylar.put(tarih.toString().split(" ")[1], Double.valueOf(satir.split(";")[3]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    
                    try {
                        Date tarih = new SimpleDateFormat("d.M.yyyy").parse(satir.split(";")[1]);
                        
                        if (tarih.toString().split(" ")[0].equals("Sat") || tarih.toString().split(" ")[0].equals("Sun")) {
                            haftasonu+=Double.valueOf(satir.split(";")[3]);
                        } else {
                            haftaici+=Double.valueOf(satir.split(";")[3]);
                        }
                        
                        
                        if (gunler.containsKey(tarih.toString().split(" ")[0])) {                    
                            gunler.put(tarih.toString().split(" ")[0], gunler.get(tarih.toString().split(" ")[0]) + Double.valueOf(satir.split(";")[3]));
                        } else {
                            gunler.put(tarih.toString().split(" ")[0], Double.valueOf(satir.split(";")[3]));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } else {
                    jTable1.getColumnModel().getColumn(0).setHeaderValue(satir.split(";")[0]);
                    jTable1.getColumnModel().getColumn(1).setHeaderValue(satir.split(";")[1]);
                    jTable1.getColumnModel().getColumn(2).setHeaderValue(satir.split(";")[2]);
                    jTable1.getColumnModel().getColumn(3).setHeaderValue(satir.split(";")[3]);
                }
                //System.out.println("şuan ki satır = " + satir);
                sira++;
            }
            
            
            // 4 , 10 , 2 , 8
            
            String enCokHarcamaYapanKategori = "";
            double enCokHarcamaYapanKategoriTutar = 0;
            sira = 0;
            
            for (Map.Entry<String, Double> entry : kategoriler.entrySet()) {
                if (sira == 0) {
                    enCokHarcamaYapanKategori = entry.getKey();
                    enCokHarcamaYapanKategoriTutar = entry.getValue();
                } else {
                    if (enCokHarcamaYapanKategoriTutar < entry.getValue()) {
                        enCokHarcamaYapanKategori = entry.getKey();
                        enCokHarcamaYapanKategoriTutar = entry.getValue();
                    }
                }
                ((DefaultTableModel) jTable_KATEGORILER.getModel()).addRow(new Object[]{entry.getKey(), entry.getValue()});
                sira++;
            }
            jLabel_ENCOKHARYAPKAT.setText("En çok harca yap kat : " +enCokHarcamaYapanKategori);
            String enAzHarcamaYapanAy = "";
            double enAzHarcamaYapanAyTutar = 0;
            sira = 0;
            
            for (Map.Entry<String, Double> entry : aylar.entrySet()) {
                if (sira == 0) {
                    enAzHarcamaYapanAy = entry.getKey();
                    enAzHarcamaYapanAyTutar = entry.getValue();
                } else {
                    if (enAzHarcamaYapanAyTutar > entry.getValue()) {
                        enAzHarcamaYapanAy = entry.getKey();
                        enAzHarcamaYapanAyTutar = entry.getValue();
                    }
                }
                ((DefaultTableModel) jTable_AYLAR1.getModel()).addRow(new Object[]{entry.getKey(), entry.getValue()});
                sira++;
            }
            jLabel_ENAZHARYAPAY.setText("En az har yap ay : " + enAzHarcamaYapanAy);
            
            for (Map.Entry<String, Double> entry : gunler.entrySet()) {
                ((DefaultTableModel) jTable_GUNLER.getModel()).addRow(new Object[]{entry.getKey(), entry.getValue()});
            }
            
            if (haftaici > haftasonu) {
                jLabel_HAFTASONUMU.setText("Hafta içi dah fazla har yap.");
            } else {
                jLabel_HAFTASONUMU.setText("Hafta sonu dah fazla har yap.");
            }            

            jLabel_TOPLAMPARA.setText("Toplam ödenen para : " + toplamPara);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable_KATEGORILER1 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_KATEGORILER = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable_GUNLER = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable_AYLAR1 = new javax.swing.JTable();
        jLabel_TOPLAMPARA = new javax.swing.JLabel();
        jLabel_ENCOKHARYAPKAT = new javax.swing.JLabel();
        jLabel_ENAZHARYAPAY = new javax.swing.JLabel();
        jLabel_HAFTASONUMU = new javax.swing.JLabel();

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(jTable3);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane4.setViewportView(jTable2);

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(jTable4);

        jTable_KATEGORILER1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KATEGORİ", "TOPLAM TUTAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTable_KATEGORILER1);
        if (jTable_KATEGORILER1.getColumnModel().getColumnCount() > 0) {
            jTable_KATEGORILER1.getColumnModel().getColumn(0).setResizable(false);
            jTable_KATEGORILER1.getColumnModel().getColumn(1).setResizable(false);
        }

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 861, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 697, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("tab1", jPanel1);

        jTable_KATEGORILER.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "KATEGORİ", "TOPLAM TUTAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable_KATEGORILER);
        if (jTable_KATEGORILER.getColumnModel().getColumnCount() > 0) {
            jTable_KATEGORILER.getColumnModel().getColumn(0).setResizable(false);
            jTable_KATEGORILER.getColumnModel().getColumn(1).setResizable(false);
        }

        jTable_GUNLER.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "GÜNLER", "TOPLAM TUTAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable_GUNLER);
        if (jTable_GUNLER.getColumnModel().getColumnCount() > 0) {
            jTable_GUNLER.getColumnModel().getColumn(0).setResizable(false);
            jTable_GUNLER.getColumnModel().getColumn(1).setResizable(false);
        }

        jTable_AYLAR1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "AYLAR", "TOPLAM TUTAR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane8.setViewportView(jTable_AYLAR1);
        if (jTable_AYLAR1.getColumnModel().getColumnCount() > 0) {
            jTable_AYLAR1.getColumnModel().getColumn(0).setResizable(false);
            jTable_AYLAR1.getColumnModel().getColumn(1).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 391, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(88, 88, 88)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel_ENCOKHARYAPKAT, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_TOPLAMPARA, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_ENAZHARYAPAY, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel_HAFTASONUMU, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(71, 71, 71)
                        .addComponent(jLabel_TOPLAMPARA, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_ENCOKHARYAPKAT, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_ENAZHARYAPAY, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel_HAFTASONUMU, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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
            java.util.logging.Logger.getLogger(AnaEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AnaEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AnaEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AnaEkran.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AnaEkran().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel_ENAZHARYAPAY;
    private javax.swing.JLabel jLabel_ENCOKHARYAPKAT;
    private javax.swing.JLabel jLabel_HAFTASONUMU;
    private javax.swing.JLabel jLabel_TOPLAMPARA;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable_AYLAR1;
    private javax.swing.JTable jTable_GUNLER;
    private javax.swing.JTable jTable_KATEGORILER;
    private javax.swing.JTable jTable_KATEGORILER1;
    // End of variables declaration//GEN-END:variables
}
