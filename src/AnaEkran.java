
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
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
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
    HashMap<String, Double> gunHashmap = new HashMap<>();    
    HashMap<String, Double> ayHashmap = new HashMap<>();
    HashMap<String, Double> kategoriHashmap = new HashMap<>();    
    double haftaicitoplampara = 0;
    double haftasonutoplampara = 0;
    /**
     * Creates new form AnaEkran
     */
    public AnaEkran() {
        initComponents();
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.LEFT);
            jTable_KATEGORILER.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jTable_GUNLER.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
            jTable_AYLAR1.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        try {
            //Dosya okuma işlemleri
            BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("src/harcamalar.txt")));
            int sira = 0;
            double toplamPara = 0;
            for (String satir; (satir = bufferedReader.readLine()) != null;) {
                String islemNo = satir.split(";")[0];
                String islemTarihi = satir.split(";")[1];
                String islemKategori = satir.split(";")[2];
                String islemTutar = satir.split(";")[3];
                if (sira != 0) {
                    ((DefaultTableModel) jTable1.getModel()).addRow(new Object[]{islemNo, islemTarihi, islemKategori, islemTutar});
                    toplamPara += Double.valueOf(islemTutar);
                    
                    try {
                        Date tarih = new SimpleDateFormat("d.M.yyyy").parse(islemTarihi);
                        String gun = tarih.toString().split(" ")[0];
                        if (gun.equals("Sat") || gun.equals("Sun")) {
                            haftasonutoplampara+=Double.valueOf(islemTutar);
                        } else {
                            haftaicitoplampara+=Double.valueOf(islemTutar);
                        }
                        
                        
                        if (gunHashmap.containsKey(gun)) {                    
                            gunHashmap.put(gun, gunHashmap.get(gun) + Double.valueOf(islemTutar));
                        } else {
                            gunHashmap.put(gun, Double.valueOf(islemTutar));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    try {
                        Date tarih = new SimpleDateFormat("d.M.yyyy").parse(islemTarihi);
                        String ay = tarih.toString().split(" ")[1];
                        if (ayHashmap.containsKey(ay)) {                    
                            ayHashmap.put(ay, ayHashmap.get(ay) + Double.valueOf(islemTutar));
                        } else {
                            ayHashmap.put(ay, Double.valueOf(islemTutar));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    if (kategoriHashmap.containsKey(islemKategori)) {                    
                        kategoriHashmap.put(islemKategori, kategoriHashmap.get(islemKategori) + Double.valueOf(islemTutar));
                    } else {
                        kategoriHashmap.put(islemKategori, Double.valueOf(islemTutar));
                    }                    
                    
                } else {
                    jTable1.getColumnModel().getColumn(0).setHeaderValue(islemNo);
                    jTable1.getColumnModel().getColumn(1).setHeaderValue(islemTarihi);
                    jTable1.getColumnModel().getColumn(2).setHeaderValue(islemKategori);
                    jTable1.getColumnModel().getColumn(3).setHeaderValue(islemTutar);
                }
                //System.out.println("şuan ki satır = " + satir);
                sira++;
            }            
            
            String enCokHarcamaYapanKategori = "";
            double enCokHarcamaYapanKategoriTutar = 0;
            sira = 0;
            
            for (Map.Entry<String, Double> entry : kategoriHashmap.entrySet()) {
                if (sira == 0) {
                    enCokHarcamaYapanKategori = entry.getKey();
                    enCokHarcamaYapanKategoriTutar = entry.getValue();
                } else {
                    if (enCokHarcamaYapanKategoriTutar < entry.getValue()) {
                        enCokHarcamaYapanKategori = entry.getKey();
                        enCokHarcamaYapanKategoriTutar = entry.getValue();
                    }
                }
                ((DefaultTableModel) jTable_KATEGORILER.getModel()).insertRow(sira,new Object[]{entry.getKey(), entry.getValue()});
                sira++;
            }
            jLabel_ENCOKHARYAPKAT.setText("En Çok Harcama Yapılan Kategori = " +enCokHarcamaYapanKategori);
            String enAzHarcamaYapanAy = "";
            double enAzHarcamaYapanAyTutar = 0;
            sira = 0;
            
            for (Map.Entry<String, Double> entry : ayHashmap.entrySet()) {
                if (sira == 0) {
                    enAzHarcamaYapanAy = entry.getKey();
                    enAzHarcamaYapanAyTutar = entry.getValue();
                } else {
                    if (enAzHarcamaYapanAyTutar > entry.getValue()) {
                        enAzHarcamaYapanAy = entry.getKey();
                        enAzHarcamaYapanAyTutar = entry.getValue();
                    }
                }
                ((DefaultTableModel) jTable_AYLAR1.getModel()).insertRow(sira,new Object[]{ceviri(entry.getKey()), entry.getValue()});
                sira++;
            }
            jLabel_ENAZHARYAPAY.setText("En Az Harcama Yapılan Ay =  " + ceviri(enAzHarcamaYapanAy));
            sira=0;
            for (Map.Entry<String, Double> entry : gunHashmap.entrySet()) {
                ((DefaultTableModel) jTable_GUNLER.getModel()).insertRow(sira,new Object[]{ceviri(entry.getKey()), entry.getValue()});
                sira++;
            }
            
            if (haftaicitoplampara > haftasonutoplampara) {
                jLabel_HAFTASONUMU.setText("Haftaiçi Daha Fazla Harcama Yapılmıştır");
            } else {
                jLabel_HAFTASONUMU.setText("Haftasonu Daha Fazla Harcama Yapılmıştır");
            }            

            jLabel_TOPLAMPARA.setText("Toplam Yapılan Harcama Tutarı =  " + toplamPara);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnaEkran.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String ceviri(String kelime) {
        switch (kelime) {
            case "Thu":
                return "Perşembe";
            case "Tue":
                return "Salı";
            case "Sat":
                return "Cumartesi";
            case "Wed":
                return "Çarşamba";
            case "Fri":
                return "Cuma";
            case "Sun":
                return "Pazar";
            case "Mon":
                return "Pazartesi";
            case "Jul":
                return "Temmuz";
            case "Oct":
                return "Ekim";
            case "Feb":
                return "Şubat";
            case "Apr":
                return "Nisan";
            case "Jun":
                return "Haziran";
            case "Aug":
                return "Ağustos";
            case "Dec":
                return "Aralık";
            case "May":
                return "Mayıs"; 
            case "Nov":
                return "Kasım";
            case "Jan":
                return "Ocak";
            case "Mar":
                return "Mart";
            case "Sep":
                return "Eylül";    
            default:
                return "Çevrilemedi";            
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
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable_AYLAR1 = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable_GUNLER = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable_KATEGORILER = new javax.swing.JTable();
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

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setForeground(new java.awt.Color(36, 36, 41));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jTable1.setBackground(new java.awt.Color(212, 198, 191));
        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175)));
        jTable1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable1.setForeground(new java.awt.Color(36, 36, 41));
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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("GENEL HARCAMALAR", jPanel1);

        jTable_AYLAR1.setAutoCreateRowSorter(true);
        jTable_AYLAR1.setBackground(new java.awt.Color(170, 165, 175));
        jTable_AYLAR1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175)));
        jTable_AYLAR1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable_AYLAR1.setForeground(new java.awt.Color(36, 36, 41));
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

        jTabbedPane1.addTab("AYLAR TOPLAM", jScrollPane8);

        jTable_GUNLER.setBackground(new java.awt.Color(170, 165, 175));
        jTable_GUNLER.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175)));
        jTable_GUNLER.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable_GUNLER.setForeground(new java.awt.Color(36, 36, 41));
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

        jTabbedPane1.addTab("GÜNLER TOPLAM", jScrollPane7);

        jTable_KATEGORILER.setBackground(new java.awt.Color(170, 165, 175));
        jTable_KATEGORILER.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175)));
        jTable_KATEGORILER.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jTable_KATEGORILER.setForeground(new java.awt.Color(36, 36, 41));
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

        jLabel_TOPLAMPARA.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel_TOPLAMPARA.setForeground(new java.awt.Color(36, 36, 41));
        jLabel_TOPLAMPARA.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_TOPLAMPARA.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175), 2));

        jLabel_ENCOKHARYAPKAT.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel_ENCOKHARYAPKAT.setForeground(new java.awt.Color(36, 36, 41));
        jLabel_ENCOKHARYAPKAT.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_ENCOKHARYAPKAT.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175), 2));

        jLabel_ENAZHARYAPAY.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel_ENAZHARYAPAY.setForeground(new java.awt.Color(36, 36, 41));
        jLabel_ENAZHARYAPAY.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_ENAZHARYAPAY.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175), 2));

        jLabel_HAFTASONUMU.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel_HAFTASONUMU.setForeground(new java.awt.Color(36, 36, 41));
        jLabel_HAFTASONUMU.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel_HAFTASONUMU.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(170, 165, 175), 2));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 755, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel_HAFTASONUMU, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ENCOKHARYAPKAT, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_TOPLAMPARA, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel_ENAZHARYAPAY, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jLabel_TOPLAMPARA, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel_ENCOKHARYAPKAT, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel_ENAZHARYAPAY, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(jLabel_HAFTASONUMU, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("KATEGORİLER ve DETAYLAR", jPanel2);

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
