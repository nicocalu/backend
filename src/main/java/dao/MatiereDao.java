
package dao;

import java.util.List;
import metier.modele.Matiere;

public class MatiereDao {
    
    /**
     * * Permet de trouver toutes les mati�res existantes
     * @return List<> : la liste des mati�res existantes
     */
    public List<Matiere> findAll() {
        return JpaUtil.obtenirContextePersistance()
                .createQuery("SELECT m FROM Matiere m", Matiere.class)
                .getResultList();
    }
    
    public void create(Matiere matiere) {
        JpaUtil.obtenirContextePersistance().persist(matiere);
    } 
    
}
