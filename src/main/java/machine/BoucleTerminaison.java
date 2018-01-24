package machine;

import util.Constantes;

import java.util.Random;

import static java.lang.Thread.sleep;

public class BoucleTerminaison implements Runnable {
    private static final Random GENERATEUR = new Random();
    private final Site parent;

    Boolean fini;

    public BoucleTerminaison(Boolean fini, Site parent) {
        this.fini = fini;
        this.parent = parent;
    }

    public void run() {
        while(!fini){
            try {
                sleep(Constantes.MIN_TEMPS_CALCUL + GENERATEUR.nextInt(Constantes.MAX_TEMPS_CALCUL));
            } catch (InterruptedException e) {
                System.err.println("Un thread en plein calcul à été interrompu.");
            }
            if(GENERATEUR.nextDouble() < Constantes.PROBABILITY){
                int j;
                //Choisir un site aléatoire différent de ce site
                do{
                    j = GENERATEUR.nextInt(Constantes.NOMBRE_DE_SITES);
                } while(j == parent.getId());
                System.out.println("Lancement d'une tache sur le site " + j + ".");
                parent.lancerThreadSurSite(j);
            }else{
                System.out.println("Terminaison d'une tache sur le site " + parent.getId() + ".");
                break;
            }
        }
    }

}
