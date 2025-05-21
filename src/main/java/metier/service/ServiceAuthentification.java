package metier.service;

import dao.JpaUtil;
import dao.PersonneDao;
import metier.modele.Eleve;
import metier.modele.Tuteur;

public class ServiceAuthentification {
    
    /**
     * * Permet d'authentifier un élève
     * @param email : email unique de l'élève
     * @param mdp : mot de passe de l'élève
     * @return Eleve : l'élève authentifié ou null si l'authentification a échoué
     */
    public Eleve authentifierEleve(String email, String mdp) {
        Eleve eleve = null;
        JpaUtil.creerContextePersistance();
        PersonneDao personneDao = new PersonneDao();
        
        try {
            eleve = personneDao.findEleveByMail(email);
            
            if (eleve == null || !eleve.getMotDePasse().equals(mdp)) {
                eleve = null;
            }
        } catch (Exception e) {
            System.err.println("Authentification élève impossible : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return eleve;
    }
    
    /**
     * * Permet d'authentifier un tuteur
     * @param identifiant : identifiant unique du tuteur
     * @param mdp : mot de passe du tuteur
     * @return Tuteur : le tuteur authentifié ou null si l'authentification a échoué
     */
    public Tuteur authentifierTuteur(String identifiant, String mdp) {
        Tuteur tuteur = null;
        JpaUtil.creerContextePersistance();
        PersonneDao personneDao = new PersonneDao();
        
        try {
            tuteur = personneDao.findTuteurByIdentifiant(identifiant);
            
            if (tuteur == null || !tuteur.getMotDePasse().equals(mdp)) {
                tuteur = null;
            }
        } catch (Exception e) {
            System.err.println("Authentification tuteur impossible : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return tuteur;
    }
}
