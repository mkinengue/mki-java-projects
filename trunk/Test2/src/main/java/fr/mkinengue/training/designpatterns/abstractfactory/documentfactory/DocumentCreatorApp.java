package fr.mkinengue.training.designpatterns.abstractfactory.documentfactory;

import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Letter;
import fr.mkinengue.training.designpatterns.abstractfactory.documentfactory.document.Resume;

public class DocumentCreatorApp {

    public static void main(String[] args) {
        DocumentCreator fancyLetterCreator = new FancyDocumentCreator();
        DocumentCreator modernResumeCreator = new ModernDocumentCreator();

        Letter letterFromFancy = fancyLetterCreator.createLetter();
        Resume resumeFromModern = modernResumeCreator.createResume();

        System.out.println("letterFromFancy.type = " + letterFromFancy.getType());
        System.out.println("resumeFromModern.type = " + resumeFromModern.getType());
    }
}
