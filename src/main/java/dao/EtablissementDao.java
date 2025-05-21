package dao;

import javax.persistence.NoResultException;
import metier.modele.Etablissement;

public class EtablissementDao {
    
    /**
     * * Permet de cr�er un �tablissement
     * @param etablissement : l'�tablissement � cr�er
     */
    public void create(Etablissement etablissement) {
        JpaUtil.obtenirContextePersistance().persist(etablissement);
    }
    
    /**
     * * Permet de trouver un �tablissement par son code
     * @param codeEtablissement : le code de l'�tablissement
     * @return Etablissement : l'�tablissement trouv� ou null si aucun �tablissement n'est trouv�
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
