package fr.mkinengue.training.designpatterns.decorator.methode2;

/**
 * Main decorator class
 */
public class TraitementDecorateurApp {

    public static void main(String[] args) {
        System.out.println("traitement 1 2 3");
        Traitement traitement123 = new TraitementDecorateur3(new TraitementDecorateur2(new TraitementDecorateur1()));
        traitement123.operation();

        System.out.println("traitement 1 3");
        Traitement traitement13 = new TraitementDecorateur3(new TraitementDecorateur1());
        traitement13.operation();
    }
}
