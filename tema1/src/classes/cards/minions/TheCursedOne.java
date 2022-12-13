package classes.cards.minions;

import classes.cards.Card;
import fileio.CardInput;

public final class TheCursedOne extends Card {

    public TheCursedOne(final CardInput cardInput) {
        super(cardInput);
    }

    public TheCursedOne(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "The Cursed One" minion card:
         it swaps the attack damage and the health socre of the attacked card

         @param card: the enemy card that is affected
     */
    @Override
    public void abilityOnCard(final Card card) {
        int aux = card.getAttackDamage();
        card.setAttackDamage(card.getHealth());
        card.setHealth(aux);
        this.hasAttacked();
    }
}
