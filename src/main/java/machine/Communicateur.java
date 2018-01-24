package machine;

import util.Constantes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * Classe permettant la reception de messages par un site.
 */
public class Communicateur implements Runnable {
    //Le site de ce communicateur
    private final Site site;
    //Le socket permettant la réception de messages
    private final DatagramSocket socket;

    /**
     * Constructeur d'un communicateur prenant en paramètre un site
     * @param site
     */
    public Communicateur(Site site) {
        this.site = site;
        socket = site.getSocket();
    }

    /**
     * Méthode run() permettant de créer un thread à partir de cette classe.
     * Cette méthode attend des paquets et les traite en fonction du type.
     */
    @Override
    public void run() {
        while(!socket.isClosed()){
            byte[] tampon = new byte[Constantes.TAILLE_TAMPON_NOUV_TACHE];
            DatagramPacket paquet = new DatagramPacket(tampon, tampon.length);
            try {
                socket.receive(paquet);
            } catch (IOException e) {
                System.err.println("Erreur de reception de paquet.");
                continue;
            }
            byte typeMessage = paquet.getData()[0];
            int idSiteEmetteur = paquet.getData()[1];
            if(typeMessage == Constantes.NOUV_TACHE){
                int idSiteDestinataire = paquet.getData()[1];
                if(idSiteDestinataire == site.getId()){
                    System.out.println("Ce site a reçu une demande de lancement de tache!");
                    site.lancerTache();
                } else {
                    //Le message n'est pas pour nous, le renvoyer plus loin
                    site.lancerThreadSurSite(idSiteDestinataire);
                }
            } else if (typeMessage == Constantes.JETON){
                if(site.getId() == idSiteEmetteur){
                    //On a recu notre propre jeton
                    if(site.getResteInactif()){
                        //On est resté inactif depuis l'envoi du jeton
                        site.setFini(true);
                        site.envoyerFin(site.getId());
                    } else {
                        if(site.getTachesEnCours().intValue() == 0){
                            site.envoyerJeton(site.getId());
                        }
                    }
                }else{

                }
            } else { //C'est un message END
                if(idSiteEmetteur != site.getId()){
                    site.envoyerFin(idSiteEmetteur);
                }
            }
        }
    }
}
