package fr.mkinengue.training.designpatterns.decorator.methode2;

/**
 * Main decorator class
 */
public abstract class TraitementDecorateur implements Traitement {

    protected Traitement traitement;

    public TraitementDecorateur() {
    }

    public TraitementDecorateur(Traitement traitement) {
        this.traitement = traitement;
    }

    public void operation() {
        if (traitement != null) {
            traitement.operation();
        }
        else
            System.out.println("traitement is null");
    }
}
