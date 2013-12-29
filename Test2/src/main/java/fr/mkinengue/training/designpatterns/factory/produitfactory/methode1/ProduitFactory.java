package fr.mkinengue.training.designpatterns.factory.produitfactory.methode1;

import fr.mkinengue.training.designpatterns.factory.produitfactory.produit.ProduitA;

/**
 * Abstract Factory superclass. Instead, an interface could have been used
 */
public abstract class ProduitFactory {

    public ProduitA getProduitA() {
        return createProduitA();
    }

    protected abstract ProduitA createProduitA();
}
