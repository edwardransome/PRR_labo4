package machine;

import util.Constantes;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Communicateur implements Runnable {

    private final Site site;
    private final DatagramSocket socket;

    public Communicateur(Site site) {
        this.site = site;
        socket = site.getSocket();
    }

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
            if(paquet.getData()[0] == Constantes.NOUV_TACHE){
                System.out.println("Ce site a reçu une demande de lancement de tache!");
                site.lancerTache();
            }
        }
    }
}
