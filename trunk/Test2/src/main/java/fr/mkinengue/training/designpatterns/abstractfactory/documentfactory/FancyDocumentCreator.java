package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory;

import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.FancyLetter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.FancyResume;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Letter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Resume;


public class FancyDocumentCreator extends DocumentCreator {

    @Override
    Letter createLetter() {
        return new FancyLetter();
    }

    @Override
    Resume createResume() {
        return new FancyResume();
    }
}
