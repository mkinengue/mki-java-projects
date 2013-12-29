package fr.mkinengue.training.designpatterns.factory.produitfactory.methode1;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;
import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA2;


public class ProduitFactory2 extends ProduitFactory {

    @Override
    protected ProduitA createProduitA() {
        return new ProduitA2();
    }

}
