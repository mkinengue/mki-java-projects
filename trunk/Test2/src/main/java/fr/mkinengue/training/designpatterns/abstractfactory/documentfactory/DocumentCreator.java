package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory;

import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Letter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Resume;

/**
 * Abstract Factory superclass. Instead, an interface could have been used
 */
public abstract class DocumentCreator {

    abstract Letter createLetter();

    abstract Resume createResume();
}
