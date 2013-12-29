package fr.mkinengue.training.designpatterns.builder.item;

import fr.mkinengue.training.designpatterns.builder.pack.Packing;


public interface Item {

    /**
     * pack is the method, as every item will be packed in a different way. E.g.:- The burger will be packed as wrapped in a paper The cold
     * drink will be given in a glass The medium fries will be packed in a card box and The toy will be put in the bag straight. The class
     * Packing is an interface for different types of for different Items.
     */

    public Packing pack();

    /**
     * price is the method as all the items burger, cold drink, fries will have a price. The toy will not have any direct price, it will be
     * given free with the meal. The total price of the meal will be the combined price of the three items.
     * 
     * @return price, int in rupees.
     */

    public int price();

}
