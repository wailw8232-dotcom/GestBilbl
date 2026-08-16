package bibliotheque_Interface;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.HashMap;

public class PanelMemoire extends JPanel {

    JTable table;
    DefaultTableModel model;

    JTextField titreField, auteurField, anneeField, rechercheField;
    JComboBox<String> specialiteCombo;

    JButton addBtn, updateBtn, deleteBtn, searchBtn;

    HashMap<String, Integer> mapSpecialite = new HashMap<>();

    public PanelMemoire() {

        setLayout(new BorderLayout());

        // ===== TABLE =====
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Titre");
        model.addColumn("Auteur");
        model.addColumn("Année");
        model.addColumn("Spécialité");
        model.addColumn("Statut");

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // ===== FORM =====
        JPanel form = new JPanel();

        titreField = new JTextField(10);
        auteurField = new JTextField(10);
        anneeField = new JTextField(5);
        rechercheField = new JTextField(10);

        specialiteCombo = new JComboBox<>();

        form.add(new JLabel("Titre:"));
        form.add(titreField);

        form.add(new JLabel("Auteur:"));
        form.add(auteurField);

        form.add(new JLabel("Année:"));
        form.add(anneeField);

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

        // ===== LOAD DATA =====
        chargerSpecialites();
        chargerMemoire();

        // ===== EVENTS =====
        addBtn.addActionListener(e -> ajouterMemoire());
        updateBtn.addActionListener(e -> modifierMemoire());
        deleteBtn.addActionListener(e -> supprimerMemoire());
        searchBtn.addActionListener(e -> rechercherMemoire());

        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();

                titreField.setText(model.getValueAt(i, 1).toString());
                auteurField.setText(model.getValueAt(i, 2).toString());
                anneeField.setText(model.getValueAt(i, 3).toString());
                specialiteCombo.setSelectedItem(model.getValueAt(i, 4).toString());
            }
        });
    }

    // ===== LOAD SPECIALITES =====
    private void chargerSpecialites() {
        try {
            Connection conn = DBConnection.getConnection();
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

    // ===== LOAD MEMOIRES =====
    private void chargerMemoire() {
        try {
        	LoginForm log = new LoginForm();
        	log.login();
            Connection conn = log.login();

            String sql =
                "SELECT m.id_memoire, m.titre, m.auteur, m.annee, s.nom_specialite ," +
                "CASE WHEN EXISTS (SELECT 1 FROM EMPRUNT e WHERE e.id_memoire = m.id_memoire AND e.statut='en cours') " +
                "THEN 'Occupé' ELSE 'Disponible' END AS statut " +
                "FROM MEMOIRE m " +
                "JOIN SPECIALITE s ON m.id_specialite = s.id_specialite";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_memoire"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("annee"),
                        rs.getString("nom_specialite"),
                        rs.getString("statut")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // ===== ADD =====
    private void ajouterMemoire() {
        try {
            Connection conn = DBConnection.getConnection() ;

            String selected = (String) specialiteCombo.getSelectedItem();
            int idSpecialite = mapSpecialite.get(selected);

            String sql = "INSERT INTO MEMOIRE(titre, auteur, annee, id_specialite) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, titreField.getText());
            pst.setString(2, auteurField.getText());
            pst.setInt(3, Integer.parseInt(anneeField.getText()));
            pst.setInt(4, idSpecialite);

            pst.executeUpdate();

            chargerMemoire();
            JOptionPane.showMessageDialog(this, "Ajout réussi");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== UPDATE =====
    private void modifierMemoire() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
            Connection conn = DBConnection.getConnection();

            String selected = (String) specialiteCombo.getSelectedItem();
            int idSpecialite = mapSpecialite.get(selected);

            String sql = "UPDATE MEMOIRE SET titre=?, auteur=?, annee=?, id_specialite=? WHERE id_memoire=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setString(1, titreField.getText());
            pst.setString(2, auteurField.getText());
            pst.setInt(3, Integer.parseInt(anneeField.getText()));
            pst.setInt(4, idSpecialite);
            pst.setInt(5, id);

            pst.executeUpdate();

            chargerMemoire();
            JOptionPane.showMessageDialog(this, "Modification réussie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== DELETE =====
    private void supprimerMemoire() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
            Connection conn = DBConnection.getConnection();

            PreparedStatement pst = conn.prepareStatement("DELETE FROM MEMOIRE WHERE id_memoire=?");
            pst.setInt(1, id);

            pst.executeUpdate();

            chargerMemoire();
            JOptionPane.showMessageDialog(this, "Mémoire supprimé");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== SEARCH =====
    private void rechercherMemoire() {
        try {
            Connection conn = DBConnection.getConnection();

            String sql =
                "SELECT m.id_memoire, m.titre, m.auteur, m.annee, s.nom_specialite, " +
                "CASE WHEN EXISTS (SELECT 1 FROM EMPRUNT e WHERE e.id_memoire = m.id_memoire AND e.statut='en cours') " +
                "THEN 'Occupé' ELSE 'Disponible' END AS statut " +
                "FROM MEMOIRE m " +
                "JOIN SPECIALITE s ON m.id_specialite = s.id_specialite " +
                "WHERE m.titre LIKE ?";

            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, "%" + rechercheField.getText() + "%");

            ResultSet rs = pst.executeQuery();

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id_memoire"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("annee"),
                        rs.getString("nom_specialite"),
                        rs.getString("statut")
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}