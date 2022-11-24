package classes.gameInfo;

import classes.cards.minions.Miraj;
import classes.cards.minions.TheCursedOne;
import classes.cards.minions.TheRipper;
import classes.cards.Card;
import classes.cards.minions.Disciple;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

public final class GameTable {

    private final int noRows = 4;
    private final int noColumns = 5;

    private ArrayList<ArrayList<Card>> cardRows;

    public GameTable() {
        this.cardRows = new ArrayList<ArrayList<Card>>();

        int i;
        for (i = 1; i <= this.noRows; i++) {
            this.cardRows.add(new ArrayList<Card>());
        }
    }

    public ArrayList<ArrayList<Card>> getCardRows() {
        return cardRows;
    }

    public int getNoRows() {
        return noRows;
    }

    public int getNoColumns() {
        return noColumns;
    }

    /**
        The method removes all the cards from the current game table
        (when a new game is supposed to start)
     */
    public void clearGameTable() {
        int i;
        ArrayList<Card> tableRow;
        for (i = 0; i < this.noRows; i++) {
            tableRow = this.cardRows.get(i);
            while (!tableRow.isEmpty()) {
                tableRow.remove(0);
            }
        }
    }

    /**
        The method inserts a new card onto the specified row on the current
        game table by using a deep copy

        @param row: the chosen row for the card placement
        @param card: the card that should be added
     */
    public void addCard(final int row, final Card card) {
        if (card == null) {
            return;
        }

        ArrayList<Card> tableRow = this.cardRows.get(row);
        // checking if the card does not exceed the chosen row capacity
        if (tableRow.size() + 1 <= this.noColumns) {
            if (card.getName().compareTo("Goliath") == 0
                    || card.getName().compareTo("Warden") == 0) {
                tableRow.add(new Card(card));
            }
            if (card.getName().compareTo("Sentinel") == 0
                    || card.getName().compareTo("Berserker") == 0) {
                tableRow.add(new Card(card));
            }
            if (card.getName().compareTo("The Ripper") == 0) {
                tableRow.add(new TheRipper(card));
            }
            if (card.getName().compareTo("Miraj") == 0) {
                tableRow.add(new Miraj(card));
            }
            if (card.getName().compareTo("The Cursed One") == 0) {
                tableRow.add(new TheCursedOne(card));
            }
            if (card.getName().compareTo("Disciple") == 0) {
                tableRow.add(new Disciple(card));
            }
        }
    }

    /**
        The method removes the card that is found in a certain spot on the game table

        @param row: the row on which the card is found
        @param column: the index of the card on the specified row
     */
    public void deleteCard(final int row, final int column) {
        // checking if the given coordinates are valid; otherwise, nothing happens
        if ((row < 0 || row >= this.noRows) || (column < 0 || column >= this.noColumns)) {
            return;
        }

        ArrayList<Card> tableRow = this.cardRows.get(row);
        if (!tableRow.isEmpty() && column >= 0 && column < tableRow.size()) {
            tableRow.remove(column);
        }
    }

    /**
        The method removes all the cards from the given row that have been killed
        (their health score is negative or 0)

        @param row: the row that has been recently attacked
     */
    public void removeDefeatedCards(final int row) {
        // checking if the given coordinate is valid; otherwise, nothing happens
        if ((row < 0 || row >= this.noRows)) {
            return;
        }

        ArrayList<Card> tableRow = this.cardRows.get(row);

        int i = 0;
        while (i < tableRow.size()) {
            if (tableRow.get(i).getHealth() <= 0) {
                tableRow.remove(i);
            } else {
                i++;
            }
        }
    }

    /**
        The method searches for an existing card that should be found at the specified coordinates
        on the current game table

        @param row: the row on which the card is found
        @param column: the index of the card on the specified row

        @return card: if the coordinates are valid, the corresponding card is returned
     */
    public Card getCard(final int row, final int column) {
        // checking if the given coordinates are valid; otherwise, nothing happens
        if ((row < 0 || row >= this.noRows) || (column < 0 || column >= this.noColumns)) {
            return null;
        }

        ArrayList<Card> tableRow = this.cardRows.get(row);
        if (column > tableRow.size() - 1) {
            return null;
        }
        return tableRow.get(column);
    }

    /**
        The method indicates that all the cards from the chosen row can now be attacked
        (the turn during which they were supposed to be unreachable ended)

        @param row: the row on which the card is found
     */
    public void defrostRow(final int row) {
        // checking if the given coordinate is valid; otherwise, nothing happens
        if (row < 0 || row >= this.noRows) {
            return;
        }

        // checking if the given row has at least 1 card; otherwise, nothing happens
        ArrayList<Card> tableRow = this.cardRows.get(row);
        if (tableRow.isEmpty()) {
            return;
        }

        for (Card card : tableRow) {
            if (card.cardIsFrozen()) {
                card.defrost();
            }
        }
    }

    /**
        The method indicates that all the cards from the chosen row can now attack
        or use their ability
        (the turn during which they were supposed to be inactive ended)

        @param row: the row on which the card is found
     */
    public void restore(final int row) {
        // checking if the given coordinate is valid; otherwise, nothing happens
        if (row < 0 || row >= this.noRows) {
            return;
        }

        // checking if the given row has at least 1 card; otherwise, nothing happens
        ArrayList<Card> tableRow = this.cardRows.get(row);
        if (tableRow.isEmpty()) {
            return;
        }

        for (Card card : tableRow) {
            if (card.didAttack()) {
                card.hadNotAttacked();
            }
        }
    }

    /**
        The method checks the existence of tanks of the rows that the current player has
     */
    public boolean hasTank(final int playerIndex) {
        ArrayList<Card> tableRow;
        int i;
        if (playerIndex == 1) {
            for (i = 2; i <= this.noRows - 1; i++) {
                tableRow = this.cardRows.get(i);
                if (!tableRow.isEmpty()) {
                    for (Card card : tableRow) {
                        if (card.isTank()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        } else if (playerIndex == 2) {
            for (i = 0; i <= 1; i++) {
                tableRow = this.cardRows.get(i);
                if (!tableRow.isEmpty()) {
                    for (Card card : tableRow) {
                        if (card.isTank()) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        return false;
    }

    /**
        The method creates a JSON object mapper that contains the details about all the cards
        that can be found onto the current table

        @param objectMapper: the object that helps creating a new instance of the JSON mapper
        @return ObjectNode: the constructed object
     */
    public ObjectNode showCardsOnTable(final ObjectMapper objectMapper) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getCardsOnTable");

        ArrayNode outputTable = objectMapper.createArrayNode();

        int i;
        for (i = 0; i < this.noRows; i++) {
            ArrayList<Card> tableRow = this.cardRows.get(i);
            ArrayNode outputRow = objectMapper.createArrayNode();
            for (Card card : tableRow) {
                ObjectNode outputCard = objectMapper.createObjectNode();
                outputCard.put("mana", card.getMana());
                if (!card.isEnvironmentCard()) {
                    outputCard.put("attackDamage", card.getAttackDamage());
                    outputCard.put("health", card.getHealth());
                }
                outputCard.put("description", card.getDescription());

                ArrayNode outputColors = objectMapper.createArrayNode();
                for (String color : card.getColors()) {
                    outputColors.add(color);
                }

                outputCard.set("colors", outputColors);

                outputCard.put("name", card.getName());

                outputRow.add(outputCard);
            }
            outputTable.add(outputRow);
        }

        result.set("output", outputTable);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about all the card
         that is supposed to be found in a certain spot on the current table

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param row: the row on which the card is found
         @param column: the index of the card on the specified row
         @return ObjectNode: the constructed object
     */
    public ObjectNode showCard(final ObjectMapper objectMapper, final int row, final int column) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getCardAtPosition");
        result.put("x", row);
        result.put("y", column);

        if ((row < 0 || row >= this.noRows)) {
            result.put("output", "No card available at that position.");
            return result;
        }

        ArrayList<Card> tableRow = this.getCardRows().get(row);

        if (column < 0 || column >= tableRow.size()) {
            result.put("output", "No card available at that position.");
            return result;
        }

        Card card = this.getCard(row, column);

        if (card == null) {
            result.put("output", "No card available at that position.");
            return result;
        }

        ObjectNode outputCard = objectMapper.createObjectNode();

        outputCard.put("mana", card.getMana());
        outputCard.put("attackDamage", card.getAttackDamage());
        outputCard.put("health", card.getHealth());
        outputCard.put("description", card.getDescription());

        ArrayNode outputColors = objectMapper.createArrayNode();
        for (String color : card.getColors()) {
            outputColors.add(color);
        }

        outputCard.set("colors", outputColors);

        outputCard.put("name", card.getName());

        result.set("output", outputCard);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about all the
         frozen cards that can be found on the current table

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @return ObjectNode: the constructed object
     */
    public ObjectNode showFrozenCardsOnTable(final ObjectMapper objectMapper) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getFrozenCardsOnTable");

        ArrayNode outputTable = objectMapper.createArrayNode();

        int i;
        for (i = 0; i < this.noRows; i++) {
            ArrayList<Card> tableRow = this.cardRows.get(i);

            for (Card card : tableRow) {
                ObjectNode outputCard = null;

                if (card.cardIsFrozen()) {
                    outputCard = objectMapper.createObjectNode();
                    outputCard.put("mana", card.getMana());
                    outputCard.put("attackDamage", card.getAttackDamage());
                    outputCard.put("health", card.getHealth());
                    outputCard.put("description", card.getDescription());

                    ArrayNode outputColors = objectMapper.createArrayNode();
                    for (String color : card.getColors()) {
                        outputColors.add(color);
                    }

                    outputCard.set("colors", outputColors);

                    outputCard.put("name", card.getName());
                }

                if (outputCard != null) {
                    outputTable.add(outputCard);
                }
            }
        }

        result.set("output", outputTable);

        return result;
    }
}
