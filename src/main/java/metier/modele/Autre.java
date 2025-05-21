package metier.modele;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("AUTRE")
public class Autre extends Tuteur implements Serializable {
    
    private String activite;

    public Autre() {
    }

    public Autre(String nom, String prenom, String identifiant, String motDePasse, String activite, String telephone, Integer niveauMin, Integer niveauMax) {
        super(identifiant, motDePasse, telephone, niveauMin, niveauMax, nom, prenom);
        this.activite = activite;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    @Override
    public String toString() {
        return "Autre{" + super.toString() + "activite=" + activite + '}';
    }
    
    

    
}
