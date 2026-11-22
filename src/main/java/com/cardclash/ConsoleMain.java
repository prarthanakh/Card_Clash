package com.cardclash;

import com.cardclash.exception.InsufficientBandwidthException;
import com.cardclash.exception.InvalidMoveException;
import com.cardclash.logic.Difficulty;
import com.cardclash.logic.EasyStrategy;
import com.cardclash.logic.Game;
import com.cardclash.logic.HardStrategy;
import com.cardclash.logic.Strategy;
import com.cardclash.model.Card;
import com.cardclash.model.ComputerPlayer;
import com.cardclash.model.Deck;
import com.cardclash.model.ExploitCard;
import com.cardclash.model.ExploitEffect;
import com.cardclash.model.FirewallCard;
import com.cardclash.model.Hand;
import com.cardclash.model.HumanPlayer;
import com.cardclash.model.PatchCard;
import com.cardclash.model.VirusCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Text-only version for the first evaluation. Cards are hard-coded here (no database yet). */
public class ConsoleMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

System.out.print("Your name: ");
String name = scanner.nextLine().trim();
if (name.isEmpty()) {
    name = "Player";
}

System.out.println("1. Easy");
System.out.println("2. Hard");
System.out.print("Choose difficulty: ");
int choice = readChoice(scanner, 2);

Difficulty difficulty = choice == 1 ? Difficulty.EASY : Difficulty.HARD;
Strategy strategy = choice == 1 ? new EasyStrategy() : new HardStrategy();

HumanPlayer human = new HumanPlayer(name, new Deck(buildHardCodedCards()));
ComputerPlayer computer = new ComputerPlayer(
        "Computer", new Deck(buildHardCodedCards()), strategy);

Game game = new Game(human, computer, difficulty);
game.startGame();
while (!game.isGameOver()) {
    printStatus(game);

    if (game.getCurrentPlayer() == human) {
        List<Card> handCards = human.getHand().getCards();
        printHand(human.getHand());

        int endTurnChoice = handCards.size() + 1;
        System.out.println(endTurnChoice + ". End turn");
        System.out.print("Choose a card or end your turn: ");

        int playerChoice = readChoice(scanner, endTurnChoice);

        if (playerChoice == endTurnChoice) {
            game.endTurn();
        } else {
            try {
                System.out.println(game.playCard(
                        human, handCards.get(playerChoice - 1)).getMessage());
            } catch (InsufficientBandwidthException | InvalidMoveException e) {
                System.out.println(e.getMessage());
            }
        }
    } else {
        computer.takeTurn(game);
        if (!game.isGameOver()) {
            game.endTurn();
        }
    }
}

System.out.println(game.getWinner().getName() + " wins!");
     }

    private static List<Card> buildHardCodedCards() {
        
        List<Card> cards = new ArrayList<>();
cards.add(new VirusCard(1, "Ping Flood", 1, 3, "Deal 3 damage"));
cards.add(new FirewallCard(2, "Basic Firewall", 1, 3, "Gain 3 Firewall"));
cards.add(new PatchCard(3, "Hotfix", 2, 5, "Heal 5 Integrity"));
cards.add(new VirusCard(4, "SQL Injection", 3, 7, "Deal 7 damage"));
cards.add(new ExploitCard(5, "Brute Force", 2,
        ExploitEffect.BRUTE_FORCE, "Double your next Virus"));

return cards;
    }

    private static void printStatus(Game game) {
        System.out.println("\nTurn " + game.getTurnNumber());
System.out.println("Human: " + game.getHuman());
System.out.println("Computer: " + game.getComputer());
System.out.println("Current turn: " + game.getCurrentPlayer().getName());
    }

    private static void printHand(Hand hand) {
        List<Card> cards = hand.getCards();

if (cards.isEmpty()) {
    System.out.println("Your hand is empty.");
    return;
}

for (int i = 0; i < cards.size(); i++) {
    System.out.println((i + 1) + ". " + cards.get(i));
}
    }

    private static int readChoice(Scanner scanner, int max) {
        while (true) {
    String input = scanner.nextLine().trim();

    try {
        int choice = Integer.parseInt(input);
        if (choice >= 1 && choice <= max) {
            return choice;
        }
    } catch (NumberFormatException ignored) {
        // Ask again if the input isn't a number.
    }

    System.out.print("Enter a number from 1 to " + max + ": ");
}
        
        
    }
}
