package bibliotheque_Interface;
import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
       

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        
        
    }

public   Connection login() {
    	 Connection conn = null;
    	 try {
    	 String url;
    	 String user;
    	 String password;
    	 url = "jdbc:mysql:#YOUR_DB_URL/GestBibl";
         
    	 
    	 user =( emailField.getText());
         password = new String(passwordField.getPassword());
         
         
         conn = DriverManager.getConnection(url, user, password);
         
         
         System.out.println("Connexion réussie !");
         
         String currentUser = getCurrentUser(conn);
         System.out.println("Connected as: " + currentUser);
         
         SwingUtilities.invokeLater(() -> {
             new MainFrame(currentUser);
         
         dispose();  });
         
         DBManager.setCredentials(user, password);
         
    	 } catch (Exception e) {
             e.printStackTrace();
         }

         return conn;
     } 

    
    private String getCurrentUser(Connection conn) throws SQLException {
	
    String sql = "SELECT CURRENT_USER() AS cu";
    try (
    		
    		PreparedStatement pst = conn.prepareStatement(sql);
        	ResultSet rs = pst.executeQuery()) {
    	
    	
        if (rs.next()) {
            String raw = rs.getString("cu"); // e.g. "logAdmin@%"
            int at = raw.indexOf('@');
            return (at >= 0) ? raw.substring(0, at) : raw;
        }
    }
    return null;
}
}
    	 
    


   
           
           
           
          
           
       
   
