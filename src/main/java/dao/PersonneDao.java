
package dao;

import java.util.List;
import javax.persistence.NoResultException;
import metier.modele.Eleve;
import metier.modele.Personne;
import metier.modele.Tuteur;

public class PersonneDao {
    
    /**
     * * Permet de cr�er une personne (�l�ve ou tuteur)
     * @param personne : la personne � cr�er
     */
    public void create(Personne personne) {
        JpaUtil.obtenirContextePersistance().persist(personne);
    }
    
    public void update(Personne personne) {
        JpaUtil.obtenirContextePersistance().merge(personne);
    }
    
    /**
     * * Permet de trouver un �l�ve par son email
     * @param email : l'email de l'�l�ve
     * @return Eleve : l'�l�ve trouv� ou null si aucun �l�ve n'est trouv�
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
     * * Permet de trouver un �l�ve par son id
     * @param id : l'id de l'�l�ve
     * @return Eleve : l'�l�ve trouv� ou null si aucun �l�ve n'est trouv�
     */
    public Eleve findEleveById(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Eleve.class, id);
    }
    
    /**
     * * Permet de trouver un tuteur par son id
     * @param id : l'id du tuteur
     * @return Tuteur : le tuteur trouv� ou null si aucun tuteur n'est trouv�
     */
    public Tuteur findTuteurById(Long id) {
        return JpaUtil.obtenirContextePersistance().find(Tuteur.class, id);
    }
    
    public void updateEleve(Eleve eleve) { // pas dans les sp�cification
        JpaUtil.obtenirContextePersistance().merge(eleve);
    }
    
    /**
     * * Permet de trouver un tuteur par son identifiant
     * @param identifiant : l'identifiant du tuteur
     * @return Tuteur : le tuteur trouv� ou null si aucun tuteur n'est trouv�
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
     * * donn� en param�tre
     * @param niveau : niveau d'enseignement recherch�
     * @return List<> : Liste de tuteur tri� par nombre de soutien croissant
     */
    public List<Tuteur> findTuteursByNiveau(int niveau) {
        return JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT t FROM Tuteur t WHERE t.niveauMin <= :niveau AND t.niveauMax >= :niveau ORDER BY t.nbSoutien ASC", Tuteur.class)
                .setParameter("niveau", niveau)
                .getResultList();
    }
}
