/*
 *    Copyright 2010-2011 University of Toronto
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package savant.view.dialog;

import java.awt.Window;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;

import savant.api.adapter.DataSourceAdapter;
import savant.controller.DataSourcePluginController;
import savant.plugin.SavantDataSourcePlugin;
import savant.util.MiscUtils;

/**
 * Dialog which lets user select a data-source plugin for creating a track.
 * @author mfiume, tarkvara
 */
public class DataSourcePluginDialog extends JDialog {

    List<SavantDataSourcePlugin> dataSources;

    SavantDataSourcePlugin selectedPlugin;

    /** Creates new form DataSourcePluginDialog */
    private DataSourcePluginDialog(Window parent, List<SavantDataSourcePlugin> dataSources) {
        super(parent, ModalityType.APPLICATION_MODAL);
        initComponents();
        setLocationRelativeTo(parent);
        this.dataSources = dataSources;
        initList(dataSources);
        getRootPane().setDefaultButton(loadButton);
        MiscUtils.registerCancelButton(cancelButton);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        dataSourceList = new javax.swing.JList();
        javax.swing.JLabel promptLabel = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load from Other Datasource");
        setModal(true);

        loadButton.setText("Load track");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        dataSourceList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        dataSourceList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dataSourceListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(dataSourceList);

        promptLabel.setText("Select a data source to load from:");

        cancelButton.setText("Cancel");
        cancelButton.setPreferredSize(new java.awt.Dimension(110, 29));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(loadButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 513, Short.MAX_VALUE)
                    .addComponent(promptLabel))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(promptLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(loadButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        String title = (String) this.dataSourceList.getSelectedValue();
        for (SavantDataSourcePlugin p : dataSources) {
            if (p.getTitle().equals(title)) {
                selectedPlugin = p;
                break;
            }
        }
        this.dispose();
    }//GEN-LAST:event_loadButtonActionPerformed

    /**
     * Double-clicking an item in the list is equivalent to selecting an item and
     * clicking Load Track.
     */
    private void dataSourceListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_dataSourceListMouseClicked
        if (evt.getClickCount() == 2) {
            loadButton.doClick();
        }
    }//GEN-LAST:event_dataSourceListMouseClicked

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JList dataSourceList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton loadButton;
    // End of variables declaration//GEN-END:variables

    private void initList(List<SavantDataSourcePlugin> datasources) {

        DefaultListModel listModel = new DefaultListModel();
        
        for (SavantDataSourcePlugin p : datasources) {
            listModel.addElement(p.getTitle());
        }

        this.dataSourceList.setModel(listModel);
        this.dataSourceList.setSelectedIndex(0);

    }

    /**
     * Display the DataSourcePluginDialog to configure a DataSource.  If the only
     * available data-source plugin is the Savant Repo source, just skip the dialog.
     *
     * @param parent parent window
     * @return the DataSource selected (null if dialog cancelled)
     */
    public static DataSourceAdapter getDataSource(Window parent) throws Exception {
        List<SavantDataSourcePlugin> ps = DataSourcePluginController.getInstance().getPlugins();
        if (DataSourcePluginController.getInstance().hasOnlySavantRepoDataSource()) {
            return ps.get(0).getDataSource();
        }

        DataSourcePluginDialog d = new DataSourcePluginDialog(parent, ps);
        d.setVisible(true);
        SavantDataSourcePlugin p = d.selectedPlugin;
        d.dispose();

        if (p != null) {
            return p.getDataSource();
        }
        return null;
    }
}
