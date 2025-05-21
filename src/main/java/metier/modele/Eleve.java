package metier.modele;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;

@Entity
public class Eleve extends Personne implements Serializable {
    
    @Column(nullable=false)
    private String motDePasse;
    
    @Column(nullable=false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dateNaissance;
    
    @Column(nullable=false)
    private Integer niveau;
    
    @Column(unique=true, nullable=false)
    private String mail;
    
    @ManyToOne
    private Etablissement etablissement;
    
    @OneToMany(mappedBy="eleve")
    private List<Soutien> soutiens;
    
    public Eleve() {
    }

    public Eleve(String nom, String prenom, String mail, String motDePasse, Date dateNaissance, Integer niveau) {
        super(nom, prenom);
        this.motDePasse = motDePasse;
        this.dateNaissance = dateNaissance;
        this.niveau = niveau;
        this.mail = mail;
        this.soutiens = new ArrayList<>();
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Date getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Integer getNiveau() {
        return niveau;
    }

    public void setNiveau(Integer niveau) {
        this.niveau = niveau;
    }
    
    // Traduction niveau vers classe
    public String getClasse() {
        String classe = "";            
        switch (getNiveau()) {
            case 0 :
                classe = "Terminale";
                break;
            case 1 : 
                classe = "1ère";
            case 2 :
                classe = "2nd";
                break;
            case 3 :
                classe = "3ème";
                break;
            case 4 :
                classe = "4ème";
                break;
            case 5 :
                classe = "5ème";
                break;
            case 6 :
                classe = "6ème";
                break;
        }
        
        return classe;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Etablissement getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(Etablissement etablissement) {
        this.etablissement = etablissement;
    }

    public List<Soutien> getSoutiens() {
        return soutiens;
    }

    public void setSoutiens(List<Soutien> soutiens) {
        this.soutiens = soutiens;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + Objects.hashCode(this.mail);
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
        final Eleve other = (Eleve) obj;
        if (!Objects.equals(this.mail, other.mail)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Eleve{" + "motDePasse=" + motDePasse + ", dateNaissance=" + dateNaissance + ", niveau=" + niveau + ", mail=" + mail + ", etablissement=" + etablissement + ", soutiens=" + soutiens + '}';
    }
    
    
    
}
