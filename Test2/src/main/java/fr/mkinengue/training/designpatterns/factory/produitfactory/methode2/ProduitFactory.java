package fr.mkinengue.training.designpatterns.factory.produitfactory.methode2;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;
import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA1;
import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA2;


public class ProduitFactory {

    public static final int TYPE_PRODUITA1 = 1;
    public static final int TYPE_PRODUITA2 = 2;

    public ProduitA getProduitA(int typeProduit) {
        ProduitA produitA = null;

        switch (typeProduit) {
            case TYPE_PRODUITA1:
                produitA = new ProduitA1();
                break;
            case TYPE_PRODUITA2:
                produitA = new ProduitA2();
                break;
            default:
                throw new IllegalArgumentException("Type de produit inconnu");
        }

        return produitA;
    }
}
