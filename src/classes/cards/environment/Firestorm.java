package classes.cards.environment;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class Firestorm extends Card {

    public Firestorm(final CardInput cardInput) {
        super(cardInput);
    }

    public Firestorm(final Card card) {
        super(card);
    }

    /**
        The implementation of the "ability" command of the "Firestorm" environment card:
        it decreases the health of the entire attacked row by 1 unit

        @param table: the 4x5 matrix that holds the cards
        @param row: the enemy row of the game table that is affected
     */
    @Override
    public void abilityOnRow(final GameTable table, final int row) {
        ArrayList<Card> tableRow = table.getCardRows().get(row);

        if (!tableRow.isEmpty()) {
            for (Card card : tableRow) {
                card.setHealth(card.getHealth() - 1);
            }
        }
    }
}
