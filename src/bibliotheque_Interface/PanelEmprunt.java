package bibliotheque_Interface;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;

public class PanelEmprunt extends JPanel {

    JTable table;
    DefaultTableModel model;

    JComboBox<String> memoireCombo, etudiantCombo;
    JTextField joursField;

    JButton addBtn, returnBtn, deleteBtn;

    HashMap<String, Integer> mapMemoire = new HashMap<>();
    HashMap<String, Integer> mapEtudiant = new HashMap<>();

    public PanelEmprunt() {

        setLayout(new BorderLayout());

        //TABLEAU
        model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Mémoire");
        model.addColumn("Etudiant");
        model.addColumn("Date Emprunt");
        model.addColumn("Retour Prévu");
        model.addColumn("Statut");

        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        //FORM
        JPanel form = new JPanel();

        memoireCombo = new JComboBox<>();
        etudiantCombo = new JComboBox<>();
        joursField = new JTextField(5);

        form.add(new JLabel("Mémoire:"));
        form.add(memoireCombo);

        form.add(new JLabel("Etudiant:"));
        form.add(etudiantCombo);

        form.add(new JLabel("Durée (jours):"));
        form.add(joursField);

        addBtn = new JButton("Emprunter");
        returnBtn = new JButton("Retourner");
        deleteBtn = new JButton("Supprimer");

        form.add(addBtn);
        form.add(returnBtn);
        form.add(deleteBtn);

        add(form, BorderLayout.SOUTH);

        //importer les infos 
        chargerMemoire();
        chargerEtudiant();
        chargerEmprunts();

        //actions  : ADD,ALTER,DELETE au bouttons
        addBtn.addActionListener(e -> ajouterEmprunt());
        returnBtn.addActionListener(e -> retournerMemoire());
        deleteBtn.addActionListener(e -> supprimerEmprunt());
    }

    //requete select idmemoire
    private void chargerMemoire() {
        try {
        	LoginForm log = new LoginForm();
            Connection conn = log.login();
            
            
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_memoire, titre FROM MEMOIRE");

            while (rs.next()) {
                String nom = rs.getString("titre");
                int id = rs.getInt("id_memoire");

                memoireCombo.addItem(nom);
                mapMemoire.put(nom, id);
                

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //requete select id etud
    private void chargerEtudiant() {
        try {
        	
        	LoginForm log = new LoginForm();
            Connection conn = log.login();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT id_etudiant, nom FROM ETUDIANT");

            while (rs.next()) {
                String nom = rs.getString("nom");
                int id = rs.getInt("id_etudiant");

                etudiantCombo.addItem(nom);
                mapEtudiant.put(nom, id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // table EMPRUNT AVEC jointure 
    private void chargerEmprunts() {
        try {
        	LoginForm log = new LoginForm();
            Connection conn = log.login();
            String sql =
                "SELECT e.id_emprunt, m.titre, et.nom, e.date_emprunt, e.date_retour_prevue, e.statut " +
                "FROM EMPRUNT e " +
                "JOIN MEMOIRE m ON e.id_memoire = m.id_memoire " +
                "JOIN ETUDIANT et ON e.id_etudiant = et.id_etudiant";

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            model.setRowCount(0);

            while (rs.next()) {

                LocalDate today = LocalDate.now();
                LocalDate retour = rs.getDate("date_retour_prevue").toLocalDate();

                String statut = rs.getString("statut");

                if (statut.equals("en cours") && today.isAfter(retour)) {
                    statut = "en retard";
                }

                model.addRow(new Object[]{
                        rs.getInt("id_emprunt"),
                        rs.getString("titre"),
                        rs.getString("nom"),
                        rs.getDate("date_emprunt"),
                        rs.getDate("date_retour_prevue"),
                        statut
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // requete insertion
    private void ajouterEmprunt() {
        try {
        	LoginForm log = new LoginForm();
            Connection conn = log.login();

            int idMemoire = mapMemoire.get((String) memoireCombo.getSelectedItem());
            int idEtudiant = mapEtudiant.get((String) etudiantCombo.getSelectedItem());

            LocalDate today = LocalDate.now();
            int jours = Integer.parseInt(joursField.getText());
            LocalDate retour = today.plusDays(jours);

            String sql = "INSERT INTO EMPRUNT(id_memoire, id_etudiant, date_emprunt, date_retour_prevue, statut) VALUES (?, ?, ?, ?, 'en cours')";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setInt(1, idMemoire);
            pst.setInt(2, idEtudiant);
            pst.setDate(3, Date.valueOf(today));
            pst.setDate(4, Date.valueOf(retour));

            pst.executeUpdate();

            chargerEmprunts();
            JOptionPane.showMessageDialog(this, "Emprunt ajouté");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // requete  update
    private void retournerMemoire() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
        	LoginForm log = new LoginForm();
            Connection conn = log.login();

            String sql = "UPDATE EMPRUNT SET date_retour_reelle=?, statut='retourné' WHERE id_emprunt=?";
            PreparedStatement pst = conn.prepareStatement(sql);

            pst.setDate(1, Date.valueOf(LocalDate.now()));
            pst.setInt(2, id);

            pst.executeUpdate();

            chargerEmprunts();
            JOptionPane.showMessageDialog(this, "Mémoire au stock");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // requete DELETE 
    private void supprimerEmprunt() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = (int) model.getValueAt(row, 0);

        try {
        	LoginForm log = new LoginForm();
            Connection conn = log.login();

            PreparedStatement pst = conn.prepareStatement("DELETE FROM EMPRUNT WHERE id_emprunt=?");
            pst.setInt(1, id);

            pst.executeUpdate();

            chargerEmprunts();
            JOptionPane.showMessageDialog(this, "Historique de l'emprunt supprimé");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}