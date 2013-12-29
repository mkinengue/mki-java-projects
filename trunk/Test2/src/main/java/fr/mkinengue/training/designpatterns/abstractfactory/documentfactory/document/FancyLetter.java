package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document;


public class FancyLetter extends Letter {

    public FancyLetter() {
        super();
    }

    @Override
    public String getType() {
        return super.getType() + " - Fancy";
    }
}
