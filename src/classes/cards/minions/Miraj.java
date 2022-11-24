package classes.cards.minions;

import classes.cards.Card;
import fileio.CardInput;

public final class Miraj extends Card {

    public Miraj(final CardInput cardInput) {
        super(cardInput);
    }

    public Miraj(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "Miraj" minion card:
         it swaps the health score of the attacker and the attacked card

         @param card: the enemy card that is affected
     */
    @Override
    public void abilityOnCard(final Card card) {
        int aux = this.getHealth();
        this.setHealth(card.getHealth());
        card.setHealth(aux);
        this.hasAttacked();
    }
}
