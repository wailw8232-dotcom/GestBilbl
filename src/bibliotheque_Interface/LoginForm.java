package bibliotheque_Interface;
import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class LoginForm extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JButton loginBtn;

    public LoginForm() {
        setTitle("Login");
        setSize(300, 200);
        setLayout(null);

        JLabel emailLabel = new JLabel("LOGIN:");
        emailLabel.setBounds(20, 20, 80, 25);
        add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(100, 20, 150, 25);
        add(emailField);

        JLabel passLabel = new JLabel("Mot de passe:");
        passLabel.setBounds(10, 60, 80, 30);
        add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(100, 60, 150, 25);
       add(passwordField);

        loginBtn = new JButton("Connecter");
        loginBtn.setBounds(100, 100, 100, 30);
        add(loginBtn);

        loginBtn.addActionListener(e -> login());
       // loginBtn.addActionListener(e -> getUser());

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        login();
        
    }

public   Connection login() {
    	 Connection conn = null;
    	 String url;
    	 String user;
    	 String password;
    	 
    	 
    	 user =( emailField.getText());
         password =( passwordField.getText());
         
    	 try {
              url = "jdbc:mysql://192.168.1.142:3306/GestBibl";
              user = user;
              password = password;
             
             
             
             conn = DriverManager.getConnection(url, user, password);
             System.out.println("C BON !");
         
   
             String sql = " SELECT User FROM mysql.user WHERE User = ? ;";
        		
             PreparedStatement pst = conn.prepareStatement(sql);
             pst.setString(1, emailField.getText());
             
             
             ResultSet rs = pst.executeQuery();

             if (rs.next()) {
                 String name = rs.getString("User");

                 new MainFrame(name);
                 dispose();
                 System.out.println("USER GET !");
             } 
             

         
   	 
    	 } catch (Exception e) {
             e.printStackTrace();
    	 } 
             return conn;
             
             
         }  }
    	 
    


   
           
           
           
          
           
       
   