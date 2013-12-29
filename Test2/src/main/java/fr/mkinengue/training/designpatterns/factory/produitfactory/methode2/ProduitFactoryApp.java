package fr.mkinengue.training.designpatterns.factory.produitfactory.methode2;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;

public class ProduitFactoryApp {

    public static void main(String[] args) {
        ProduitFactory produitFactory = new ProduitFactory();

        ProduitA produitA = null;

        produitA = produitFactory.getProduitA(ProduitFactory.TYPE_PRODUITA1);
        produitA.methodeA();

        produitA = produitFactory.getProduitA(ProduitFactory.TYPE_PRODUITA2);
        produitA.methodeA();

        produitA = produitFactory.getProduitA(3);
        produitA.methodeA();
    }
}
