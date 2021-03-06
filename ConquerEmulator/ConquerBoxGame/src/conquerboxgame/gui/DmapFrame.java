/**
 * **********************************************************************
 * Copyright 2012 Charles Benger
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * ***************************************************************************
 */
package conquerboxgame.gui;

import conquerboxgame.io.DMapLoader;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.ImageIcon;

/**
 *
 * @author chuck
 */
public class DmapFrame extends javax.swing.JFrame {

    /**
     * Creates new form DmapFrame
     */
    public DmapFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dmapPreview = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        map = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dmapPreview.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                dmapPreviewMousePressed(evt);
            }
        });

        jToolBar1.setRollover(true);

        map.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mapActionPerformed(evt);
            }
        });
        jToolBar1.add(map);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 895, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(dmapPreview, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dmapPreview, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mapActionPerformed
        
        dmapPreview.setIcon(maps.get(Integer.parseInt(map.getSelectedItem().toString())));
    }//GEN-LAST:event_mapActionPerformed

    private void dmapPreviewMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dmapPreviewMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_dmapPreviewMousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DmapFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DmapFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DmapFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DmapFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new DmapFrame().setVisible(true);
            }
        });
    }
    
    
    public int max;
    
    public void renderDMaps()
    {
        for(int key : DMapLoader.getKeys())
        {
            byte[][] dmap = DMapLoader.getDmap(key);
            
            BufferedImage image = new BufferedImage(dmap.length, dmap[0].length, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();

            for(int i = 0; i < dmap.length; i++)
                {
                    for(int j = 0; j < dmap[i].length; j++)
                    {
                        
                        if(DMapLoader.valid(key, i, j)){
                            int height = DMapLoader.getHeight(key, j, i);

                            
                            if(height > max)
                                max = height;
                            
                            g.setColor(new Color((150 + height) % 255, (150 + height) % 255, (150 + height) % 255));
                           
                            g.drawLine(i, j, i, j);   
                        } else if(DMapLoader.isPortal(key, i, j))
                        {
                            g.setColor(Color.green);
                            g.fillRect(i, j, 10, 10);
                        }
                    }

                }
         
         ImageIcon icon = new ImageIcon( image.getScaledInstance(this.getWidth() - 100, this.getHeight() - 100, BufferedImage.SCALE_SMOOTH));
         maps.put(key, icon);
         map.addItem(key);
        }
        
        System.out.println(max);
    }
    
    private HashMap<Integer, ImageIcon> maps = new HashMap<>();
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dmapPreview;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JComboBox map;
    // End of variables declaration//GEN-END:variables
}
