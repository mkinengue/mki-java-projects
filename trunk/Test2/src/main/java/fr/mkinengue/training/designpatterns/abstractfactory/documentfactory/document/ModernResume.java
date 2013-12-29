package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document;


public class ModernResume extends Resume {

    public ModernResume() {
        super();
    }

    @Override
    public String getType() {
        return super.getType() + " - Modern";
    }
}
