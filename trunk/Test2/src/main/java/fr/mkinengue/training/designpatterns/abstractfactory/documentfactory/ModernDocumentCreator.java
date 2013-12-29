package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory;

import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Letter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.ModernLetter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.ModernResume;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Resume;


public class ModernDocumentCreator extends DocumentCreator {

    @Override
    Letter createLetter() {
        return new ModernLetter();
    }

    @Override
    Resume createResume() {
        return new ModernResume();
    }

}
