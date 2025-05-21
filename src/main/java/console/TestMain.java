package console;

import dao.JpaUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import metier.modele.Eleve;
import metier.modele.Matiere;
import metier.modele.Soutien;
import metier.modele.Tuteur;
import metier.service.ServiceAuthentification;
import metier.service.ServiceInstructIF;

public class TestMain {
    
    private static final String BG_BLUE = "\u001b[46m";
    private static final String RESET = "\u001B[0m";

    /**
     * @param args the command line arguments
     * @throws java.text.ParseException
     */
    public static void main(String[] args) throws ParseException {
        JpaUtil.desactiverLog();
        JpaUtil.creerFabriquePersistance();
        testNouvelUtilisateurFaitDemandeSoutienAcceptee();
        testCasLimiteTropDeDemandes();
        JpaUtil.fermerFabriquePersistance();
    }
    
    /**
     * * Teste le sc�nario o� tout se passe bien : 
     * * De la cr�ation du compte, � l'authentification, � la demande et
     * * compl�tion d'un soutien
     * @throws ParseException 
     */
    private static void testNouvelUtilisateurFaitDemandeSoutienAcceptee() throws ParseException {
        System.out.println(BG_BLUE + "=== Test Cr�ation de compte et de demande de soutien Acceptee ===" + RESET);
        ServiceInstructIF service = new ServiceInstructIF();
        ServiceAuthentification serviceAuth = new ServiceAuthentification();

        //Cr�e les Tuteurs et mati�res
        service.initialiserMatiereEtTuteur();
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateNaissance = sdf.parse("30/10/2003");
        
        if (service.verifierEmailDisponible("johnny.doe@insa-lyon.fr")) {
            Eleve eleve = new Eleve("John", "Doe", "johnny.doe@insa-lyon.fr", "dodo", dateNaissance, 2);
            //Inscrit un Eleve
            service.inscrireEleve(eleve, "0692155T");
        } else {
            System.out.println("Adresse e-mail non disponible");
            return;
        }
        
        //Se connecte � l'application
        Eleve eleveConnecte = serviceAuth.authentifierEleve("johnny.doe@insa-lyon.fr", "dodo");
        
        if (eleveConnecte != null) {
            System.out.println(eleveConnecte.getNom()+" "+eleveConnecte.getPrenom()+" est bien connect�!");
        } else {
            System.err.println("Mot de passe ou identifiant incorrect");
        }
        
        //R�cup�re la liste des mati�res
        List<Matiere> matieres = service.listerMatieres();
        
        Soutien s1 = new Soutien("Besoin d'aide pour le BAC", matieres.get(0), eleveConnecte); 
        
        //Cr�e un soutien
        service.creerSoutien(s1);
        
        //Liste la liste des soutiens de l'�l�ve connect�
        List<Soutien> soutiensEleve = service.listerSoutiensEleve(eleveConnecte);
        
        if (soutiensEleve.get(0).getStatut().equals("Accepte")) {
            System.out.println("Un tuteur a �t� trouv�");
        } else {
            System.err.println("Aucun tuteur n'a �t� trouv�");
        }
        
        //Le tuteur a re�u une notification
        //Il se connecte
        Tuteur tuteur = soutiensEleve.get(0).getTuteur();
        Tuteur tuteurConnecte = serviceAuth.authentifierTuteur(tuteur.getIdentifiant(), tuteur.getMotDePasse());
        //Il se trouve sur son historique de demande
        List<Soutien> soutiensTuteur = service.listerSoutiensTuteur(tuteurConnecte);
        
        //Les deux personnes sont en attente sur le lien visio le tuteur d�cide de lancer
        service.commencerSoutien(soutiensTuteur.get(0));
        
        //Le soutien est termin�
        service.terminerSoutien(soutiensTuteur.get(0));
        
        //Le tuteur r�dige son bilan et l'�l�ve note la session
        service.redigerBilan(soutiensTuteur.get(0), "L'�l�ve �tait attentif et a su se poser les bonnes questions!");
        soutiensEleve = service.listerSoutiensEleve(eleveConnecte);
        service.noterSoutien(soutiensEleve.get(0), 4.5f);
        
        //Le tuteur se dirige vers sa page de statistiques
        Map<String, Double> statsMoyennes = service.obtenirStatistiquesMoyennes(tuteur);
        
        if (statsMoyennes != null && !statsMoyennes.isEmpty()) {
            System.out.println("Statistiques Moyennes:");
            statsMoyennes.forEach((key, value) -> System.out.println(key + ": " + value));
        } else {
            System.err.println("Aucune moyenne disponible.");
        }
        
        Integer nombreSessions = service.obtenirNombreSession(tuteur);
        System.out.println("Nombre de sessions: " + nombreSessions);
        
        Map<String, Integer> repartitionGeo = service.obtenirRepartitionGeographique(tuteur);
        if (repartitionGeo != null && !repartitionGeo.isEmpty()) {
            System.out.println("R�partition G�ographique:");
            repartitionGeo.forEach((commune, count) -> System.out.println(commune + ": " + count));
        } else {
            System.err.println("R�partition g�ographique indisponible.");
        }
    }
    
    /**
     * * Teste plusieurs cas limite :
     * * Utilisation d'une m�me adresse mail pour 2 comptes diff�rents
     * * Demande de soutien alors que tous les tuteurs sont occup�s
     * @throws ParseException 
     */
    private static void testCasLimiteTropDeDemandes() throws ParseException {
        System.out.println(BG_BLUE + "=== Test Cas Limite : Trop de demandes en m�me temps ===" + RESET);
        ServiceInstructIF service = new ServiceInstructIF();
        ServiceAuthentification serviceAuth = new ServiceAuthentification();

        //service.initialiserMatiereEtTuteur(); // 3 tuteurs sont cr��s

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Liste d'emails � tester
        String[] emails = {
            "john.doe@insa-lyon.fr",   
            "john.doe@insa-lyon.fr",    // d�j� utilis�
            "alice.smith@insa-lyon.fr",
            "bob.johnson@insa-lyon.fr",
            "dylan.dupont@insa-lyon.fr"
        };

        List<Eleve> eleves = new java.util.ArrayList<>();

        for (int i = 0; i < emails.length; i++) {
            String email = emails[i];
            if (service.verifierEmailDisponible(email)) {
                Eleve eleve = new Eleve(
                    "User" + i,
                    "Test" + i,
                    email,
                    "pass" + i,
                    sdf.parse("01/01/200" + (i + 1)),
                    1
                );
                service.inscrireEleve(eleve, "0692155T");
                eleves.add(eleve);
                System.out.println("Compte cr�� pour : " + email);
            } else {
                System.err.println("Email d�j� utilis� : " + email);
            }
        }

        List<Matiere> matieres = service.listerMatieres();

        // Cr�ation des soutiens pour chaque �l�ve connect�
        for (Eleve e : eleves) {
            Eleve connecte = serviceAuth.authentifierEleve(e.getMail(), e.getMotDePasse());
            if (connecte != null) {
                Soutien soutien = new Soutien("Demande de soutien test", matieres.get(0), connecte);
                service.creerSoutien(soutien);

                // R�cup�rer le soutien avec statut
                List<Soutien> soutiens = service.listerSoutiensEleve(connecte);
                Soutien created = soutiens.get(0);
                System.out.println("Demande de " + connecte.getPrenom() + " avec Statut : " + created.getStatut());

                if (created.getStatut().equals("Refuse")) {
                    System.err.println("Pas de tuteur disponible pour : " + connecte.getPrenom());
                }
            }
        }

        // V�rification : nombre de soutiens Acceptes
        System.out.println(BG_BLUE + "=== R�sum� des affectations ===" + RESET);
        eleves.forEach((e) -> {
            List<Soutien> soutiens = service.listerSoutiensEleve(e);
            if (!soutiens.isEmpty()) {
                Soutien s = soutiens.get(0);
                System.out.println(e.getPrenom() + " : " + s.getStatut() + (s.getTuteur() != null ? " (Tuteur : " + s.getTuteur().getPrenom() + ")" : ""));
            } else {
                System.out.println(e.getPrenom() + " : Aucune session cr��e");
            }
        });
        
    }

    
}
