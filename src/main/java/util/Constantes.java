package util;

import java.nio.ByteBuffer;

/**
 * Interface contenant les constantes de l'application permettant de les
 * modifier facilement.
 */
public interface Constantes {
    //Les ports utilisés par les sites 0-3
    int[] PORTS = {6060, 6061, 6062, 6063};

    //Les adresses IP utilisées par les sites 0-3
    String[] ADRESSES_IP = {"127.0.0.1", "127.0.0.1", "127.0.0.1", "127.0.0.1"};

    //Le nombre total de sites dans la topologie
    int NOMBRE_DE_SITES = ADRESSES_IP.length;

    //Probabilité p de l'algorithme de terminaison
    double PROBABILITY = 0.2;

    //Temps minimum et maximum que prends un calcul dans une tache en ms
    int MIN_TEMPS_CALCUL = 1000;
    int MAX_TEMPS_CALCUL = 10000;

    //Types de message
    byte NOUV_TACHE = (byte) 0;
    byte JETON = (byte) 1;
    byte FIN = (byte) 2;


    //Tailles des types de message
    //Un byte pour le type, un byte pour l'id du site destinataire
    int TAILLE_TAMPON_NOUV_TACHE = Byte.BYTES + Byte.BYTES;
    //Un byte pour le type, un byte pour l'id du site emetteur
    int TAILLE_TAMPON_JETON = Byte.BYTES + Byte.BYTES;
    //Un byte pour le type, un byte pour l'id du site emetteur
    int TAILLE_TAMPON_FIN = Byte.BYTES + Byte.BYTES;
}
