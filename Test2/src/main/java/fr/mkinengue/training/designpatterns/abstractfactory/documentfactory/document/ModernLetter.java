package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document;


public class ModernLetter extends Letter {

    public ModernLetter() {
        super();
    }

    @Override
    public String getType() {
        return super.getType() + " - Modern";
    }
}
