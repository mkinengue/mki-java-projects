package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document;


public class FancyResume extends Resume {

    public FancyResume() {
        super();
    }

    @Override
    public String getType() {
        return super.getType() + " - Fancy";
    }
}
