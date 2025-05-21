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
     * * Teste le scénario où tout se passe bien : 
     * * De la création du compte, à l'authentification, à la demande et
     * * complétion d'un soutien
     * @throws ParseException 
     */
    private static void testNouvelUtilisateurFaitDemandeSoutienAcceptee() throws ParseException {
        System.out.println(BG_BLUE + "=== Test Création de compte et de demande de soutien Acceptee ===" + RESET);
        ServiceInstructIF service = new ServiceInstructIF();
        ServiceAuthentification serviceAuth = new ServiceAuthentification();

        //Crée les Tuteurs et matières
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
        
        //Se connecte à l'application
        Eleve eleveConnecte = serviceAuth.authentifierEleve("johnny.doe@insa-lyon.fr", "dodo");
        
        if (eleveConnecte != null) {
            System.out.println(eleveConnecte.getNom()+" "+eleveConnecte.getPrenom()+" est bien connecté!");
        } else {
            System.err.println("Mot de passe ou identifiant incorrect");
        }
        
        //Récupère la liste des matières
        List<Matiere> matieres = service.listerMatieres();
        
        Soutien s1 = new Soutien("Besoin d'aide pour le BAC", matieres.get(0), eleveConnecte); 
        
        //Crée un soutien
        service.creerSoutien(s1);
        
        //Liste la liste des soutiens de l'élève connecté
        List<Soutien> soutiensEleve = service.listerSoutiensEleve(eleveConnecte);
        
        if (soutiensEleve.get(0).getStatut().equals("Accepte")) {
            System.out.println("Un tuteur a été trouvé");
        } else {
            System.err.println("Aucun tuteur n'a été trouvé");
        }
        
        //Le tuteur a reçu une notification
        //Il se connecte
        Tuteur tuteur = soutiensEleve.get(0).getTuteur();
        Tuteur tuteurConnecte = serviceAuth.authentifierTuteur(tuteur.getIdentifiant(), tuteur.getMotDePasse());
        //Il se trouve sur son historique de demande
        List<Soutien> soutiensTuteur = service.listerSoutiensTuteur(tuteurConnecte);
        
        //Les deux personnes sont en attente sur le lien visio le tuteur décide de lancer
        service.commencerSoutien(soutiensTuteur.get(0));
        
        //Le soutien est terminé
        service.terminerSoutien(soutiensTuteur.get(0));
        
        //Le tuteur rédige son bilan et l'élève note la session
        service.redigerBilan(soutiensTuteur.get(0), "L'élève était attentif et a su se poser les bonnes questions!");
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
            System.out.println("Répartition Géographique:");
            repartitionGeo.forEach((commune, count) -> System.out.println(commune + ": " + count));
        } else {
            System.err.println("Répartition géographique indisponible.");
        }
    }
    
    /**
     * * Teste plusieurs cas limite :
     * * Utilisation d'une même adresse mail pour 2 comptes différents
     * * Demande de soutien alors que tous les tuteurs sont occupés
     * @throws ParseException 
     */
    private static void testCasLimiteTropDeDemandes() throws ParseException {
        System.out.println(BG_BLUE + "=== Test Cas Limite : Trop de demandes en même temps ===" + RESET);
        ServiceInstructIF service = new ServiceInstructIF();
        ServiceAuthentification serviceAuth = new ServiceAuthentification();

        //service.initialiserMatiereEtTuteur(); // 3 tuteurs sont créés

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Liste d'emails à tester
        String[] emails = {
            "john.doe@insa-lyon.fr",   
            "john.doe@insa-lyon.fr",    // déjà utilisé
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
                System.out.println("Compte créé pour : " + email);
            } else {
                System.err.println("Email déjà utilisé : " + email);
            }
        }

        List<Matiere> matieres = service.listerMatieres();

        // Création des soutiens pour chaque élève connecté
        for (Eleve e : eleves) {
            Eleve connecte = serviceAuth.authentifierEleve(e.getMail(), e.getMotDePasse());
            if (connecte != null) {
                Soutien soutien = new Soutien("Demande de soutien test", matieres.get(0), connecte);
                service.creerSoutien(soutien);

                // Récupérer le soutien avec statut
                List<Soutien> soutiens = service.listerSoutiensEleve(connecte);
                Soutien created = soutiens.get(0);
                System.out.println("Demande de " + connecte.getPrenom() + " avec Statut : " + created.getStatut());

                if (created.getStatut().equals("Refuse")) {
                    System.err.println("Pas de tuteur disponible pour : " + connecte.getPrenom());
                }
            }
        }

        // Vérification : nombre de soutiens Acceptes
        System.out.println(BG_BLUE + "=== Résumé des affectations ===" + RESET);
        eleves.forEach((e) -> {
            List<Soutien> soutiens = service.listerSoutiensEleve(e);
            if (!soutiens.isEmpty()) {
                Soutien s = soutiens.get(0);
                System.out.println(e.getPrenom() + " : " + s.getStatut() + (s.getTuteur() != null ? " (Tuteur : " + s.getTuteur().getPrenom() + ")" : ""));
            } else {
                System.out.println(e.getPrenom() + " : Aucune session créée");
            }
        });
        
    }

    
}
