package classes.cards.heroes;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class LordRoyce extends Card {
    private static final int DEFAULT_HEALTH = 30;

    public LordRoyce(final CardInput cardInput) {
        super(cardInput);
        this.setHealth(DEFAULT_HEALTH);
    }

    public LordRoyce(final Card card) {
        super(card);
        this.setHealth(DEFAULT_HEALTH);
    }

    /**
         The implementation of the "ability" command of the "Lord Royce" hero card:
         it freezes the card with the highest attack damage from the enemy row
         for the current turn

         @param table: the 4x5 matrix that holds the cards
         @param row: the enemy row of the game table that is affected
     */
    @Override
    public void abilityOnRow(final GameTable table, final int row) {
        int i;
        ArrayList<Card> tableRow = table.getCardRows().get(row);

        if (!tableRow.isEmpty()) {
            Card card = tableRow.get(0);
            for (i = 1; i < tableRow.size(); i++) {
                if (card.getAttackDamage() < tableRow.get(i).getAttackDamage()) {
                    card = tableRow.get(i);
                }
            }
            card.freeze();
            this.hasAttacked();
        }
    }
}
