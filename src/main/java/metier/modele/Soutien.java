package metier.modele;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Soutien implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(nullable=false)
    private String description;
    
    private String lienVisio;
    
    @Temporal(TemporalType.DATE)
    private Date dateCreation;
    
    @Temporal(TemporalType.DATE)
    private Date dateDebut;
    
    private Long duree;
    
    private String statut;
    
    private String bilan;
    
    private Float note;

    @ManyToOne(fetch = FetchType.EAGER)
    private Matiere matiere;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Eleve eleve;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Tuteur tuteur;

    public Soutien() {
    }

    public Soutien(String description, Matiere matiere, Eleve eleve) {
        this.description = description;
        this.dateCreation = new Date();
        this.duree = 0L;
        this.note = 0.0f;
        this.matiere = matiere;
        this.eleve = eleve;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLienVisio() {
        return lienVisio;
    }

    public void setLienVisio(String lienVisio) {
        this.lienVisio = lienVisio;
    }
    
    public void genererLienVisio() {
        this.lienVisio = "eleve=" + eleve.getMail() + "&intervenant=" + tuteur.getIdentifiant();
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Long getDuree() {
        return duree;
    }

    public void setDuree(Long duree) {
        this.duree = duree;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getBilan() {
        return bilan;
    }

    public void setBilan(String bilan) {
        this.bilan = bilan;
    }

    public Float getNote() {
        return note;
    }

    public void setNote(Float note) {
        this.note = note;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public Eleve getEleve() {
        return eleve;
    }

    public void setEleve(Eleve eleve) {
        this.eleve = eleve;
    }

    public Tuteur getTuteur() {
        return tuteur;
    }

    public void setTuteur(Tuteur tuteur) {
        this.tuteur = tuteur;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Soutien other = (Soutien) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Soutien{" + "id=" + id + ", description=" + description + ", lienVisio=" + lienVisio + ", dateCreation=" + dateCreation + ", duree=" + duree + ", statut=" + statut + ", bilan=" + bilan + ", note=" + note + ", matiere=" + matiere + '}';
    }
}
