package classes.gameInfo;

import classes.cards.minions.TheCursedOne;
import classes.cards.minions.TheRipper;
import classes.cards.environment.Firestorm;
import classes.cards.environment.HeartHound;
import classes.cards.environment.Winterfell;
import classes.cards.*;
import classes.cards.heroes.EmpressThorina;
import classes.cards.heroes.GeneralKocioraw;
import classes.cards.heroes.KingMudface;
import classes.cards.heroes.LordRoyce;
import classes.cards.minions.Disciple;
import classes.cards.minions.Miraj;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.CardInput;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {
    private ArrayList<Card> deck;
    private ArrayList<Card> hand;

    private int totalMana;
    private Card hero;
    private int totalGames;
    private int totalWins;

    public Player() {
        this.deck = new ArrayList<Card>();
        this.hand = new ArrayList<Card>();
        this.totalGames = 0;
        this.totalWins = 0;
    }

    /**
        The method removes all the cards found in the current deck of the player
        (when another game starts and the player picks another deck)
     */
    public void clearDeck() {
        while (!this.deck.isEmpty()) {
            this.deck.remove(0);
        }
    }

    /**
         The method removes all the cards found in the hand of the player
         (when another game starts and the player still has unused/unplaced cards)
     */
    public void clearHand() {
        while (!this.hand.isEmpty()) {
            this.hand.remove(0);
        }
    }

    /**
        The method constructs the deck that the player hs chosen (by using deep copy)

        @param decks: the array of decks from which a deck is picked up
        @param deckIndex: the position of the chosen deck within the array
        @param seed: the seed needed to shuffle the deck after its construction
     */
    public void chooseDeck(final ArrayList<ArrayList<CardInput>> decks,
        final int deckIndex, final int seed) {

        Random random = new Random(seed);
        ArrayList<CardInput> chosenDeck = decks.get(deckIndex);

        for (CardInput card : chosenDeck) {
            if (card.getName().compareTo("Goliath") == 0
                    || card.getName().compareTo("Warden") == 0) {
                this.deck.add(new Card(card));
            }
            if (card.getName().compareTo("Sentinel") == 0
                    || card.getName().compareTo("Berserker") == 0) {
                this.deck.add(new Card(card));
            }
            if (card.getName().compareTo("The Ripper") == 0) {
                this.deck.add(new TheRipper(card));
            }
            if (card.getName().compareTo("Miraj") == 0) {
                this.deck.add(new Miraj(card));
            }
            if (card.getName().compareTo("The Cursed One") == 0) {
                this.deck.add(new TheCursedOne(card));
            }
            if (card.getName().compareTo("Disciple") == 0) {
                this.deck.add(new Disciple(card));
            }
            if (card.getName().compareTo("Firestorm") == 0) {
                this.deck.add(new Firestorm(card));
            }
            if (card.getName().compareTo("Winterfell") == 0) {
                this.deck.add(new Winterfell(card));
            }
            if (card.getName().compareTo("Heart Hound") == 0) {
                this.deck.add(new HeartHound(card));
            }
        }

        Collections.shuffle(this.deck, random);
    }

    /**
        The method constructs the hero that the player has chosen (by using deep copy)

        @param card: the hero card that is picked up
     */
    public void setHero(final CardInput card) {
        if (card.getName().compareTo("Lord Royce") == 0) {
            this.hero = new LordRoyce(card);
        }
        if (card.getName().compareTo("Empress Thorina") == 0) {
            this.hero = new EmpressThorina(card);
        }
        if (card.getName().compareTo("King Mudface") == 0) {
            this.hero = new KingMudface(card);
        }
        if (card.getName().compareTo("General Kocioraw") == 0) {
            this.hero = new GeneralKocioraw(card);
        }
    }

    /**
        The method removed the card that the player decided to use/place on the game table

        @param handIndex: the position of the chosen card within the array that represents the hand
     */
    public void removeCardFromHand(final int handIndex) {
        if (handIndex < 0 || handIndex >= this.hand.size()) {
            return;
        }

        this.hand.remove(handIndex);
    }

    /**
        The method takes the first card (if it exists) from the deck that the player owns,
        places it at the end of the hand array of cards (by using deep copy) and then
        removes it from the deck
     */
    public void placeCardInHand() {
        if (this.deck.isEmpty()) {
            return;
        }

        Card card = this.deck.get(0);

        if (card.getName().compareTo("Goliath") == 0
                || card.getName().compareTo("Warden") == 0) {
            this.hand.add(new Card(card));
        }
        if (card.getName().compareTo("Sentinel") == 0
                || card.getName().compareTo("Berserker") == 0) {
            this.hand.add(new Card(card));
        }
        if (card.getName().compareTo("The Ripper") == 0) {
            this.hand.add(new TheRipper(card));
        }
        if (card.getName().compareTo("Miraj") == 0) {
            this.hand.add(new Miraj(card));
        }
        if (card.getName().compareTo("The Cursed One") == 0) {
            this.hand.add(new TheCursedOne(card));
        }
        if (card.getName().compareTo("Disciple") == 0) {
            this.hand.add(new Disciple(card));
        }
        if (card.getName().compareTo("Firestorm") == 0) {
            this.hand.add(new Firestorm(card));
        }
        if (card.getName().compareTo("Winterfell") == 0) {
            this.hand.add(new Winterfell(card));
        }
        if (card.getName().compareTo("Heart Hound") == 0) {
            this.hand.add(new HeartHound(card));
        }

        this.deck.remove(0);
    }

    public ArrayList<Card> getHand() {
        return this.hand;
    }

    public Card getHero() {
        return this.hero;
    }

    /**
        The method updates the player mana by collecting some every new round

        @param mana: the mana score that is added to the total
     */
    public void receiveMana(final int mana) {
        this.totalMana += mana;
    }

    public int getTotalMana() {
        return this.totalMana;
    }

    public void setTotalMana(final int totalMana) {
        this.totalMana = totalMana;
    }

    /**
        The method decreases the total mana the current player has when placing
        a card or using its ability

        @param mana: the mana score that the card costs
     */
    public void useMana(final int mana) {
        this.totalMana -= mana;
    }

    public int getTotalWins() {
        return totalWins;
    }

    /**
         The method creates a JSON object mapper that contains the details about all the
         cards that can be found in the player's hand

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param playerIndex: the player whose hand is to be shown
         @return ObjectNode: the constructed object
     */
    public ObjectNode showCardsInHand(final ObjectMapper objectMapper, final int playerIndex) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getCardsInHand");
        result.put("playerIdx", playerIndex);

        ArrayNode outputCards = objectMapper.createArrayNode();

        for (Card card : this.hand) {
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

            outputCards.add(outputCard);
        }

        result.set("output", outputCards);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about all the
         cards that can be found in the player's current deck

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param playerIndex: the player whose deck is to be shown
         @return ObjectNode: the constructed object
     */
    public ObjectNode showCardsInDeck(final ObjectMapper objectMapper, final int playerIndex) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getPlayerDeck");
        result.put("playerIdx", playerIndex);

        ArrayNode outputCards = objectMapper.createArrayNode();

        for (Card card : this.deck) {
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

            outputCards.add(outputCard);
        }

        result.set("output", outputCards);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about the
         hero card that the player has

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param playerIndex: the player whose hero is to be shown
         @return ObjectNode: the constructed object
     */
    public ObjectNode showHero(final ObjectMapper objectMapper, final int playerIndex) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getPlayerHero");
        result.put("playerIdx", playerIndex);

        ObjectNode outputHero = objectMapper.createObjectNode();

        outputHero.put("mana", this.hero.getMana());
        outputHero.put("description", this.hero.getDescription());

        ArrayNode outputColors = objectMapper.createArrayNode();
        for (String color : this.hero.getColors()) {
            outputColors.add(color);
        }

        outputHero.set("colors", outputColors);

        outputHero.put("name", this.hero.getName());
        outputHero.put("health", this.hero.getHealth());

        result.set("output", outputHero);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about the
         total mana that the player currently has

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param playerIndex: the player whose total mana is to be shown
         @return ObjectNode: the constructed object
     */
    public ObjectNode showTotalMana(final ObjectMapper objectMapper, final int playerIndex) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getPlayerMana");
        result.put("playerIdx", playerIndex);
        result.put("output", this.totalMana);

        return result;
    }

    /**
         The method creates a JSON object mapper that contains the details about all the
         environment cards that can be found in the player's hand

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @param playerIndex: the player whose cards are shown
         @return ObjectNode: the constructed object
     */
    public ObjectNode showEnvironmentCardsInHand(final ObjectMapper objectMapper,
         final int playerIndex) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getEnvironmentCardsInHand");
        result.put("playerIdx", playerIndex);

        ArrayNode outputCards = objectMapper.createArrayNode();

        for (Card card : this.hand) {
            ObjectNode outputCard = null;
            if (card.isEnvironmentCard()) {
                outputCard = objectMapper.createObjectNode();
                outputCard.put("mana", card.getMana());
                outputCard.put("description", card.getDescription());

                ArrayNode outputColors = objectMapper.createArrayNode();
                for (String color : card.getColors()) {
                    outputColors.add(color);
                }

                outputCard.set("colors", outputColors);

                outputCard.put("name", card.getName());
            }

            if (outputCard != null) {
                outputCards.add(outputCard);
            }
        }

        result.set("output", outputCards);

        return result;
    }

    /**
        The method counts the total number of games a player has participated in
     */
    public void hasPlayedAnotherGame() {
        this.totalGames++;
    }

    /**
     The method counts the total number of games a player has won
     */
    public void hasWonAnotherGame() {
        this.totalWins++;
    }

    /**
         The method creates a JSON object mapper that contains the number of games
         a player has participated in

         @param objectMapper: the object that helps creating a new instance of the JSON mapper
         @return ObjectNode: the constructed object
     */
    public ObjectNode showTotalGamesPlayed(final ObjectMapper objectMapper) {
        ObjectNode result = objectMapper.createObjectNode();

        result.put("command", "getTotalGamesPlayed");
        result.put("output", this.totalGames);

        return result;
    }
}
