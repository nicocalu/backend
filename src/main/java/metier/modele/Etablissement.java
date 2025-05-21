package metier.modele;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Etablissement implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(nullable=false, unique=true)
    private String codeEtablissement;

    @Column(nullable=false)
    private String nom;
    
    @Column(nullable=false)
    private String secteur;   
    
    @Column(nullable=false)
    private String codeCommune;    
    
    @Column(nullable=false)
    private String commune;
    
    @Column(nullable=false)
    private String departement;
    
    @Column(nullable=false)
    private String codeDepartement;

    @Column(nullable=false)
    private String academie;
    
    private Double ips;

    private Double latitude;

    private Double longitude;
    
    public Etablissement() {
    }
    
    public Etablissement(String codeEtablissement, String nom, String secteur, String codeCommune, String commune, String departement, String codeDepartement, String academie, Double ips, Double latitude, Double longitude) {
        this.codeEtablissement = codeEtablissement;
        this.nom = nom;
        this.secteur = secteur;
        this.codeCommune = codeCommune;
        this.commune = commune;
        this.departement = departement;
        this.codeDepartement = codeDepartement;
        this.academie = academie;
        this.ips = ips;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public String getCodeEtablissement() {
        return codeEtablissement;
    }

    public void setCodeEtablissement(String codeEtablissement) {
        this.codeEtablissement = codeEtablissement;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getSecteur() {
        return secteur;
    }

    public void setSecteur(String secteur) {
        this.secteur = secteur;
    }
    
    public String getCodeCommune() {
        return codeCommune;
    }

    public void setCodeCommune(String codeCommune) {
        this.codeCommune = codeCommune;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    
    public String getDepartement() {
        return departement;
    }

    public void setDepartement(String departement) {
        this.departement = departement;
    }
    
    public String getCodeDepartement() {
        return codeDepartement;
    }

    public void setCodeDepartement(String codeDepartement) {
        this.codeDepartement = codeDepartement;
    }
    

    public String getAcademie() {
        return academie;
    }

    public void setAcademie(String academie) {
        this.academie = academie;
    }

    public Double getIps() {
        return ips;
    }

    public void setIps(Double ips) {
        this.ips = ips;
    }
    

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    
    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + Objects.hashCode(this.id);
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
        final Etablissement other = (Etablissement) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Etablissement{" + "id=" + id + ", codeEtablissement=" + codeEtablissement + ", nom=" + nom + ", secteur=" + secteur + ", codeCommune=" + codeCommune + ", commune=" + commune + ", departement=" + departement + ", codeDepartement=" + codeDepartement + ", academie=" + academie + ", ips=" + ips + ", latitude=" + latitude + ", longitude=" + longitude + '}';
    }
    
    
    
}
