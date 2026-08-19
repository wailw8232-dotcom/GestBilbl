package bibliotheque_Interface;
import javax.swing.*; 
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.HashMap;

public class PanelEtudiant extends JPanel {

    JTable table;
    DefaultTableModel model;

    JTextField nomField, prenomField, emailField, rechercheField;
    JComboBox<String> specialiteCombo;

    JButton addBtn, deleteBtn, updateBtn, searchBtn;

    // Map
    HashMap<String, Integer> mapSpecialite = new HashMap<>();

    public PanelEtudiant() {

        setLayout(new BorderLayout());

        //TABLE 
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Nom");
        model.addColumn("Prénom");
        model.addColumn("Email");
        model.addColumn("Spécialité");

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        //FORM 
        JPanel form = new JPanel();

        nomField = new JTextField(8);
        prenomField = new JTextField(8);
        emailField = new JTextField(10);
        rechercheField = new JTextField(10);

        specialiteCombo = new JComboBox<>();

        form.add(new JLabel("Nom:"));
        form.add(nomField);

        form.add(new JLabel("Prénom:"));
        form.add(prenomField);

        form.add(new JLabel("Email:"));
        form.add(emailField);

        form.add(new JLabel("Spécialité:"));
        form.add(specialiteCombo);

        form.add(new JLabel("Recherche:"));
        form.add(rechercheField);

        addBtn = new JButton("Ajouter");
        updateBtn = new JButton("Modifier");
        deleteBtn = new JButton("Supprimer");
        searchBtn = new JButton("Rechercher");

        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);
        form.add(searchBtn);

        add(form, BorderLayout.SOUTH);

        //appel
        chargerSpecialites();
        chargerEtudiants();

        //boutton d'actions 
        addBtn.addActionListener(e -> ajouterEtudiant());
        updateBtn.addActionListener(e -> modifierEtudiant());
        deleteBtn.addActionListener(e -> supprimerEtudiant());
        searchBtn.addActionListener(e -> rechercherEtudiant());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();

                nomField.setText(model.getValueAt(i, 1).toString());
                prenomField.setText(model.getValueAt(i, 2).toString());
                emailField.setText(model.getValueAt(i, 3).toString());
                specialiteCombo.setSelectedItem(model.getValueAt(i, 4).toString());
            }
        });
    }

    // LOAD SPECIALITES
    private void chargerSpecialites() {
        try {
        	Connection conn = DBManager.getConnection();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM SPECIALITE");

            while (rs.next()) {
                String nom = rs.getString("nom_specialite");
                int id = rs.getInt("id_specialite");

                specialiteCombo.addItem(nom);
                mapSpecialite.put(nom, id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  LOAD ETUDIANTS
    private void chargerEtudiants() {
        try {
        	Connection conn = DBManager.getConnection();

            String sql = "SELECT e.id_etudiant, e.nom, e.prenom, e.email, s.nom_specialite " +
                         "FROM ETUDIANT e LEFT JOIN SPECIALITE s ON e.id_specialite = s.id_specialite";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_etudiant"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("nom_specialite")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ADD
    private void ajouterEtudiant() {
        try {
        	Connection conn = DBManager.getConnection();

            String selected = (String) specialiteCombo.getSelectedItem();
            int idSpecialite = mapSpecialite.get(selected);

            String sql = "INSERT INTO ETUDIANT(nom, prenom, email, id_specialite) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nomField.getText());
            pst.setString(2, prenomField.getText());
            pst.setString(3, emailField.getText());
            pst.setInt(4, idSpecialite);

            pst.executeUpdate();

            chargerEtudiants();
            JOptionPane.showMessageDialog(this, "Ajout réussi");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE 
    private void modifierEtudiant() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
        	Connection conn = DBManager.getConnection();

            String selected = (String) specialiteCombo.getSelectedItem();
            int idSpecialite = mapSpecialite.get(selected);

            String sql = "UPDATE ETUDIANT SET nom=?, prenom=?, email=?, id_specialite=? WHERE id_etudiant=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, nomField.getText());
            pst.setString(2, prenomField.getText());
            pst.setString(3, emailField.getText());
            pst.setInt(4, idSpecialite);
            pst.setInt(5, id);

            pst.executeUpdate();

            chargerEtudiants();
            JOptionPane.showMessageDialog(this, "Modification réussie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  DELETE 
    private void supprimerEtudiant() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
        	Connection conn = DBManager.getConnection();

            PreparedStatement pst = conn.prepareStatement("DELETE FROM ETUDIANT WHERE id_etudiant=?");
            pst.setInt(1, id);

            pst.executeUpdate();

            chargerEtudiants();
            JOptionPane.showMessageDialog(this, "Etudiant supprimé");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // SEARCH
    private void rechercherEtudiant() {
        try {
            Connection conn = DBManager.getConnection();

            String sql = "SELECT e.id_etudiant, e.nom, e.prenom, e.email, s.nom_specialite " +
                         "FROM ETUDIANT e LEFT JOIN SPECIALITE s ON e.id_specialite = s.id_specialite " +
                         "WHERE e.nom LIKE ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + rechercheField.getText() + "%");

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_etudiant"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("nom_specialite")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
