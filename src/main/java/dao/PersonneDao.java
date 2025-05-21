
package dao;

import java.util.List;
import javax.persistence.NoResultException;
import metier.modele.Eleve;
import metier.modele.Personne;
import metier.modele.Tuteur;

public class PersonneDao {
    
    /**
     * * Permet de créer une personne (élève ou tuteur)
     * @param personne : la personne à créer
     */
    public void create(Personne personne) {
        JpaUtil.obtenirContextePersistance().persist(personne);
    }
    
    public void update(Personne personne) {
        JpaUtil.obtenirContextePersistance().merge(personne);
    }
    
    /**
     * * Permet de trouver un élève par son email
     * @param email : l'email de l'élève
     * @return Eleve : l'élève trouvé ou null si aucun élève n'est trouvé
     */
    public Eleve findEleveByMail(String email) {
        Eleve eleve;
        try {
            eleve = (Eleve) JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT e FROM Eleve e WHERE e.mail = :mail")
                .setParameter("mail", email)
                .getSingleResult();
        } catch (NoResultException e) {
            eleve = null;
        }
        return eleve;
    }
    
    /**
     * * Permet de trouver un élève par son id
     * @param id : l'id de l'élève
     * @return Eleve : l'élève trouvé ou null si aucun élève n'est trouvé
     */
    public Eleve findEleveById(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Eleve.class, id);
    }
    
    /**
     * * Permet de trouver un tuteur par son id
     * @param id : l'id du tuteur
     * @return Tuteur : le tuteur trouvé ou null si aucun tuteur n'est trouvé
     */
    public Tuteur findTuteurById(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Tuteur.class, id);
    }
    
    public void updateEleve(Eleve eleve) { // pas dans les spécification
        JpaUtil.obtenirContextePersistance().merge(eleve);
    }
    
    /**
     * * Permet de trouver un tuteur par son identifiant
     * @param identifiant : l'identifiant du tuteur
     * @return Tuteur : le tuteur trouvé ou null si aucun tuteur n'est trouvé
     */
    public Tuteur findTuteurByIdentifiant(String identifiant) {
        Tuteur tuteur;
        try {
            tuteur = (Tuteur) JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT t FROM Tuteur t WHERE t.identifiant = :identifiant")
                .setParameter("identifiant", identifiant)
                .getSingleResult();
        } catch (NoResultException e) {
            tuteur = null;
        }
        return tuteur;
    }
    
    /**
     * * Permet de trouver un tuteur qui est capable d'enseigner pour le niveau
     * * donné en paramètre
     * @param niveau : niveau d'enseignement recherché
     * @return List<> : Liste de tuteur trié par nombre de soutien croissant
     */
    public List<Tuteur> findTuteursByNiveau(int niveau) {
        return JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT t FROM Tuteur t WHERE t.niveauMin <= :niveau AND t.niveauMax >= :niveau ORDER BY t.nbSoutien ASC", Tuteur.class)
                .setParameter("niveau", niveau)
                .getResultList();
    }
}
