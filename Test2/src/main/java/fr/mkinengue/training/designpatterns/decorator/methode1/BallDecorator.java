package fr.mkinengue.training.designpatterns.decorator.methode1;


public class BallDecorator extends Decorator {

    public BallDecorator(ChristmasTree tree) {
        Branch branch = tree.getBranch();
        place(branch);
    }

    @Override
    public void place(Branch branch) {
        branch.put("ball");
    }

}
