package machine;

import util.Constantes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Représente un site. Peut lancer plusieures tâches de calcul et est considéré
 * actif tant qu'une tâche est en cours.
 */
public class Site {
    //L'id du site
    private int id;
    //Le socket permettant la communication
    private DatagramSocket socket;
    //Le nombre de tâches en cours sur ce site
    private AtomicInteger tachesEnCours;
    //Indique le site est terminé ou non (aucune tâche ne pourra être créée une fois terminé)
    private Boolean fini = false;
    //Permet de quitter l'application via la saisie utilisateur
    private Boolean quitter = false;
    //Indique si le site est resté inactif depuis le dernier envoi de jeton
    private Boolean resteInactif = true;
    //Permet la saisie utilisateur
    private Scanner scanner = new Scanner(System.in);


    /**
     * Contructeur d'un site à partir d'une id et d'un port.
     *
     * @param id   int
     * @param port int
     */
    public Site(int id, int port) {
        this.id = id;
        tachesEnCours = new AtomicInteger(0);
        try {
            socket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.err.println("Erreur lors de l'initialisation du socket du site " + id + ".");
        }

        //Lancement du thread communicateur recevant les messages
        Thread t = new Thread(new Communicateur(this));
        t.start();

        //Lancement d'une tache
        lancerTache();
        //Démarre la boucle principale de saisie utilisateur
        bouclerDemandeSaisie();
    }

    /**
     * Lance une boucle demandant la saisie utilisateur
     */
    private void bouclerDemandeSaisie() {
        while (!quitter) {
            afficherChoix();
            int saisie = scanner.nextInt();
            switch (saisie) {
                case 1:
                    lancerTache();
                    break;
                case 2:
                    fini = true;
                    envoyerFin(id);
                    break;
                case 3:
                    quitter = true;
                    socket.close();
                    break;
                default:
                    System.out.println("Saisie incorrecte, reessayez");
            }
        }
    }

    /**
     * Affiche les choix disponibles à l'utilisateur
     */
    private void afficherChoix() {
        System.out.println("Veuillez saisir un numero: ");
        System.out.println("\t1. Lancer une nouvelle tache sur ce site");
        System.out.println("\t2. Demander la terminaison de ce site");
        System.out.println("\t3. Quitter");
    }

    /**
     * Lance une nouvelle tâche sur ce site. Si le site est fini, signale une
     * erreur et ne lance pas de tâche.
     */
    public void lancerTache() {
        if (!fini) {
            System.out.println("Lancement d'une nouvelle tache sur le site " + id + ".");
            resteInactif = false;
            tachesEnCours.incrementAndGet();
            Thread boucleTerminaison = new Thread(new Tache(fini, this));
            boucleTerminaison.start();
        } else {
            System.err.println("Impossible de lancer une tache sur le site " + id + ". Il est termine.");
        }
    }

    /**
     * Lance une demande de lancement de tâche sur le site en paramètre. La
     * demande est envoyée au voisin mais contient l'ID du site destinataire.
     *
     * @param i int
     */
    public void lancerThreadSurSite(int i) {
        byte[] tampon = new byte[Constantes.TAILLE_TAMPON_NOUV_TACHE];
        tampon[0] = Constantes.NOUV_TACHE;
        tampon[1] = (byte) i;
        try {
            DatagramPacket paquet = new DatagramPacket(tampon, tampon.length,
                    InetAddress.getByName(Constantes.ADRESSES_IP[prochainSite()]), Constantes.PORTS[prochainSite()]);
            socket.send(paquet);
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement de la tache sur le site "
                    + i + " depuis le site " + id + ".");
        }
    }

    /**
     * Envoi le jeton au voisin. Contient l'emetteur du jeton (paramètre i)
     *
     * @param i int
     */
    public void envoyerJeton(int i) {
        //On se déclare comme inactif
        resteInactif = true;
        byte[] tampon = new byte[Constantes.TAILLE_TAMPON_JETON];
        tampon[0] = Constantes.JETON;
        tampon[1] = (byte) i;
        try {
            DatagramPacket paquet = new DatagramPacket(tampon, tampon.length,
                    InetAddress.getByName(Constantes.ADRESSES_IP[prochainSite()]), Constantes.PORTS[prochainSite()]);
            socket.send(paquet);
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement de la tache sur le site "
                    + i + " depuis le site " + this.id + ".");
        }
    }

    /**
     * Envoi le message de fin au voisin. Contient l'émetteur du message (paramètre i)
     *
     * @param i
     */
    public void envoyerFin(int i) {
        byte[] tampon = new byte[Constantes.TAILLE_TAMPON_FIN];
        tampon[0] = Constantes.FIN;
        tampon[1] = (byte) i;
        try {
            DatagramPacket paquet = new DatagramPacket(tampon, tampon.length,
                    InetAddress.getByName(Constantes.ADRESSES_IP[prochainSite()]), Constantes.PORTS[prochainSite()]);
            socket.send(paquet);
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement de la tache sur le site "
                    + i + " depuis le site " + this.id + ".");
        }

    }


    //Getters et Setters de la classe Site

    public DatagramSocket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }

    public int prochainSite() {
        return (id + 1) % Constantes.NOMBRE_DE_SITES;
    }

    public AtomicInteger getTachesEnCours() {
        return tachesEnCours;
    }

    public Boolean getResteInactif() {
        return resteInactif;
    }

    public void setFini(boolean fini) {
        this.fini = fini;
    }

    public void setResteInactif(boolean resteInactif) {
        this.resteInactif = resteInactif;
    }
}
