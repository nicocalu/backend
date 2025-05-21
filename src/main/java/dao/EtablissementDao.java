package dao;

import javax.persistence.NoResultException;
import metier.modele.Etablissement;

public class EtablissementDao {
    
    /**
     * * Permet de créer un établissement
     * @param etablissement : l'établissement à créer
     */
    public void create(Etablissement etablissement) {
        JpaUtil.obtenirContextePersistance().persist(etablissement);
    }
    
    /**
     * * Permet de trouver un établissement par son code
     * @param codeEtablissement : le code de l'établissement
     * @return Etablissement : l'établissement trouvé ou null si aucun établissement n'est trouvé
     */
    public Etablissement findByCodeEtablissement(String codeEtablissement) {
        Etablissement etablissement;
        try {
            etablissement = (Etablissement) JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT e FROM Etablissement e WHERE e.codeEtablissement = :code")
                .setParameter("code", codeEtablissement)
                .getSingleResult();
        } catch (NoResultException e) {
            etablissement = null;
        }
        return etablissement;
    }
}
