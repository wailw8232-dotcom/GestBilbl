package bibliotheque_Interface;

import java.util.HashMap;

public class hash {
    public static void main(String[] args) {
        // Initialisation de la map
        HashMap<String, Integer> mapMemoire = new HashMap<>();

        // Ajout d'exemples d'auteurs (Noms fictifs basés sur les promos typiques)
        mapMemoire.put("Amine_Benhamadi", 2023);
        mapMemoire.put("Sarah_Bouzidi", 2024);
        mapMemoire.put("Fouad_Kaci", 2022);
        mapMemoire.put("Meriem_Lamine", 2024);

        // Affichage pour vérification
        System.out.println("Liste des auteurs et années : " + mapMemoire);
    }
}
