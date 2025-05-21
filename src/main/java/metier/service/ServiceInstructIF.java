package metier.service;

import dao.EtablissementDao;
import dao.JpaUtil;
import dao.MatiereDao;
import dao.PersonneDao;
import dao.SoutienDao;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.modele.Autre;
import metier.modele.Eleve;
import metier.modele.Etablissement;
import metier.modele.Etudiant;
import metier.modele.Matiere;
import metier.modele.Professeur;
import metier.modele.Soutien;
import metier.modele.Tuteur;
import util.Message;

public class ServiceInstructIF {

    /**
     * * Permet d'inscrire un élève sur le réseau INSTRUCT'IF
     * @param nouvelEleve : élève à  inscrire
     * @param codeEtablissement : code de l'établissement de l'élève
     * @return Boolean : true si l'inscription a réussi, false sinon
     */
    public Boolean inscrireEleve(Eleve nouvelEleve, String codeEtablissement) {
        Boolean state = true;
        PersonneDao personneDao = new PersonneDao();
        EtablissementDao etablissementDao = new EtablissementDao();

        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();

            Etablissement etablissement = etablissementDao.findByCodeEtablissement(codeEtablissement);

            if (etablissement == null) {
                etablissement = new ServiceOutil().obtenirEtablissementDepuisApi(codeEtablissement);

                if (etablissement != null) {
                    etablissementDao.create(etablissement);
                } else {
                    throw new Exception("Impossible de récupérer l'établissement");
                }
            }

            nouvelEleve.setEtablissement(etablissement);
            personneDao.create(nouvelEleve);

            JpaUtil.validerTransaction();
            Message.envoyerMail("contact@instruct.if", nouvelEleve.getMail(), 
                    "Bienvenue sur le réseau INSTRUCT'IF", 
                    "Bonjour " + nouvelEleve.getPrenom() +", nous te confirmons ton inscription sur le réseau INSTRUCT'IF."
                            + " Si tu as besoin d'un soutien pour tes leçons ou tes devoirs, rends-toi sur notre site pour une "
                            + "mise en relation avec un intervenant."
            );
            System.out.println("Inscription réussie pour l'élève : " + nouvelEleve);
        } catch (Exception e) {
            Message.envoyerMail("contact@instruct.if", nouvelEleve.getMail(), 
                "Echec de l'instruction sur le réseau INSTRUCT'IF", 
                "Bonjour " + nouvelEleve.getPrenom() +", ton inscription sur le réseau INSTRUCT'IF "
                        + "a malencontreusement échoué... Merci de recommencer ultérieurement."
            );
            System.err.println("Erreur lors de l'inscription de l'élève : " + e);
            JpaUtil.annulerTransaction();
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return state;
    }
    
    /**
     * * Permet d'inscrire un tuteur sur le réseau INSTRUCT'IF
     * * Initialisation des tuteurs non géré par l'IHM
     * @param nouveauTuteur : tuteur à inscrire
     * @return Boolean : true si l'inscription a réussi, false sinon
     */
    public Boolean inscrireTuteur(Tuteur nouveauTuteur) {
        Boolean state = true;
        PersonneDao personneDao = new PersonneDao();
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            personneDao.create(nouveauTuteur);
            
            JpaUtil.validerTransaction();
            System.out.println("Inscription réussie pour le tuteur : " + nouveauTuteur);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'inscription du tuteur : " + e);
            JpaUtil.annulerTransaction();
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return state;
    }
    
    /**
     * * Permet de vérifier si l'email n'est associé à aucun autre élève
     * @param email : email à vérifier
     * @return Boolean : true si l'email est disponible, false sinon
     */
    public Boolean verifierEmailDisponible(String email) {
        Boolean state = false;
        PersonneDao personneDao = new PersonneDao();
        try {
            JpaUtil.creerContextePersistance();
            
            state = personneDao.findEleveByMail(email) == null;
        } catch (Exception e) {
            System.err.println("Erreur lors de la vérification de la disponibilité : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
    }
    
    /**
     * * Permet d'obtenir les informations d'un élève à partir de son id
     * @param id : id de l'élève
     * @return Eleve : l'élève correspondant à l'id ou null si aucun élève n'est trouvé
     */
    public Eleve obtenirProfilEleve(Long id) {
        Eleve eleve = null;
        PersonneDao personneDao = new PersonneDao();

        try {
            JpaUtil.creerContextePersistance();
            eleve = personneDao.findEleveById(id);
            if (eleve == null) {
                throw new Exception("Aucun élève trouvé : " + id);
            }
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la recherche : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return eleve;
        
    }
    
    /**
     * * Permet d'obtenir les informations d'un tuteur à partir de son id
     * @param id : id du tuteur
     * @return Tuteur : le tuteur correspondant à l'id ou null si aucun tuteur n'est trouvé
     */
    public Tuteur obtenirProfilTuteur(Long id) {
        Tuteur tuteur = null;
        PersonneDao personneDao = new PersonneDao();

        try {
            JpaUtil.creerContextePersistance();
            tuteur = personneDao.findTuteurById(id);
            if (tuteur == null) {
                throw new Exception("Aucun tuteur trouvé : " + id);
            }
        } catch (Exception e) {
            System.err.println("Une erreur est survenue lors de la recherche : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return tuteur;
    }
    
    /**
     * * Permet d'ajouter une matière à la liste des matières disponibles
     * * Matière non géré par l'IHM
     * @param matiere : matière à ajouter
     * @return Boolean : true si l'ajout a réussi, false sinon
     */
    public Boolean ajouterMatiere(Matiere matiere) {
        Boolean state = true;
        MatiereDao matiereDao = new MatiereDao();
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            matiereDao.create(matiere);
            
            JpaUtil.validerTransaction();
            System.out.println("matière ajoutée avec succés : " + matiere);
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur ajout matière : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
        
    }
    
    /**
     * * Permet de lister toutes les matières disponibles
     * @return List<> : liste des matières disponibles
     */
    public List<Matiere> listerMatieres() {
        List<Matiere> matieres = null;
        MatiereDao matiereDao = new MatiereDao();
        
        try {
            JpaUtil.creerContextePersistance();
            matieres = matiereDao.findAll();
        } catch (Exception e) {
            System.err.println("Une erreur est survenue : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return matieres;
    }
    
    /**
     * * Permet de lister tous les soutiens d'un élève
     * @param eleve : élève dont on veut lister les soutiens
     * @return List<> : liste des soutiens de l'élève
     */
    public List<Soutien> listerSoutiensEleve(Eleve eleve) {
        List<Soutien> soutiens = null;
        SoutienDao soutienDao = new SoutienDao();
        
        try {
            JpaUtil.creerContextePersistance();
            soutiens = soutienDao.findSoutiensByEleve(eleve);
        } catch (Exception e) {
            System.err.println("Une erreur est survenue : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return soutiens;
    }
    
    /**
     * * * Permet de lister tous les soutiens d'un tuteur
     * @param tuteur : tuteur dont on veut lister les soutiens
     * @return List<> : liste des soutiens du tuteur
     */
    public List<Soutien> listerSoutiensTuteur(Tuteur tuteur) {
        List<Soutien> soutiens = null;
        SoutienDao soutienDao = new SoutienDao();
        
        try {
            JpaUtil.creerContextePersistance();
            soutiens = soutienDao.findSoutiensByTuteur(tuteur);
        } catch (Exception e) {
            System.err.println("Une erreur est survenue : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return soutiens;
    }
    
    /**
     * * Permet de créer un soutien
     * * Le tuteur est choisi en fonction de sa disponibilité et de son niveau
     * * Le tuteur avec le moins de soutiens est choisi en priorité
     * @param soutien : soutien à  créer
     * @return Boolean : true si la création a réussi, false sinon
     */
    public Boolean creerSoutien(Soutien soutien) {
        Boolean state = true;
        SoutienDao soutienDao = new SoutienDao();
        PersonneDao personneDao = new PersonneDao();
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            int niveau = soutien.getEleve().getNiveau();
            
            // Sélectionne seulement les tuteurs qui ont le niveau
            List<Tuteur> tuteursCompatibles = personneDao.findTuteursByNiveau(niveau);
            soutien.setStatut("Refuse");
            Tuteur tuteurDisponible;
            if (!tuteursCompatibles.isEmpty()) {
                for(Tuteur tuteur : tuteursCompatibles) {
                    // Tuteur qui n'ont aucun soutien en cours
                    if (tuteur.estDisponible()) {
                        System.out.println("===============" + tuteursCompatibles + "===========================");
                        tuteurDisponible = tuteur;
                        tuteurDisponible.incrNbSoutien();
                        tuteurDisponible.addSoutien(soutien);
                        soutien.setTuteur(tuteurDisponible);
                        soutien.setStatut("Accepte");
                        soutien.genererLienVisio();
                        personneDao.update(tuteurDisponible);
                        
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String timeFormatted = sdf.format(new Date());
                        Message.envoyerNotification(soutien.getTuteur().getTelephone(),
                            "Bonjour "+soutien.getTuteur().getPrenom()+". Merci de prendre en charge la demande de soutien"
                            + " en "+soutien.getMatiere().getLibelle()+" demandée à " + timeFormatted +
                            " par " +soutien.getEleve().getPrenom()+" en classe de "+ soutien.getEleve().getClasse()
                        );
                        
                        break;
                    }
                }
            }
            
            soutienDao.create(soutien);
            personneDao.update(soutien.getEleve());
            
            JpaUtil.validerTransaction();
            
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur création soutien : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
    }
    
    /**
     * * Permet de noter un soutien
     * @param soutien : soutien à noter
     * @param note : note à attribuer au soutien
     * @return Boolean : true si la notation a réussi, false sinon
     */
    public Boolean noterSoutien(Soutien soutien, Float note) {
        Boolean state = true;
        SoutienDao soutienDao = new SoutienDao();
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            soutien.setNote(note);
            soutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            System.out.println("Soutien noté : " + soutien);
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur modification soutien : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
    }
    
    /**
     * * * Permet de rédiger le bilan d'un soutien
     * @param soutien : soutien à rédiger
     * @param bilan : bilan à rédiger
     * @return Boolean : true si la rédaction a réussi, false sinon
     */
    public Boolean redigerBilan(Soutien soutien, String bilan) {
        Boolean state = true;
        SoutienDao soutienDao = new SoutienDao();
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            soutien.setBilan(bilan);
            soutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();

            Message.envoyerMail("contact@instruct.if", soutien.getEleve().getMail(), 
                    "Bilan de soutien du " + soutien.getDateDebut(), 
                    "Bonjour " + soutien.getEleve().getPrenom() +", voici le bilan de ton soutien en "
                            + soutien.getMatiere().getLibelle() + " : \n" + bilan
            );

            System.out.println("Bilan du soutien rédigé : " + soutien);
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur modification soutien : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return state;
    }
    

    /**
     * * Permet de commencer un soutien
     * * Un soutien peut être commencé que si il a été Accepte
     * @param soutien : soutien à commencer
     * @return Boolean : true si le statut du soutien a été modifié, false sinon
     */
    public Boolean commencerSoutien(Soutien soutien) {
        Boolean state = true;
        SoutienDao soutienDao = new SoutienDao();
        
        try {
            if (soutien.getStatut().equals("Refuse")) {
                throw new Exception("Le soutien n'a pas été Accepte");
            }
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            soutien.setDateDebut(new Date());
            soutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            System.out.println("Soutien démarré : " + soutien);
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur modification soutien : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
    }
    
    /**
     * * Permet de terminer un soutien
     * * Un soutien peut Ãªtre terminé que si il a été commencé
     * @param soutien : soutien à terminer
     * @return Boolean : true si le statut du soutien a été modifié, false sinon
     */
    public Boolean terminerSoutien(Soutien soutien) {
        Boolean state = true;
        SoutienDao soutienDao = new SoutienDao();
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            if (soutien.getDateDebut() == null || soutien.getStatut().equals("Refuse")) {
                throw new Exception("Le soutien n'a pas commencé ou n'a pas été Accepte");
            } 
            soutien.setStatut("Complete");
            
            Instant now = Instant.now();
            Instant dateDebutInstant = soutien.getDateDebut().toInstant();

            Long minutes = Duration.between(dateDebutInstant, now).toMinutes();
            
            soutien.setDuree(minutes);
            
            soutienDao.updateSoutien(soutien);
            
            JpaUtil.validerTransaction();
            System.out.println("Soutien Complete : " + soutien);
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Erreur modification soutien : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
        
    }
    
    // Gestion Statistiques

    /**
     * * Permet d'obtenir les statistiques moyennes d'un tuteur
     * * Moyenne des notes obtenues
     * * Moyenne des IPS
     * * Moyenne des durées de soutien
     * @param tuteur : tuteur dont on veut les statistiques
     * @return Map<String, Double> : Map avec nom de la statistique et sa valeur
     */
    public Map<String, Double> obtenirStatistiquesMoyennes(Tuteur tuteur) {
        Map<String, Double> statistiquesMoyennes = new HashMap<>();
        SoutienDao soutienDao = new SoutienDao();

        try {
            JpaUtil.creerContextePersistance();
            statistiquesMoyennes = soutienDao.getSoutienStatsByTuteur(tuteur);
            if (statistiquesMoyennes == null) {
                throw new Exception("Aucune statistique trouvée pour le tuteur : " + tuteur.getIdentifiant());
            }
        } catch (Exception e) {
            System.err.println("Erreur d'obtention des statistiques : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return statistiquesMoyennes;
    }
    
    /**
     * * Permet d'obtenir le nombre de sessions d'un tuteur
     * @param tuteur : tuteur dont on veut le nombre de sessions
     * @return Integer : nombre de sessions du tuteur
     */
    public Integer obtenirNombreSession(Tuteur tuteur) {
        Integer nbreSessions = 0;
        SoutienDao soutienDao = new SoutienDao();
        try {
            JpaUtil.creerContextePersistance();
            nbreSessions = soutienDao.findSoutiensByTuteur(tuteur).size();
        } catch(Exception e) {
            System.err.println("Erreur d'obtention du nombre de sessions : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return nbreSessions;
    }
    
    /**
     * * Permet d'obtenir la répartition géographique des soutiens d'un tuteur
     * * Se fait par commune
     * @param tuteur : tuteur dont on veut la répartition géographique
     * @return Map<String, Integer> : Map avec nom de la commune et le nombre de soutiens
     */
    public Map<String, Integer> obtenirRepartitionGeographique(Tuteur tuteur) {
        Map<String, Integer> repartition = new HashMap<>();
        SoutienDao soutienDao = new SoutienDao();
        try {
            JpaUtil.creerContextePersistance();
            List<Soutien> interventions = soutienDao.findSoutiensByTuteur(tuteur);
            // Parcours tous les soutiens du tuteur
            for (Soutien soutien:interventions) {
                String commune = soutien.getEleve().getEtablissement().getCommune();
                Integer nbreOccurrences = repartition.get(commune);
                if (nbreOccurrences == null) {
                    nbreOccurrences = 1;
                } else {
                    nbreOccurrences++;
                }
                // Récupère le nombre d'occurence de chaque commune
                repartition.put(commune, nbreOccurrences);
            }
        } catch(Exception e) {
            System.err.println("Erreur d'obtention de la répartition géographique : " + e);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return repartition;
    }
    
    //Service initialisation
    public Boolean initialiserMatiereEtTuteur() {
        PersonneDao personneDao = new PersonneDao();
        MatiereDao matiereDao = new MatiereDao();
        Boolean state = true;
        
        List<Matiere> matieres = new ArrayList<>();
        matieres.add(new Matiere("Physique"));
        matieres.add(new Matiere("Chimie"));
        matieres.add(new Matiere("Histoire"));
        matieres.add(new Matiere("Mathematiques"));
        
        List<Tuteur> tuteurs = new ArrayList<>();
        tuteurs.add(new Etudiant("Juliette", "Magnan", "jmagnan", "azerty", "INSA Lyon", "Mathematiques", "0742123945", 0,6));
        tuteurs.add(new Autre("Pierre", "Dona", "pdona", "sherif","Retraité", "0742125115", 0,3));
        tuteurs.add(new Professeur("Lou", "Fleur", "lfleur", "mojito","Université", "0742125115", 0,6));
        
        
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            tuteurs.forEach((tuteur) -> {
                personneDao.create(tuteur);
            });
            
            matieres.forEach((matiere) -> {
                matiereDao.create(matiere);
            });
            
            JpaUtil.validerTransaction();
        } catch(Exception e) {
            JpaUtil.annulerTransaction();
            System.err.println("Service initialisation : " + e);
            state = false;
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return state;
    }
}