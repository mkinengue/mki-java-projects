package fr.mkinengue.training.designpatterns.builder.pack;

import fr.mkinengue.training.designpatterns.builder.item.Item;


public class MealBox implements Packing {

    private Item[] items;

    public MealBox(Item... items) {
        if ((items != null) && (items.length != 0)) {
            this.items = new Item[items.length];
            int i = 0;
            for (Item it : items) {
                this.items[i] = it;
                i++;
            }
        }
    }

    public Item[] getItems() {
        return items;
    }
}
