package fr.mkinengue.training.designpatterns.factory.produitfactory.methode1;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;
import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA1;


public class ProduitFactory1 extends ProduitFactory {

    @Override
    protected ProduitA createProduitA() {
        return new ProduitA1();
    }

}
