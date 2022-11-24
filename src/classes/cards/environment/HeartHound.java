package classes.cards.environment;

import classes.cards.*;
import classes.cards.minions.Disciple;
import classes.cards.minions.Miraj;
import classes.cards.minions.TheCursedOne;
import classes.cards.minions.TheRipper;
import classes.gameInfo.GameTable;
import fileio.CardInput;

import java.util.ArrayList;

public final class HeartHound extends Card {

    public HeartHound(final CardInput cardInput) {
        super(cardInput);
    }

    public HeartHound(final Card card) {
        super(card);
    }

    /**
         The implementation of the "ability" command of the "Heart Hound" environment card:
         it steals the card with the highest health score from the enemy row and places
         it onto the correct row of the player who attacks

         @param table: the 4x5 matrix that holds the cards
         @param row: the enemy row of the game table that is affected
     */
    @Override
    public void abilityOnRow(final GameTable table, final int row) {
        int newRow = table.getNoRows() - 1 - row, i;
        ArrayList<Card> tableRow = table.getCardRows().get(row);
        ArrayList<Card> newTableRow = table.getCardRows().get(newRow);

        if (newTableRow.size() < table.getNoColumns() && !tableRow.isEmpty()) {
            Card card = tableRow.get(0);
            for (i = 1; i < tableRow.size(); i++) {
                if (card.getHealth() < tableRow.get(i).getHealth()) {
                    card = tableRow.get(i);
                }
            }

            // adding the card with the highest health score onto the game table
            // by creating a deep copy of it
            if (card.getName().compareTo("Goliath") == 0
                    || card.getName().compareTo("Warden") == 0) {
                newTableRow.add(new Card(card));
            }
            if (card.getName().compareTo("Sentinel") == 0
                    || card.getName().compareTo("Berserker") == 0) {
                newTableRow.add(new Card(card));
            }
            if (card.getName().compareTo("The Ripper") == 0) {
                newTableRow.add(new TheRipper(card));
            }
            if (card.getName().compareTo("Miraj") == 0) {
                newTableRow.add(new Miraj(card));
            }
            if (card.getName().compareTo("The Cursed One") == 0) {
                newTableRow.add(new TheCursedOne(card));
            }
            if (card.getName().compareTo("Disciple") == 0) {
                newTableRow.add(new Disciple(card));
            }
            // removing the stolen card from the enemy row
            tableRow.remove(card);
        }
    }
}
