package classes.cards.minions;

import classes.cards.Card;
import fileio.CardInput;

public final class Disciple extends Card {

    public Disciple(final CardInput cardInput) {
        super(cardInput);
    }

    public Disciple(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "Disciple" minion card:
         it increases the health score of the ally card by 2 units

         @param card: the ally card that is affected
     */
    @Override
    public void abilityOnCard(final Card card) {
        card.setHealth(card.getHealth() + 2);
        this.hasAttacked();
    }
}
