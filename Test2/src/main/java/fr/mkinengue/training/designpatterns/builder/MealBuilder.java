package fr.mkinengue.training.designpatterns.builder;

import fr.mkinengue.training.designpatterns.builder.item.Item;
import fr.mkinengue.training.designpatterns.builder.item.burger.BurgerItem;
import fr.mkinengue.training.designpatterns.builder.item.drink.DrinkItem;
import fr.mkinengue.training.designpatterns.builder.item.fries.FriesItem;
import fr.mkinengue.training.designpatterns.builder.item.toy.ToyItem;
import fr.mkinengue.training.designpatterns.builder.pack.MealBox;


public class MealBuilder {

    private final MealBox box;

    public MealBuilder(BurgerItem burger, FriesItem fries, DrinkItem drink, ToyItem toy) {
        box = new MealBox(burger, fries, drink, toy);
    }

    public int calculatePrice() {
        int price = 0;
        for (Item item : box.getItems()) {
            price += item.price();
        }
        return price;
    }
}
