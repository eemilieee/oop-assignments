

# Tema POO  - GwentStone

<div align="center"><img src="https://tenor.com/view/witcher3-gif-9340436.gif" width="500px"></div>

#### Assignment Link: [https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema](https://ocw.cs.pub.ro/courses/poo-ca-cd/teme/tema)

##### Name: Arpasanu Emilia-Oana
##### Group: 321 CA

## The idea

The program is designed to implement a card game that receives commands as an
input and executes them according to the specified rules. It consists of amicable
games between two players, offering a wide range of cards to be used.

## The structure

The built application follows a simplified class hierarchy that may allow further
development, highlighting encapsulation, polymorphism and inheritance. One of the main part is the card arrangement: as it is observed,
the Card class becomes the heart of the implementation; it is extended by the particular
types of cards used during the game, whose target is to redefine the way a generic card
applies its ability. This feature is possible by overriding the methods found in
the super class, easing the process of calling the methods in question. Apart from
the cards, there is also a GameTable class, that represents the area where minions
are placed and fulfill their tasks. Its specific methods consist of cards' manipulation,
such as insertion, removal and retrieving cards. The player class holds critical
information: the chosen deck, the existing cards in hand, the hero that represents
the instantiated object, the mana used for placing cards on the table and using the
hero's ability
or the total number of wins. Lastly, the methods that handle all the exceptions that
may occur during the game and execute the commands are found within the Helper class.
The existing commands describe two principal types: the "debug" ones, that retrieve wanted information
and the others which characterise the whole game process.

## The logic of the program

The entry point of the implementation begins with the objects' instantiations: the
game table and the two players. For each game that is supposed to happen some
initializations take place: the decks, the heroes, the mana, the current player index and
two counters, the number of rounds and turns. The players also place the first card
of each of their decks in their hands.Then comes an iteration through the actions
and the commands' execution. When the turn of the current player ends, the index
switches to the other player's one, the turns' number increases and the cards are
marked as unfrozen and able to attack again. If two turn have ended, the round counter
increases, the turns' one resets, two other cards are retrieved from the decks and mana
is received. After a game ends, as one hero is defeated, the game table, the decks and the hands are
emptied.

## Skel Structure

* src/
  * checker/ - checker files
  * fileio/ - contains classes used to read data from the json files
  * main/
      * Main - the Main class runs the checker on your implementation. Add the entry point to your implementation in it. Run Main to test your implementation from the IDE or from command line.
      * Test - run the main method from Test class with the name of the input file from the command line and the result will be written
        to the out.txt file. Thus, you can compare this result with ref.
* input/ - contains the tests in JSON format
* ref/ - contains all reference output for the tests in JSON format

## Tests

1. test01_game_start - 3p
2. test02_place_card - 4p
3. test03_place_card_invalid - 4p
4. test04_use_env_card - 4p
5. test05_use_env_card_invalid - 4p
6. test06_attack_card - 4p
7. test07_attack_card_invalid - 4p
8. test08_use_card_ability - 4p
9. test09_use_card_ability_invalid -4p
10. test10_attack_hero - 4p
11. test11_attack_hero_invalid - 4p
12. test12_use_hero_ability_1 - 4p
13. test13_use_hero_ability_2 - 4p
14. test14_use_hero_ability_invalid_1 - 4p
15. test15_use_hero_ability_invalid_2 - 4p
16. test16_multiple_games_valid - 5p
17. test17_multiple_games_invalid - 6p
18. test18_big_game - 10p


<div align="center"><img src="https://tenor.com/view/homework-time-gif-24854817.gif" width="500px"></div>
