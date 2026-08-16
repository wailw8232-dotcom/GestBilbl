package bibliotheque_Interface;

import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



public class MainFrame extends JFrame {

    public MainFrame(String name) {
        setTitle("Gestion Bibliothèque INFSP 500 Sétif ");
        setSize(1280, 720);

        JTabbedPane tabs = new JTabbedPane();

        
        tabs.add("Mémoires", new PanelMemoire());
        
        
         
        
        
        
        
        if (name.equals("logAdmin")) {
            tabs.add("Etudiants", new PanelEtudiant());
            tabs.add("Emprunts", new PanelEmprunt());
        }else if (name.equals("gestion")) {
           
            tabs.add("Emprunts", new PanelEmprunt());
        }

        add(tabs);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}