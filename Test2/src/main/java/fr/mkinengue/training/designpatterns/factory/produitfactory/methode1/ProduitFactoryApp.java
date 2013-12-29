package fr.mkinengue.training.designpatterns.factory.produitfactory.methode1;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;

public class ProduitFactoryApp {

    public static void main(String[] args) {
        ProduitFactory produitFactory1 = new ProduitFactory1();
        ProduitFactory produitFactory2 = new ProduitFactory2();
        ProduitA produitA = null;

        System.out.println("Utilisation de la premiere fabrique");
        produitA = produitFactory1.getProduitA();
        produitA.methodeA();

        System.out.println();

        System.out.println("Utilisation de la seconde fabrique");
        produitA = produitFactory2.getProduitA();
        produitA.methodeA();
    }
}
