package classes.cards.minions;

import classes.cards.Card;
import fileio.CardInput;

public final class TheRipper extends Card {

    public TheRipper(final CardInput cardInput) {
        super(cardInput);
    }

    public TheRipper(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "The Ripper" minion card:
         it decreases the attack damage of the enemy card by 2 units
         (if it becomes a negative value, it will be rounded to 0)

         @param card: the enemy card that is affected
     */
    @Override
    public void abilityOnCard(final Card card) {
        if (card.getAttackDamage() - 2 >= 0) {
            card.setAttackDamage(card.getAttackDamage() - 2);
        } else {
            card.setAttackDamage(0);
        }

        this.hasAttacked();
    }
}
