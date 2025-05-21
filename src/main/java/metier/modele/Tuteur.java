package metier.modele;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;

@Entity
@Inheritance (strategy= InheritanceType.SINGLE_TABLE)
public class Tuteur extends Personne {
    
    @Column(unique=true, nullable=false)
    private String identifiant;
    
    @Column(nullable=false)
    private String motDePasse;
    
    @Column(nullable=false)
    private String telephone;
    
    @Column(nullable=false)
    private Integer niveauMin;
    
    @Column(nullable=false)
    private Integer niveauMax;
    
    private Integer nbSoutien;
    
    @OneToMany(mappedBy="tuteur")
    private List<Soutien> soutiens;
    
    public Tuteur() {
    }
    
    public Tuteur(String identifiant, String motDePasse, String telephone, Integer niveauMin, Integer niveauMax, String nom, String prenom) {
        super(nom, prenom);
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.telephone = telephone;
        this.niveauMin = niveauMin;
        this.niveauMax = niveauMax;
        this.nbSoutien = 0;
        this.soutiens = new ArrayList<Soutien>();
    }    

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }
    

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }
    

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getNiveauMin() {
        return niveauMin;
    }

    public void setNiveauMin(Integer niveauMin) {
        this.niveauMin = niveauMin;
    }


    public Integer getNiveauMax() {
        return niveauMax;
    }

    public void setNiveauMax(Integer niveauMax) {
        this.niveauMax = niveauMax;
    }


    public Integer getNbSoutien() {
        return nbSoutien;
    }

    public void setNbSoutien(Integer nbSoutien) {
        this.nbSoutien = nbSoutien;
    }
    
    public void incrNbSoutien() {
        ++this.nbSoutien;
    }

    public List<Soutien> getSoutiens() {
        return soutiens;
    }

    public void addSoutien(Soutien soutiens) {
        this.soutiens.add(soutiens);
    }
    
    /**
     * Indique la disponibilité du tuteur
     * @return true si : il n'a jamais fait de soutien ou si son dernier soutien est Complete
     */
    public Boolean estDisponible() {
        return soutiens.isEmpty() || soutiens.get(soutiens.size() - 1).getStatut().equals("Complete");
    }
    
    

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + Objects.hashCode(this.identifiant);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Tuteur other = (Tuteur) obj;
        if (!Objects.equals(this.identifiant, other.identifiant)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tuteur{" + "identifiant=" + identifiant + ", motDePasse=" + motDePasse + ", telephone=" + telephone + ", niveauMin=" + niveauMin + ", niveauMax=" + niveauMax + ", nbSoutien=" + nbSoutien + ", soutiens=" + soutiens + '}';
    }
}
