package classes.cards.heroes;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class EmpressThorina extends Card {

    private static final int DEFAULT_HEALTH = 30;

    public EmpressThorina(final CardInput cardInput) {
        super(cardInput);
        this.setHealth(DEFAULT_HEALTH);
    }

    public EmpressThorina(final Card card) {
        super(card);
        this.setHealth(DEFAULT_HEALTH);
    }

    /**
         The implementation of the "ability" command of the "Empress Thorina" hero card:
         it removes the card with the highest health score from the enemy row

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
                if (card.getHealth() < tableRow.get(i).getHealth()) {
                    card = tableRow.get(i);
                }
            }

            tableRow.remove(card);
            this.hasAttacked();
        }
    }
}
