/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * FeatureRequestDialog.java
 *
 * Created on Mar 31, 2011, 3:56:01 PM
 */

package savant.view.dialog;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JFileChooser;
import savant.api.util.DialogUtils;
import savant.mail.Mail;
import savant.swing.component.PathField;

/**
 *
 * @author mfiume
 */
public class BugReportDialog extends javax.swing.JDialog {
    private final PathField pf;

    /** Creates new form FeatureRequestDialog */
    public BugReportDialog(java.awt.Frame parent, boolean modal) {
        this(parent,modal,null,null);
    }

     public BugReportDialog(java.awt.Frame parent, boolean modal, String description) {
         this(parent,modal,description,null);
     }

    public BugReportDialog(java.awt.Frame parent, boolean modal, String description, String path) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        this.attachment_panel.setLayout(new BorderLayout());
        pf = new PathField(JFileChooser.OPEN_DIALOG);
        this.attachment_panel.add(pf, BorderLayout.CENTER);
        if (path != null) {
            pf.setPath(path);
        }
        if(description != null) {
            field_description.setText(description);
            field_description.setCaretPosition(0);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        field_description = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        button_cancel = new javax.swing.JButton();
        button_send = new javax.swing.JButton();
        field_name = new javax.swing.JTextField();
        field_email = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        field_institution = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        field_requesttype = new javax.swing.JComboBox();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        attachment_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Report an Issue");
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13));
        jLabel1.setText("Name *");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13));
        jLabel2.setText("Email address *");

        field_description.setColumns(20);
        field_description.setLineWrap(true);
        field_description.setRows(5);
        field_description.setWrapStyleWord(true);
        jScrollPane1.setViewportView(field_description);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Description of issue *");

        button_cancel.setText("Cancel");
        button_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_cancelActionPerformed(evt);
            }
        });

        button_send.setText("Send Request");
        button_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_sendActionPerformed(evt);
            }
        });

        field_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                field_emailActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13));
        jLabel4.setText("Institution   ");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Issue Type *");

        field_requesttype.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "File Formatting", "Navigation", "Visualization", "Other" }));

        jScrollPane3.setBorder(null);
        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane3.setFocusable(false);
        jScrollPane3.setOpaque(false);

        jTextArea3.setBackground(java.awt.SystemColor.control);
        jTextArea3.setColumns(20);
        jTextArea3.setEditable(false);
        jTextArea3.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(5);
        jTextArea3.setText("We are constantly improving Savant. Please report any issues you encounter, so that we can address them. We will contact you once we have corrected the problem.");
        jTextArea3.setWrapStyleWord(true);
        jTextArea3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane3.setViewportView(jTextArea3);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Attachment   ");

        attachment_panel.setBackground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout attachment_panelLayout = new javax.swing.GroupLayout(attachment_panel);
        attachment_panel.setLayout(attachment_panelLayout);
        attachment_panelLayout.setHorizontalGroup(
            attachment_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        attachment_panelLayout.setVerticalGroup(
            attachment_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 29, Short.MAX_VALUE)
        );

        jScrollPane4.setBorder(null);
        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        jScrollPane4.setFocusable(false);
        jScrollPane4.setOpaque(false);

        jTextArea4.setBackground(java.awt.SystemColor.control);
        jTextArea4.setColumns(20);
        jTextArea4.setEditable(false);
        jTextArea4.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(5);
        jTextArea4.setText("10MB maximum. You may attach (1) a screenshot demonstrating the issue or (2) a file, if you are having trouble formatting it. Please ensure that you have permission before attaching private data.");
        jTextArea4.setWrapStyleWord(true);
        jTextArea4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jScrollPane4.setViewportView(jTextArea4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                            .addComponent(attachment_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(button_send)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(button_cancel))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                            .addComponent(field_email, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                            .addComponent(field_name, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                            .addComponent(field_institution, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)
                            .addComponent(field_requesttype, 0, 532, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(171, 171, 171)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 532, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(field_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(field_email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(field_institution, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(field_requesttype, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jLabel6))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(attachment_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_cancel)
                            .addComponent(button_send)))
                    .addComponent(jLabel3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void field_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_field_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_field_emailActionPerformed

    private void button_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_cancelActionPerformed
        this.dispose();
    }//GEN-LAST:event_button_cancelActionPerformed

    private void button_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_sendActionPerformed
        if (validateForm()) {

            String name = getPersonsName();
            String email = getEmail();
            String type = getType();
            String institution = getInstitution();
            String description = getDescription();

            String subject = "[Savant Bug Report] from " + name;
            String message = ""
                + "Name: " + name + "\n\n"
                + "Email: " + email + "\n\n"
                + "Type: " + type + "\n\n"
                + "Institution: " + institution + "\n\n"
                + "Description:\n" + description + "\n";

            this.button_send.setText("Sending...");
            this.button_send.setEnabled(false);
            this.button_cancel.setEnabled(false);

            boolean result;
            if (this.pf.getPath().equals("")) {
                result = Mail.sendEMailToDevelopers(name, subject, message);
            } else {
                result = Mail.sendEMailToDevelopers(name, subject, message, new File(this.pf.getPath()));
            }

            if (result) {
                DialogUtils.displayMessage("Report sent. Thank you for reporting your issue!");
            } else {
                DialogUtils.displayError("Error sending report. Check your internet connection or try again later.");
            }

            this.dispose();
        }
    }//GEN-LAST:event_button_sendActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BugReportDialog dialog = new BugReportDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel attachment_panel;
    private javax.swing.JButton button_cancel;
    private javax.swing.JButton button_send;
    private javax.swing.JTextArea field_description;
    private javax.swing.JTextField field_email;
    private javax.swing.JTextField field_institution;
    private javax.swing.JTextField field_name;
    private javax.swing.JComboBox field_requesttype;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    // End of variables declaration//GEN-END:variables

    private String getPersonsName() {
        return this.field_name.getText();
    }

    private String getEmail() {
        return this.field_email.getText();
    }

    private String getType() {
        return (String) this.field_requesttype.getSelectedItem();
    }

    private String getInstitution() {
        return this.field_institution.getText();
    }

    private String getDescription() {
        return this.field_description.getText();
    }

    private boolean validateForm() {
        if (this.getPersonsName().equals("")){
            DialogUtils.displayMessage("Enter your name.");
            this.field_name.requestFocus();
            return false;
        } else if (!this.getEmail().contains("@")) {
            DialogUtils.displayMessage("Enter a valid email address.");
            this.field_email.requestFocus();
            return false;
        } else if (!this.pf.getPath().equals("") && !(new File(this.pf.getPath()).exists())) {
            DialogUtils.displayMessage("The attachment does not exist at that path.");
            this.pf.requestFocus();
            return false;
        } else if (fileSize(new File(this.pf.getPath())) > 10000000) {
            DialogUtils.displayMessage("The attachment is too large.\nIf you are having issues formatting this file,\nyou could attach a file that contains the first few lines (e.g. 30 lines). That is often enough to diagnose the problem.");
            this.pf.requestFocus();
            return false;
        }
        else if (this.getDescription().equals("")) {
            DialogUtils.displayMessage("Enter a description of the feature.");
            this.field_description.requestFocus();
            return false;
        }
        return true;
    }

    private long fileSize(File file) {
        return file.length();
    }

}