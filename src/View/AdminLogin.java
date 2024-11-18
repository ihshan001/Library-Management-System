
package View;


import java.awt.Color;
import library.management.system.UI.HoverEffect;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author AL MUBDIE
 */
public class AdminLogin extends javax.swing.JFrame {

    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
    String jdbcUrl = "jdbc:mysql://localhost:3306/lms";
    String user = "root";
    String dbpassword = "";
    
    public AdminLogin() {
        initComponents();
        
         HoverEffect.glowEffect(loginBtn);
         HoverEffect.glowEffect(resetBtn);
         HoverEffect.glowEffect(signUpBtn);

       
    }
    private void login(){
     String username = uNameTxt.getText();
        String password = String.valueOf(passTxt.getPassword());

    try {
        con = DriverManager.getConnection(jdbcUrl, user, dbpassword);
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        pst = con.prepareStatement(sql);
        pst.setString(1, username);
        pst.setString(2, password);
        rs = pst.executeQuery();

        if (rs.next()) {
            // Username and password match, proceed to dashboard
            JOptionPane.showMessageDialog(this, "Login Successful");
            dashboard d = new dashboard();
            this.dispose();  // Close login window
            d.setVisible(true);  // Open dashboard window
        } else {
            // Username or password incorrect
            JOptionPane.showMessageDialog(this, "Invalid username or password");
        }

    } catch (SQLException ex) {
        Logger.getLogger(AdminLogin.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Database connection failed");
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

        rSMetroTextPlaceHolderBeanInfo1 = new rojerusan.RSMetroTextPlaceHolderBeanInfo();
        rSPasswordTextPlaceHolderBeanInfo1 = new rojerusan.RSPasswordTextPlaceHolderBeanInfo();
        rSPasswordTextPlaceHolderBeanInfo2 = new rojerusan.RSPasswordTextPlaceHolderBeanInfo();
        customUI1 = new necesario.CustomUI();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        uNameTxt = new rojerusan.RSMetroTextPlaceHolder();
        passTxt = new rojerusan.RSPasswordTextPlaceHolder();
        loginBtn = new rojerusan.RSMaterialButtonRectangle();
        resetBtn = new rojerusan.RSMaterialButtonRectangle();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        signUpBtn = new rojerusan.RSMaterialButtonRectangle();
        rSButtonHover1 = new rojerusan.RSButtonHover();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        rSLabelImage1 = new rojerusan.RSLabelImage();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Password");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, -1, -1));

        uNameTxt.setBackground(new java.awt.Color(0, 0, 0));
        uNameTxt.setForeground(new java.awt.Color(255, 102, 0));
        uNameTxt.setBorderColor(new java.awt.Color(153, 153, 153));
        uNameTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        uNameTxt.setPhColor(new java.awt.Color(51, 51, 51));
        uNameTxt.setPlaceholder("Enter Username");
        jPanel2.add(uNameTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 120, -1, 40));

        passTxt.setBackground(new java.awt.Color(0, 0, 0));
        passTxt.setForeground(new java.awt.Color(255, 102, 0));
        passTxt.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N
        passTxt.setPhColor(new java.awt.Color(51, 51, 51));
        passTxt.setPlaceholder("Enter Password");
        jPanel2.add(passTxt, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 200, 250, 40));

        loginBtn.setBackground(new java.awt.Color(255, 153, 0));
        loginBtn.setText("login");
        loginBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });
        jPanel2.add(loginBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 270, 112, 38));

        resetBtn.setBackground(new java.awt.Color(255, 102, 0));
        resetBtn.setText("reset");
        resetBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        resetBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resetBtnMouseClicked(evt);
            }
        });
        jPanel2.add(resetBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 114, 38));

        jLabel4.setBackground(new java.awt.Color(255, 102, 0));
        jLabel4.setFont(new java.awt.Font("Source Sans 3 Black", 0, 38)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 102, 0));
        jLabel4.setText("In");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 30, 40, -1));

        jLabel5.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Username");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 90, -1, -1));

        signUpBtn.setBackground(new java.awt.Color(255, 153, 0));
        signUpBtn.setText("Sign Up");
        signUpBtn.setFont(new java.awt.Font("Century Gothic", 1, 14)); // NOI18N
        signUpBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signUpBtnMouseClicked(evt);
            }
        });
        jPanel2.add(signUpBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 200, 150, 50));

        rSButtonHover1.setBackground(new java.awt.Color(255, 153, 0));
        rSButtonHover1.setText("X");
        rSButtonHover1.setColorHover(new java.awt.Color(51, 51, 51));
        rSButtonHover1.setColorTextHover(new java.awt.Color(255, 153, 0));
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        jPanel2.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 50, 30));

        jLabel9.setFont(new java.awt.Font("Source Sans 3 SemiBold", 0, 24)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Hello, Friend");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 130, -1, -1));

        jLabel8.setFont(new java.awt.Font("Source Sans 3 Medium", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(51, 51, 51));
        jLabel8.setText("Doesn't have an account?");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 170, -1, -1));

        jLabel6.setBackground(new java.awt.Color(255, 153, 0));
        jLabel6.setFont(new java.awt.Font("Source Sans 3 Black", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 153, 0));
        jLabel6.setText("Sign");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 32, -1, 50));

        rSLabelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Images/bg/login1.jpg"))); // NOI18N
        jPanel2.add(rSLabelImage1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-120, 0, 660, 360));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 360));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        // TODO add your handling code here:
        login();
    }//GEN-LAST:event_loginBtnActionPerformed

    private void signUpBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signUpBtnMouseClicked
        // TODO add your handling code here:
        Registration R= new Registration();
        this.hide();
        R.setVisible(true);
    }//GEN-LAST:event_signUpBtnMouseClicked

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void resetBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resetBtnMouseClicked
        // TODO add your handling code here:
        uNameTxt.setText("");
        passTxt.setText("");
    }//GEN-LAST:event_resetBtnMouseClicked

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
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AdminLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private necesario.CustomUI customUI1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private rojerusan.RSMaterialButtonRectangle loginBtn;
    private rojerusan.RSPasswordTextPlaceHolder passTxt;
    private rojerusan.RSButtonHover rSButtonHover1;
    private rojerusan.RSLabelImage rSLabelImage1;
    private rojerusan.RSMetroTextPlaceHolderBeanInfo rSMetroTextPlaceHolderBeanInfo1;
    private rojerusan.RSPasswordTextPlaceHolderBeanInfo rSPasswordTextPlaceHolderBeanInfo1;
    private rojerusan.RSPasswordTextPlaceHolderBeanInfo rSPasswordTextPlaceHolderBeanInfo2;
    private rojerusan.RSMaterialButtonRectangle resetBtn;
    private rojerusan.RSMaterialButtonRectangle signUpBtn;
    private rojerusan.RSMetroTextPlaceHolder uNameTxt;
    // End of variables declaration//GEN-END:variables
}