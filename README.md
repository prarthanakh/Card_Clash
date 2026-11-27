# Card Clash

Card Clash is a Java card-battle game. Play against a computer opponent, choose an AI difficulty, and use attack, defense, healing, and special cards to reduce the opponent's System Integrity to zero.

## Requirements

- JDK 21 or later
- Apache Maven

## Run the game

Open a terminal in the project folder and run:

```powershell
mvn javafx:run
```

The first run may take longer while Maven downloads the project dependencies.

## How to play

1. Choose **Start Match** from the main menu.
2. Select **Easy** or **Hard** difficulty.
3. Flip the coin to determine who takes the first turn, then start the match.
4. On your turn, play cards you can afford with your current Bandwidth. Click **End Turn** when finished.
5. Reduce the opponent's Integrity to zero to win.

Each player starts with 30 Integrity, 0 Firewall, and a five-card hand. Bandwidth increases by one at the start of each turn, up to 10, and refills each turn. Damage uses Firewall before Integrity. Healing cannot raise Integrity above 30. The hand limit is seven cards; when a Toolkit runs out, used cards are reshuffled.

Use **How to Play** in the main menu for the complete rules and card reference.

## Project layout

- `src/main/java/com/cardclash/MainApp.java` — JavaFX application and screens
- `src/main/java/com/cardclash/model/` — cards, players, hands, and decks
- `src/main/java/com/cardclash/logic/` — game turns, results, and computer strategies
- `src/main/java/com/cardclash/database/` — SQLite database access
- `src/main/java/com/cardclash/exception/` — application exceptions
- `src/main/resources/com/cardclash/cardclash.css` — JavaFX screen and card styling

The SQLite database file, `cardclash.db`, is created in the working directory when the game starts. It contains the card library and saved player and match records.
