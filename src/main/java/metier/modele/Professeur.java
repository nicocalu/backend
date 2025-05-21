package metier.modele;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PROFESSEUR")
public class Professeur extends Tuteur {
    
    private String typeEtablissement;

    public Professeur() {
    }

    public Professeur(String nom, String prenom, String identifiant, String motDePasse, String typeEtablissement, String telephone, Integer niveauMin, Integer niveauMax) {
        super(identifiant, motDePasse, telephone, niveauMin, niveauMax, nom, prenom);
        this.typeEtablissement = typeEtablissement;
    }

    public String getTypeEtablissement() {
        return typeEtablissement;
    }

    public void setTypeEtablissement(String typeEtablissement) {
        this.typeEtablissement = typeEtablissement;
    }

    @Override
    public String toString() {
        return "Professeur{" + super.toString() + "typeEtablissement=" + typeEtablissement + '}';
    }
    
    
    
}
