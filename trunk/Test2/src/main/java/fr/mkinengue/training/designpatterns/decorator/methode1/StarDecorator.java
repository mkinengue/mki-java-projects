package fr.mkinengue.training.designpatterns.decorator.methode1;


public class StarDecorator extends Decorator {

    public StarDecorator(ChristmasTree tree) {
        Branch branch = tree.getBranch();
        place(branch);
    }

    @Override
    public void place(Branch branch) {
        branch.put("star");
    }

}
