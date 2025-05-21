package metier.modele;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ETUDIANT")
public class Etudiant extends Tuteur implements Serializable {
    
    private String universite;
    
    private String specialite;

    public Etudiant() {
    }

    public Etudiant(String nom, String prenom, String identifiant, String motDePasse, String universite, String specialite, String telephone, Integer niveauMin, Integer niveauMax) {
        super(identifiant, motDePasse, telephone, niveauMin, niveauMax, nom, prenom);
        this.universite = universite;
        this.specialite = specialite;
    }

    public String getUniversite() {
        return universite;
    }

    public void setUniversite(String universite) {
        this.universite = universite;
    }

    public String getSpecialite() {
        return specialite;
    }

    public void setSpecialite(String specialite) {
        this.specialite = specialite;
    }

    @Override
    public String toString() {
        return "Etudiant{" + super.toString() + "universite=" + universite + ", specialite=" + specialite + '}';
    }
    
    
    
    
}
