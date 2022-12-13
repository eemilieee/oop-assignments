package classes.cards.heroes;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class KingMudface extends Card {
    private static final int DEFAULT_HEALTH = 30;

    public KingMudface(final CardInput cardInput) {
        super(cardInput);
        this.setHealth(DEFAULT_HEALTH);
    }

    public KingMudface(final Card card) {
        super(card);
        this.setHealth(DEFAULT_HEALTH);
    }

    /**
         The implementation of the "ability" command of the "King Mudface" hero card:
         it increases the health score of each card of the ally row by 1 unit

         @param table: the 4x5 matrix that holds the cards
         @param row: the ally row of the game table that is affected
     */
    @Override
    public void abilityOnRow(final GameTable table, final int row) {
        ArrayList<Card> tableRow = table.getCardRows().get(row);

        if (!tableRow.isEmpty()) {
            for (Card card : tableRow) {
                card.setHealth(card.getHealth() + 1);
            }
        }
        this.hasAttacked();
    }
}
