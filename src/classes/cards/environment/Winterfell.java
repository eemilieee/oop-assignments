package classes.cards.environment;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class Winterfell extends Card {

    public Winterfell(final CardInput cardInput) {
        super(cardInput);
    }

    public Winterfell(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "Winterfell" environment card:
         it freezes the entire enemy row for the current turn

         @param table: the 4x5 matrix that holds the cards
         @param row: the enemy row of the game table that is affected
     */
    @Override
    public void abilityOnRow(final GameTable table, final int row) {
        ArrayList<Card> tableRow = table.getCardRows().get(row);

        if (!tableRow.isEmpty()) {
            for (Card card : tableRow) {
                card.freeze();
            }
        }
    }
}
