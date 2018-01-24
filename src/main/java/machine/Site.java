package machine;

import util.Constantes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class Site {
    private int id;
    private int port;
    private DatagramSocket socket;
    private Boolean fini = false;
    private Boolean quitter = false;
    private Scanner scanner = new Scanner(System.in);


    public Site(int id, int port){
        this.id = id;
        try{
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

    private void bouclerDemandeSaisie() {
        while(!quitter){
            afficherChoix();
            int saisie = scanner.nextInt();
            switch(saisie){
                case 1:
                    lancerTache();
                    break;
                case 2:
                    fini = true;
                    socket.close();
                    break;
                case 3:
                    quitter = true;
                    break;
                default:
                    System.out.println("Saisie incorrecte, reessayez");
            }
        }
    }

    private void afficherChoix() {
        System.out.println("Veuillez saisir un numero: ");
        System.out.println("\t1. Lancer une nouvelle tache sur ce site");
        System.out.println("\t2. Demander la terminaison de ce site");
        System.out.println("\t3. Quitter");
    }

    public void lancerTache() {
        if(!fini){
            System.out.println("Lancement d'une nouvelle tache sur le site " + id + ".");
            Thread boucleTerminaison = new Thread(new BoucleTerminaison(fini, this));
            boucleTerminaison.start();
        }else{
            System.err.println("Impossible de lancer une tache sur le site " + id + ". Il est termine.");
        }
    }


    public void lancerThreadSurSite(int i) {
        byte[] tampon = new byte[Constantes.TAILLE_TAMPON_NOUV_TACHE];
        try {
            DatagramPacket paquet = new DatagramPacket(tampon, tampon.length,
                InetAddress.getByName(Constantes.ADRESSES_IP[i]), Constantes.PORTS[i]);
            socket.send(paquet);
        } catch (IOException e) {
            System.err.println("Erreur lors du lancement de la tache sur le site "
            + i + " depuis le site " + id + ".");
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public int getId() {
        return id;
    }
}
