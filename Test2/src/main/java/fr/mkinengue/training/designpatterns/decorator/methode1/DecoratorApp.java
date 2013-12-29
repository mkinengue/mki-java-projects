package fr.mkinengue.training.designpatterns.decorator.methode1;

/**
 * An interface could have been used instead
 */
public class DecoratorApp {

    public static void main(String[] args) {
        ChristmasTree myTree = new ChristmasTree();

        // Add some decorations to the tree
        new BallDecorator(myTree);
        new StarDecorator(myTree);
        new RufflesDecorator(myTree);
    }
}
