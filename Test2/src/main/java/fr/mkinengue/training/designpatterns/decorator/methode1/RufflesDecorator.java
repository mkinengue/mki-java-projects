package fr.mkinengue.training.designpatterns.decorator.methode1;


public class RufflesDecorator extends Decorator {

    public RufflesDecorator(ChristmasTree tree) {
        Branch branch = tree.getBranch();
        place(branch);
    }

    @Override
    public void place(Branch branch) {
        branch.put("ruffles");
    }
}
