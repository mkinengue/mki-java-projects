package fr.mkinengue.training.designpatterns.decorator.methode2;


public class TraitementDecorateur2 extends TraitementDecorateur {

    public TraitementDecorateur2() {
        super();
    }

    public TraitementDecorateur2(Traitement traitement) {
        super(traitement);
    }

    @Override
    public void operation() {
        // if (traitement != null) {
        // traitement.operation();
        // }
        super.operation();
        System.out.println("TraitementDecorateur2.Operation()");
    }
}
