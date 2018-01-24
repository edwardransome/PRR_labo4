/*
 -------------------------------------------------------------------------------
 Laboratoire : Laboratoire 4 - PRR
 Fichier     : Main.java
 Auteur(s)   : Michael Spierer & Edward Ransome
 Date        : 24.01.2017

Dans ce laboratoire, il était question d'implémenter l'algorithme de terminaison
sur un anneau en implémentant comme "tache" d'un site un calcul simulé par
un sleep de durée aléatoire. De plus, un utilisateur peut choisir a n'importe
quel moment de lancer une tache ou de terminer le site.

Les quatres sites doivent êtres lancés en leur passant chacun une ID différente
de 0 à 3. Toutes les constantes sont configurables, mais nous avons choisi
arbitrairemnt une probabilité p égale à 0.2 de relancer deux taches (une
localement et une sur un autre site) lors de la terminaison d'une tache. Le
temps de calcul simulé est entre 1 et 10 secondes.

La donnée spécifie qu'un site terminé ne peut plus lancer de taches. Une tache
en cours lors d'une terminaison (si l'utilisateur termine manuellement)
sera terminée normalement, mais n'effectuera pas de relancement de tache sur un
 autre site.

 -------------------------------------------------------------------------------
 */


import machine.Site;
import util.Constantes;

/**
 * Fonction amorce qui initialise un Site. Nécessite un paramètre ID en comme
 * argument de ligne de commande, échoue sinon.
 */
public class Main {
    public static void main(String[] args) {
        if(args.length != 1){
            System.err.println("Passez l'id du site à démarrer comme unique paramètre");
        }else{
            int id = Integer.parseInt(args[0]);
            Site site = new Site(id, Constantes.PORTS[id]);
        }

    }
}
