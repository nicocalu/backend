
package dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import metier.modele.Eleve;
import metier.modele.Soutien;
import metier.modele.Tuteur;

public class SoutienDao {
    
    /**
     * * Permet de trouver un soutien par son id
     * @param soutien : le soutien à trouver
     */
    public void create(Soutien soutien) {
        JpaUtil.obtenirContextePersistance().persist(soutien);
    }
    
    /**
     * * Permet de mettre à jour un soutien dans la base de données
     * @param soutien : le soutien à mettre à jour
     */
    public void updateSoutien(Soutien soutien) {
        JpaUtil.obtenirContextePersistance().merge(soutien);
    }
    
    /**
     * * Permet de lister les soutiens d'un tuteur
     * @param tuteur : le tuteur dont on veut lister les soutiens
     * @return List<> : la liste des soutiens du tuteur du plus récent au plus ancien
     */
    public List<Soutien> findSoutiensByTuteur(Tuteur tuteur) {
        return (List<Soutien>) JpaUtil.obtenirContextePersistance()
            .createQuery("SELECT s FROM Soutien s WHERE s.tuteur = :tuteur ORDER BY s.dateCreation DESC", Soutien.class)
            .setParameter("tuteur", tuteur)
            .getResultList();
    }
    
    /**
     * * Permet de lister les soutiens d'un élève par ordre de date de création
     * @param eleve : l'élève dont on veut lister les soutiens
     * @return List<> : la liste des soutiens de l'élève
     */
    public List<Soutien> findSoutiensByEleve(Eleve eleve) {
        return (List<Soutien>) JpaUtil.obtenirContextePersistance()
            .createQuery("SELECT s FROM Soutien s WHERE s.eleve = :eleve ORDER BY s.dateCreation DESC", Soutien.class)
            .setParameter("eleve", eleve)
            .getResultList();
    }

    /**
     * * Permet d'obtenir les statistiques de soutien d'un tuteur
     * @param tuteur : le tuteur dont on veut les statistiques
     * @return Map<String, Double> : la map contenant nom et valeur des statistiques
     * * note moyenne, IPS moyen, durée moyenne)
     */
    public Map<String, Double> getSoutienStatsByTuteur(Tuteur tuteur) {
        Object[] result = (Object[]) JpaUtil.obtenirContextePersistance()
            .createQuery(
                "SELECT AVG(s.note), AVG(s.eleve.etablissement.ips), AVG(s.duree) " +
                "FROM Soutien s WHERE s.tuteur = :tuteur AND s.statut = 'Complete'"
            )
            .setParameter("tuteur", tuteur)
            .getSingleResult();

        Map<String, Double> statistiquesMoyennes = new HashMap<>();
        statistiquesMoyennes.put("Note moyenne", result[0] != null ? (Double) result[0] : 0.0);
        statistiquesMoyennes.put("IPS moyen", result[1] != null ? (Double) result[1] : 0.0);
        statistiquesMoyennes.put("Duree moyenne", result[2] != null ? (Double) result[2] : 0.0);

        return statistiquesMoyennes;
    }
}
