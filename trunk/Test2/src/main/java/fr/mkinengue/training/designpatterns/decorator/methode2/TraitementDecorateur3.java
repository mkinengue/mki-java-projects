package fr.mkinengue.training.designpatterns.decorator.methode2;


public class TraitementDecorateur3 extends TraitementDecorateur {

    public TraitementDecorateur3() {
        super();
    }

    public TraitementDecorateur3(Traitement traitement) {
        super(traitement);
    }

    @Override
    public void operation() {
        // if (traitement != null) {
        // traitement.operation();
        // }
        super.operation();
        System.out.println("TraitementDecorateur3.Operation()");
    }
}
