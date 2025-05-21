package dao;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;

/**
 * Cette classe fournit des m�thodes statiques utiles pour acc�der aux
 * fonctionnalit�s de JPA (Entity Manager, Entity Transaction). Le nom de
 * l'unit� de persistance (PERSISTENCE_UNIT_NAME) doit être conforme � la
 * configuration indiqu�e dans le fichier persistence.xml du projet.
 *
 * @author DASI Team
 */
public class JpaUtil {

    /**
     * Nom de l'unit� de persistance utilis�e par la Factory de Entity Manager.
     * <br><strong>V�rifier le nom de l'unit� de persistance
     * (cf.&nbsp;persistence.xml)</strong>
     */
    public static final String PERSISTENCE_UNIT_NAME = "dev";
    /**
     * Factory de Entity Manager li�e � l'unit� de persistance.
     * <br/><strong>V�rifier le nom de l'unit� de persistance indiqu�e dans
     * l'attribut statique PERSISTENCE_UNIT_NAME
     * (cf.&nbsp;persistence.xml)</strong>
     */
    private static EntityManagerFactory entityManagerFactory = null;
    /**
     * G�re les instances courantes de Entity Manager li�es aux Threads.
     * L'utilisation de ThreadLocal garantie une unique instance courante par
     * Thread.
     */
    private static final ThreadLocal<EntityManager> threadLocalEntityManager = new ThreadLocal<EntityManager>() {

        @Override
        protected EntityManager initialValue() {
            return null;
        }
    };
    /**
     * Indicateur d'affichage du Log de JpaUtil.
     * Par d�faut, le Log de JpaUtil s'affiche dans la console.
     * Utiliser la m�thode <code>desactiverLog()</code> pour d�sactiver cet affichage.
     */
    private static boolean JPAUTIL_LOG_ACTIVE = true;

    /**
     * M�thode priv�e d'affichage du Log sur la console.
     * @param message Message � afficher dans le Log
     */
    private static void log(String message) {
        if (JPAUTIL_LOG_ACTIVE) {
            System.out.println("[JpaUtil:Log] " + message);
        }
    }
    
    /**
     * M�thode pour d�sactiver l'affichage du Log de JpaUtil.
     * À utiliser avant la m�thode <code>creerFabriquePersistance()</code> pour
     * �galement d�sactiver le Log de la librairie EclipseLink.
     */    
    public static void desactiverLog() {
        JPAUTIL_LOG_ACTIVE = false;
    }

    /**
     * Initialise la Fabrique (Factory) de Contexte de Persistance (Entity Manager).
     * <br><strong>À utiliser uniquement au d�but de la m�thode main() [projet
     * Java Application] ou dans la m�thode init() de la Servlet Contrôleur
     * (ActionServlet) [projet Web Application].</strong>
     */
    public static synchronized void creerFabriquePersistance() {
        log("Cr�ation de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
        Map<String, String> propertyMap = new HashMap<>();
        if (!JPAUTIL_LOG_ACTIVE) {
            propertyMap.put("eclipselink.logging.level", "OFF");
        }
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME,propertyMap);
    }

    /**
     * Lib�re la Fabrique (Factory) de Contexte de Persistance (Entity Manager).
     * <br><strong>À utiliser uniquement � la fin de la m�thode main() [projet
     * Java Application] ou dans la m�thode destroy() de la Servlet Contrôleur
     * (ActionServlet) [projet Web Application].</strong>
     */
    public static synchronized void fermerFabriquePersistance() {
        log("Fermeture de la fabrique de contexte de persistance");
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
            entityManagerFactory = null;
        }
    }

    /**
     * Cr��e l'instance courante du Contexte de Persistance (Entity Manager), li�e � ce Thread.
     * <br><strong>À utiliser uniquement au niveau Service.</strong>
     */
    public static void creerContextePersistance() {
        log("Cr�ation du contexte de persistance");
        threadLocalEntityManager.set(entityManagerFactory.createEntityManager());
    }

    /**
     * Ferme l'instance courante du Contexte de Persistance (Entity Manager), li�e � ce Thread.
     * <br><strong>À utiliser uniquement au niveau Service.</strong>
     */
    public static void fermerContextePersistance() {
        log("Fermeture du contexte de persistance");
        EntityManager em = threadLocalEntityManager.get();
        em.close();
        threadLocalEntityManager.set(null);
    }

    /**
     * D�marre une transaction sur l'instance courante du Contexte de Persistance (Entity Manager).
     * <br><strong>À utiliser uniquement au niveau Service.</strong>
     */
    public static void ouvrirTransaction() throws Exception {
        log("Ouverture de la transaction (begin)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().begin();
        } catch (Exception ex) {
            log("Erreur lors de l'ouverture de la transaction");
            throw ex;
        }
    }

    /**
     * Valide la transaction courante sur l'instance courante du Contexte de Persistance (Entity Manager).
     * <br><strong>À utiliser uniquement au niveau Service.</strong>
     *
     * @exception RollbackException lorsque le <em>commit</em> n'a pas r�ussi.
     */
    public static void validerTransaction() throws RollbackException, Exception {
        log("Validation de la transaction (commit)");
        try {
            EntityManager em = threadLocalEntityManager.get();
            em.getTransaction().commit();
        } catch (Exception ex) {
            log("Erreur lors de la validation (commit) de la transaction");
            throw ex;
        }
    }

    /**
     * Annule la transaction courante sur l'instance courante du Contexte de Persistance (Entity Manager).
     * Si la transaction courante n'est pas d�marr�e, cette m�thode n'effectue
     * aucune op�ration.
     * <br><strong>À utiliser uniquement au niveau Service.</strong>
     */
    public static void annulerTransaction() {
        try {
            log("Annulation de la transaction (rollback)");

            EntityManager em = threadLocalEntityManager.get();
            if (em.getTransaction().isActive()) {
                log("Annulation effective de la transaction (rollback d'une transaction active)");
                em.getTransaction().rollback();
            }

        } catch (Exception ex) {
            log("Erreur lors de l'annulation (rollback) de la transaction");
        }
    }

    /**
     * Retourne l'instance courante de Entity Manager.
     * <br><strong>À utiliser uniquement au niveau DAO.</strong>
     *
     * @return instance de Entity Manager
     */
    protected static EntityManager obtenirContextePersistance() {
        log("Obtention du contexte de persistance");
        return threadLocalEntityManager.get();
    }
}