package machine;

import util.Constantes;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * Cette classe implémente Runnable et permet la création facile d'un Thread
 * qui effectue la boucle principale de la tâche fournie.
 * Il va donc dormir un temps aléatoire, puis générer un nombre aléatoire qui
 * va décidier si le thread se termine ou si il génère une tache sur un site
 * voisin et recommence sa propre tâche.
 */
public class Tache implements Runnable {
    //Generateur de nombres aléatoires
    private static final Random GENERATEUR = new Random();
    //Le site ayant lancé la tâche
    private final Site parent;

    //Indique si le site est terminé ou non
    private Boolean fini;

    /**
     * Contructeur d'une Tache.
     * @param fini Boolean
     * @param parent Site
     */
    public Tache(Boolean fini, Site parent) {
        this.fini = fini;
        this.parent = parent;
    }

    /**
     * Boucle principale d'une Tache
     */
    public void run() {
        while(!fini){
            try {
                sleep(Constantes.MIN_TEMPS_CALCUL + GENERATEUR.nextInt(Constantes.MAX_TEMPS_CALCUL));
            } catch (InterruptedException e) {
                System.err.println("Un thread en plein calcul à été interrompu.");
            }
            if(!fini && GENERATEUR.nextDouble() < Constantes.PROBABILITY){
                int j;
                //Choisir un site aléatoire différent de ce site
                do{
                    j = GENERATEUR.nextInt(Constantes.NOMBRE_DE_SITES);
                } while(j == parent.getId());
                System.out.println("Lancement d'une tache sur le site " + j + ".");
                parent.lancerThreadSurSite(j);
            }else{
                System.out.println("Terminaison d'une tache sur le site " + parent.getId() + ".");
                if(parent.getTachesEnCours().decrementAndGet() == 0){
                    parent.envoyerJeton(parent.getId());
                }
                break;
            }
        }
    }

}
