package classes.cards;

import classes.gameInfo.GameTable;
import fileio.CardInput;
import java.util.ArrayList;

public class Card {
    private int mana;
    private int health;
    private int attackDamage;
    private String description;
    private ArrayList<String> colors;
    private String name;
    private int isFrozen;
    private int hasAlreadyAttacked;

    public Card() {
    }

    public Card(final CardInput cardInput) {
        if (cardInput == null) {
            return;
        }

        this.mana = cardInput.getMana();
        this.health = cardInput.getHealth();
        this.attackDamage = cardInput.getAttackDamage();
        this.description = new String(cardInput.getDescription());
        this.colors = new ArrayList<String>();
        for (String color : cardInput.getColors()) {
            this.colors.add(new String(color));
        }
        this.name = new String(cardInput.getName());
        this.isFrozen = 0;
        this.hasAlreadyAttacked = 0;
    }

    public Card(final Card card) {
        if (card == null) {
            return;
        }

        this.mana = card.getMana();
        this.health = card.getHealth();
        this.attackDamage = card.getAttackDamage();
        this.description = new String(card.getDescription());
        this.colors = new ArrayList<String>();
        for (String color : card.getColors()) {
            this.colors.add(new String(color));
        }
        this.name = new String(card.getName());
        this.isFrozen = card.isFrozen;
        this.hasAlreadyAttacked = card.hasAlreadyAttacked;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final int getHealth() {
        return health;
    }

    public final void setHealth(final int health) {
        this.health = health;
    }

    public final int getAttackDamage() {
        return attackDamage;
    }

    public final void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    /**
        The method checks if the current card had been frozen or not during the game.
     */
    public final boolean cardIsFrozen() {
        if (this.isFrozen == 0) {
            return false;
        }
        return true;
    }

    /**
        The method sets the current card as frozen (it cannot attack or be
        attacked for the present turn)
     */
    public final void freeze() {
        this.isFrozen = 1;
    }

    /**
        The method reverses the freezing effect over the current card after
        the turn during which it occurred ends
     */
    public final void defrost() {
        this.isFrozen = 0;
    }

    /**
        The method indicates that the card has already attacked in the current turn
        by setting the corresponding attribute as 1 (meaning true), as it can only
        attack once during a round
     */
    public final void hasAttacked() {
        this.hasAlreadyAttacked = 1;
    }

    /**
        The method resets the attribute responsible for checking whether the card has
        attacked in the current round or not
     */
    public final void hadNotAttacked() {
        this.hasAlreadyAttacked = 0;
    }

    /**
        The method checks if the card has already attacked or used its ability
        during the current turn
     */
    public final boolean didAttack() {
        if (this.hasAlreadyAttacked == 1) {
            return true;
        }
        return false;
    }

    /**
        The method verifies if the current card is a minion that is supposed
        to be placed on the first row of the table that is assigned to the player
     */
    public final boolean isFrontRowMinion() {
        if (this.name.compareTo("The Ripper") == 0) {
            return true;
        }
        if (this.name.compareTo("Miraj") == 0) {
            return true;
        }
        if (this.name.compareTo("Goliath") == 0) {
            return true;
        }
        if (this.name.compareTo("Warden") == 0) {
            return true;
        }
        return false;
    }

    /**
         The method verifies if the current card is a minion that is supposed
         to be placed on the second row of the table that is assigned to the player
     */
    public final boolean isBackRowMinion() {
        if (this.name.compareTo("Sentinel") == 0) {
            return true;
        }
        if (this.name.compareTo("Berserker") == 0) {
            return true;
        }
        if (this.name.compareTo("The Cursed One") == 0) {
            return true;
        }
        if (this.name.compareTo("Disciple") == 0) {
            return true;
        }
        return false;
    }

    /**
        The method checks whether the current card is the "environment" type or not
     */
    public final boolean isEnvironmentCard() {
        if (this.name.compareTo("Firestorm") == 0) {
            return true;
        }
        if (this.name.compareTo("Winterfell") == 0) {
            return true;
        }
        if (this.name.compareTo("Heart Hound") == 0) {
            return true;
        }
        return false;
    }

    /**
        The method checks whether the current card is the "hero" type or not
     */
    public final boolean isHero() {
        if (this.name.compareTo("Lord Royce") == 0) {
            return true;
        }
        if (this.name.compareTo("Empress Thorina") == 0) {
            return true;
        }
        if (this.name.compareTo("King Mudface") == 0) {
            return true;
        }
        if (this.name.compareTo("General Kocioraw") == 0) {
            return true;
        }
        return false;
    }

    /**
        The method checks whether the current card is the "tank" type or not
     */
    public final boolean isTank() {
        if (this.name.compareTo("Goliath") == 0) {
            return true;
        }
        if (this.name.compareTo("Warden") == 0) {
            return true;
        }
        return false;
    }

    /**
        The method indicates if the row of the given player is an enemy to the card
        that is supposed to attack
     */
    public final boolean isEnemy(final int playerIndex, final int row) {
        if (playerIndex == 1) {
            if (row >= 2) {
                return true;
            }
        }

        if (playerIndex == 2) {
            if (row <= 1) {
                return true;
            }
        }
        return false;
    }

    /**
         The method indicates if the row of the given player is an ally to the card
         that is supposed to attack
     */
    public final boolean isAlly(final int playerIndex, final int row) {
        if (!this.isEnemy(playerIndex, row)) {
            return true;
        }
        return false;
    }

    /**
        The method implements the "attack" command that all cards hold
        (the attacked card's health decreases by "attackDamage" units of the attacker)

        @param card: the enemy card that is affected
     */
    public void attack(final Card card) {
        if (card == null || this.isEnvironmentCard()) {
            return;
        }
        card.setHealth(card.getHealth() - this.attackDamage);
        this.hasAttacked();
    }

    /**
        The method implements the "ability" command that is applied over a certain card
        depending on the attacker's type (the method is overridden depending on the attacker's ability)

        @param card: the enemy/ally card that is affected
     */
    public void abilityOnCard(final Card card) {
    }

    /**
     The method implements the "ability" command that is applied over a certain row of cards
     depending on the attacker's type (the method is overridden depending on the attacker's ability)

     @param table: the 4x5 matrix that holds the cards
     @param row: the enemy/ally row of the game table that is affected
     */
    public void abilityOnRow(final GameTable table, final int row) {
    }
}
