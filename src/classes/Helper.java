package classes;

import classes.cards.Card;
import classes.gameInfo.GameTable;
import classes.gameInfo.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.ActionsInput;
import fileio.Coordinates;

import java.util.ArrayList;

public final class Helper {

    private Helper() {
    }

    /**
        The method implements all the "debug" commands that show the details about the game

        @param objectMapper: the object that helps creating a new instance of a JSON mapper
        @param output: the array of JSON mappers that represents the final output of the program
        @param table: the 4x5 matrix that hold the cards
        @param player1: the first player that takes part in games
        @param player2: the second player that takes part in games
        @param currentPlayerIndex: the player whose turn is currently taking place
        @param actionsIndex: the number of the action that is being fulfilled
        @param actions: the list of actions of the current game
     */
    public static void debugCommands(final ObjectMapper objectMapper, final ArrayNode output,
         final GameTable table, final Player player1, final Player player2,
             final int currentPlayerIndex, final int actionsIndex,
                 final ArrayList<ActionsInput> actions) {

        Player wantedPlayer, currentPlayer;
        int wantedPlayerIndex = actions.get(actionsIndex).getPlayerIdx();
        String command = actions.get(actionsIndex).getCommand();

        if (wantedPlayerIndex != 0) {
            if (wantedPlayerIndex == 1) {
                wantedPlayer = player1;
            } else {
                wantedPlayer = player2;
            }

            if (command.compareTo("getCardsInHand") == 0) {
                output.add(wantedPlayer.showCardsInHand(objectMapper, wantedPlayerIndex));
            }
            if (command.compareTo("getPlayerDeck") == 0) {
                output.add(wantedPlayer.showCardsInDeck(objectMapper, wantedPlayerIndex));
            }
            if (command.compareTo("getPlayerHero") == 0) {
                output.add(wantedPlayer.showHero(objectMapper, wantedPlayerIndex));
            }
            if (command.compareTo("getPlayerMana") == 0) {
                output.add(wantedPlayer.showTotalMana(objectMapper, wantedPlayerIndex));
            }
            if (command.compareTo("getEnvironmentCardsInHand") == 0) {
                output.add(wantedPlayer.showEnvironmentCardsInHand(objectMapper,
                                                                        wantedPlayerIndex));
            }
        }

        if (wantedPlayerIndex == 1) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }

        if (command.compareTo("getCardAtPosition") == 0) {
            int row, column;
            row = actions.get(actionsIndex).getX();
            column = actions.get(actionsIndex).getY();
            output.add(table.showCard(objectMapper, row, column));
        }

        if (command.compareTo("getFrozenCardsOnTable") == 0) {
            output.add(table.showFrozenCardsOnTable(objectMapper));
        }

        if (command.compareTo("getCardsOnTable") == 0) {
            output.add(table.showCardsOnTable(objectMapper));
        }

        if (command.compareTo("getPlayerTurn") == 0) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("output", currentPlayerIndex);
            output.add(result);
        }

        if (command.compareTo("getTotalGamesPlayed") == 0) {
            output.add(currentPlayer.showTotalGamesPlayed(objectMapper));
        }

        if (command.compareTo("getPlayerOneWins") == 0) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("output", player1.getTotalWins());
            output.add(result);
        }

        if (command.compareTo("getPlayerTwoWins") == 0) {
            ObjectNode result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("output", player2.getTotalWins());
            output.add(result);
        }
    }

    /**
         The method implements the "useAttackHero" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param player1: the first player that takes part in games
         @param player2: the second player that takes part in games
         @param currentPlayerIndex: the player whose turn is currently taking place
         @param endGame: the old value of the variable that indicates whether the game should
         end or not (one of the heroes was defeated)
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
         @return endGame: the new value of the variable that indicates whether the game should
         end or not (one of the heroes was defeated)
     */
    public static int useAttackHero(final ObjectMapper objectMapper, final ArrayNode output,
        final GameTable table, final Player player1, final Player player2,
            final int currentPlayerIndex, int endGame, final int actionsIndex,
                final ArrayList<ActionsInput> actions) {

        ObjectNode result = null;
        Card attacker, attacked;
        Coordinates attackerCoords;
        int enemyPlayer = table.getNoRows() - 1 - currentPlayerIndex;

        String command = actions.get(actionsIndex).getCommand();

        attackerCoords = actions.get(actionsIndex).getCardAttacker();

        attacker = table.getCard(attackerCoords.getX(),
                                                        attackerCoords.getY());
        if (attacker == null) {
            return -1;
        }

        if (enemyPlayer == 1) {
            attacked = player1.getHero();
        } else {
            attacked = player2.getHero();
        }

        if (attacker.cardIsFrozen()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.put("error", "Attacker card is frozen.");
        } else if (attacker.didAttack()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.put("error", "Attacker card has already attacked this turn.");
        } else if (table.hasTank(enemyPlayer)) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.put("error", "Attacked card is not of type 'Tank'.");
        } else {
            attacker.attack(attacked);
            if (attacked.getHealth() <= 0) {
                result = objectMapper.createObjectNode();
                if (currentPlayerIndex == 1) {
                    player1.hasWonAnotherGame();
                    result.put("gameEnded", "Player one killed the enemy hero.");
                } else {
                    player2.hasWonAnotherGame();
                    result.put("gameEnded", "Player two killed the enemy hero.");
                }
                endGame = 1;
            }
        }

        if (result != null) {
            output.add(result);
        }
        return endGame;
    }

    /**
         The method implements the "useHeroAbility" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param currentPlayer: the player who is now attacking
         @param currentPlayerIndex: indicates what player is currently taking part
         in this turn
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
     */
    public static void useHeroAbility(final ObjectMapper objectMapper, final ArrayNode output,
          final GameTable table, final Player currentPlayer, final int currentPlayerIndex,
              final int actionsIndex, final ArrayList<ActionsInput> actions) {

        ObjectNode result = null;
        Card attacker = currentPlayer.getHero();
        int affectedRow = actions.get(actionsIndex).getAffectedRow();
        int enemyPlayer = table.getNoRows() - 1 - currentPlayerIndex;

        String command = actions.get(actionsIndex).getCommand();

        if (currentPlayer.getTotalMana() < attacker.getMana()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("affectedRow", affectedRow);
            result.put("error", "Not enough mana to use hero's ability.");
        } else if (attacker.didAttack()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("affectedRow", affectedRow);
            result.put("error", "Hero has already attacked this turn.");
        } else if ((attacker.getName().compareTo("Lord Royce") == 0
                            || attacker.getName().compareTo("Empress Thorina") == 0)
                                && !attacker.isEnemy(enemyPlayer, affectedRow)) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("affectedRow", affectedRow);
            result.put("error", "Selected row does not belong to the enemy.");
        } else if ((attacker.getName().compareTo("General Kocioraw") == 0
                            || attacker.getName().compareTo("King Mudface") == 0)
                                && !attacker.isAlly(enemyPlayer, affectedRow)) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("affectedRow", affectedRow);
            result.put("error", "Selected row does not belong to the current player.");
        } else {
            currentPlayer.useMana(attacker.getMana());
            attacker.abilityOnRow(table, affectedRow);
            table.removeDefeatedCards(affectedRow);
        }

        if (result != null) {
            output.add(result);
        }
    }

    /**
         The method implements the "cardUsesAbility" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param currentPlayerIndex: indicates what player is currently taking part
         in this turn
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
         */
    public static void cardUsesAbility(final ObjectMapper objectMapper, final ArrayNode output,
           final GameTable table, final int currentPlayerIndex, final int actionsIndex,
               final ArrayList<ActionsInput> actions) {

        ObjectNode result = null;
        Card attacker, attacked;
        Coordinates attackerCoords, attackedCoords;
        int enemyPlayer = table.getNoRows() - 1 - currentPlayerIndex;

        String command = actions.get(actionsIndex).getCommand();

        attackerCoords = actions.get(actionsIndex).getCardAttacker();
        attackedCoords = actions.get(actionsIndex).getCardAttacked();

        attacker = table.getCard(attackerCoords.getX(), attackerCoords.getY());
        attacked = table.getCard(attackedCoords.getX(), attackedCoords.getY());

        if (attacker == null || attacked == null) {
            return;
        }

        if (attacker.cardIsFrozen()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacker card is frozen.");
        } else if (attacker.didAttack()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacker card has already attacked this turn.");
        } else if (attacker.getName().compareTo("Disciple") == 0
                                    && !attacked.isAlly(enemyPlayer, attackedCoords.getX())) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacked card does not belong to the current player.");
        } else if ((attacker.getName().compareTo("The Ripper") == 0
                        || attacker.getName().compareTo("Miraj") == 0
                            || attacker.getName().compareTo("The Cursed One") == 0)
                                && !attacked.isEnemy(enemyPlayer, attackedCoords.getX())) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacked card does not belong to the enemy.");
        } else if ((attacker.getName().compareTo("The Ripper") == 0
                        || attacker.getName().compareTo("Miraj") == 0
                            || attacker.getName().compareTo("The Cursed One") == 0)
                                && table.hasTank(enemyPlayer) && !attacked.isTank()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacked card is not of type 'Tank'.");
        } else {
            attacker.abilityOnCard(attacked);
            if (attacked.getHealth() <= 0) {
                table.deleteCard(attackedCoords.getX(), attackedCoords.getY());
            }
        }
        if (result != null) {
            output.add(result);
        }
    }

    /**
         The method implements the "cardUsesAtack" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param currentPlayerIndex: indicates what player is currently taking part
         in this turn
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
     */
    public static void cardUsesAttack(final ObjectMapper objectMapper, final ArrayNode output,
          final GameTable table, final int currentPlayerIndex, final int actionsIndex,
              final ArrayList<ActionsInput> actions) {

        ObjectNode result = null;
        Card attacker, attacked;
        Coordinates attackerCoords, attackedCoords;
        int enemyPlayer = table.getNoRows() - 1 - currentPlayerIndex;

        String command = actions.get(actionsIndex).getCommand();

        attackerCoords = actions.get(actionsIndex).getCardAttacker();
        attackedCoords = actions.get(actionsIndex).getCardAttacked();

        attacker = table.getCard(attackerCoords.getX(), attackerCoords.getY());
        attacked = table.getCard(attackedCoords.getX(), attackedCoords.getY());

        if (attacker == null || attacked == null) {
            return;
        }

        if (!attacked.isEnemy(enemyPlayer, attackedCoords.getX())) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacked card does not belong to the enemy.");
        } else if (attacker.didAttack()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacker card has already attacked this turn.");
        } else if (attacker.cardIsFrozen()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacker card is frozen.");
        } else if (table.hasTank(enemyPlayer) && !attacked.isTank()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.putObject("cardAttacker").put("x",
                                        attackerCoords.getX()).put("y", attackerCoords.getY());
            result.putObject("cardAttacked").put("x",
                                        attackedCoords.getX()).put("y", attackedCoords.getY());
            result.put("error", "Attacked card is not of type 'Tank'.");
        } else {
            attacker.attack(attacked);
            if (attacked.getHealth() <= 0) {
                table.deleteCard(attackedCoords.getX(), attackedCoords.getY());
            }
        }

        if (result != null) {
            output.add(result);
        }
    }

    /**
         The method implements the "useEnvironmentCard" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param currentPlayer: the player who is now attacking
         @param currentPlayerIndex: indicates what player is currently taking part
         in this turn
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
     */
    public static void useEnvironmentCard(final ObjectMapper objectMapper, final ArrayNode output,
          final GameTable table, final Player currentPlayer, final int currentPlayerIndex,
              final int actionsIndex, final ArrayList<ActionsInput> actions) {

        int handIndex = actions.get(actionsIndex).getHandIdx();
        int affectedRow = actions.get(actionsIndex).getAffectedRow();
        int enemyPlayer = table.getNoRows() - 1 - currentPlayerIndex;
        ObjectNode result = null;

        String command = actions.get(actionsIndex).getCommand();

        Card attacker = currentPlayer.getHand().get(handIndex);

        if (attacker == null) {
            return;
        }

        if (!attacker.isEnvironmentCard()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("affectedRow", affectedRow);
            result.put("error", "Chosen card is not of type environment.");
        } else if (currentPlayer.getTotalMana() < attacker.getMana()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("affectedRow", affectedRow);
            result.put("error", "Not enough mana to use environment card.");
        } else if (attacker.isAlly(enemyPlayer, affectedRow)) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("affectedRow", affectedRow);
            result.put("error", "Chosen row does not belong to the enemy.");
        } else if (attacker.getName().compareTo("Heart Hound") == 0
                && table.getCardRows().get(table.getNoRows() - 1 - affectedRow).size() + 1
                                                                        > table.getNoColumns()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("affectedRow", affectedRow);
            result.put("error", "Cannot steal enemy card since the player's row is full.");
        } else {
            currentPlayer.useMana(attacker.getMana());
            attacker.abilityOnRow(table, affectedRow);
            table.removeDefeatedCards(affectedRow);
            currentPlayer.removeCardFromHand(handIndex);
        }
        if (result != null) {
            output.add(result);
        }
    }

    /**
         The method implements the "placeCard" command

         @param objectMapper: the object that helps creating a new instance of a JSON mapper
         @param output: the array of JSON mappers that represents the final output of the program
         @param table: the 4x5 matrix that hold the cards
         @param currentPlayer: the player who is now attacking
         @param actionsIndex: the number of the action that is being fulfilled
         @param actions: the list of actions of the current game
     */
    public static void placeCard(final ObjectMapper objectMapper, final ArrayNode output,
        final GameTable table, final Player currentPlayer, final int actionsIndex,
            final int backRow, final int frontRow, final ArrayList<ActionsInput> actions) {

        int handIndex = actions.get(actionsIndex).getHandIdx();
        ObjectNode result = null;

        String command = actions.get(actionsIndex).getCommand();

        Card card = currentPlayer.getHand().get(handIndex);

        if (card == null) {
            return;
        }

        if (card.isEnvironmentCard()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("error", "Cannot place environment card on table.");
        } else if (currentPlayer.getTotalMana() < card.getMana()) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("error", "Not enough mana to place card on table.");
        } else if ((card.isBackRowMinion() && table.getCardRows().get(backRow).size() + 1
                    > table.getNoColumns()) || (card.isFrontRowMinion()
                        && table.getCardRows().get(frontRow).size() + 1 > table.getNoColumns())) {
            result = objectMapper.createObjectNode();
            result.put("command", command);
            result.put("handIdx", handIndex);
            result.put("error", "Cannot place card on table since row is full.");
        } else {
            currentPlayer.useMana(card.getMana());
            if (card.isBackRowMinion()) {
                table.addCard(backRow, card);
            } else {
                table.addCard(frontRow, card);
            }
            currentPlayer.removeCardFromHand(handIndex);
        }
        if (result != null) {
            output.add(result);
        }
    }
}
