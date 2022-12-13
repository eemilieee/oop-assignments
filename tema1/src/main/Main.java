package main;

import checker.Checker;
import classes.gameInfo.GameTable;
import classes.gameInfo.Player;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import checker.CheckerConstants;
import fileio.ActionsInput;
import fileio.DecksInput;
import fileio.StartGameInput;
import fileio.GameInput;
import fileio.Input;
import classes.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {

    private static final int MAX_MANA = 10;

    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     *
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);

        ArrayNode output = objectMapper.createArrayNode();

        GameTable table = new GameTable();
        Player player1 = new Player();
        Player player2 = new Player();

        Player currentPlayer;
        int currentPlayerIndex, endedTurns, currentRound, endGame;
        int backRow, frontRow;
        int i, j;

        DecksInput deckSet1 = inputData.getPlayerOneDecks();
        DecksInput deckSet2 = inputData.getPlayerTwoDecks();

        for (i = 0; i < inputData.getGames().size(); i++) {

            GameInput currentGame = inputData.getGames().get(i);
            StartGameInput gameDetails = currentGame.getStartGame();
            ArrayList<ActionsInput> actions = currentGame.getActions();

            player1.chooseDeck(deckSet1.getDecks(), gameDetails.getPlayerOneDeckIdx(),
                                                            gameDetails.getShuffleSeed());
            player2.chooseDeck(deckSet2.getDecks(), gameDetails.getPlayerTwoDeckIdx(),
                                                            gameDetails.getShuffleSeed());
            player1.setHero(gameDetails.getPlayerOneHero());
            player2.setHero(gameDetails.getPlayerTwoHero());

            currentPlayerIndex = gameDetails.getStartingPlayer();

            currentRound = 1;
            endedTurns = 0;
            endGame = 0;

            player1.placeCardInHand();
            player2.placeCardInHand();

            player1.setTotalMana(0);
            player2.setTotalMana(0);

            player1.receiveMana(currentRound);
            player2.receiveMana(currentRound);

            player1.hasPlayedAnotherGame();
            player2.hasPlayedAnotherGame();

            for (j = 0; j < actions.size(); j++) {

                if (currentPlayerIndex == 1) {
                    currentPlayer = player1;
                    backRow = table.getNoRows() - 1;
                    frontRow = backRow - 1;
                } else {
                    currentPlayer = player2;
                    backRow = 0;
                    frontRow = backRow + 1;
                }

                String command = actions.get(j).getCommand();

                Helper.debugCommands(objectMapper, output, table, player1, player2,
                                                        currentPlayerIndex, j, actions);

                if (endGame == 0) {
                    if (command.compareTo("placeCard") == 0) {
                        Helper.placeCard(objectMapper, output, table, currentPlayer, j,
                                                        backRow, frontRow, actions);
                    }

                    if (command.compareTo("useEnvironmentCard") == 0) {
                        Helper.useEnvironmentCard(objectMapper, output, table, currentPlayer,
                                                        currentPlayerIndex, j, actions);
                    }

                    if (command.compareTo("cardUsesAttack") == 0) {
                        Helper.cardUsesAttack(objectMapper, output, table, currentPlayerIndex,
                                                                            j, actions);
                    }

                    if (command.compareTo("cardUsesAbility") == 0) {
                        Helper.cardUsesAbility(objectMapper, output, table, currentPlayerIndex,
                                                                            j, actions);
                    }

                    if (command.compareTo("useHeroAbility") == 0) {
                        Helper.useHeroAbility(objectMapper, output, table, currentPlayer,
                                                    currentPlayerIndex, j, actions);
                    }

                    if (command.compareTo("useAttackHero") == 0) {
                        endGame = Helper.useAttackHero(objectMapper, output, table, player1,
                                    player2, currentPlayerIndex, endGame, j, actions);
                    }

                    if (command.compareTo("endPlayerTurn") == 0) {
                        table.defrostRow(frontRow);
                        table.defrostRow(backRow);
                        table.restore(frontRow);
                        table.restore(backRow);

                        if (currentPlayerIndex == 1) {
                            player1.getHero().hadNotAttacked();
                        } else {
                            player2.getHero().hadNotAttacked();
                        }

                        endedTurns++;
                        currentPlayerIndex = table.getNoRows() - 1 - currentPlayerIndex;
                    }
                    if (endedTurns == 2) {
                        currentRound++;
                        endedTurns = 0;

                        player1.placeCardInHand();
                        player2.placeCardInHand();

                        if (currentRound <= MAX_MANA) {
                            player1.receiveMana(currentRound);
                            player2.receiveMana(currentRound);
                        } else {
                            player1.receiveMana(MAX_MANA);
                            player2.receiveMana(MAX_MANA);
                        }
                    }
                }
            }

            player1.clearHand();
            player2.clearHand();

            player1.clearDeck();
            player2.clearDeck();

            table.clearGameTable();
        }

        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }
}
