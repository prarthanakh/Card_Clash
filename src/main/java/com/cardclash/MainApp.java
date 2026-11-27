package com.cardclash;

import com.cardclash.database.CardDAO;
import com.cardclash.database.DatabaseManager;
import com.cardclash.database.MatchDAO;
import com.cardclash.database.PlayerDAO;
import com.cardclash.exception.DatabaseException;
import com.cardclash.exception.InsufficientBandwidthException;
import com.cardclash.exception.InvalidMoveException;
import com.cardclash.logic.Difficulty;
import com.cardclash.logic.EasyStrategy;
import com.cardclash.logic.GameEventListener;
import com.cardclash.logic.Game;
import com.cardclash.logic.GameResult;
import com.cardclash.logic.HardStrategy;
import com.cardclash.model.Card;
import com.cardclash.model.CardData;
import com.cardclash.model.CardResult;
import com.cardclash.model.CardFactory;
import com.cardclash.model.ComputerPlayer;
import com.cardclash.model.Deck;
import com.cardclash.model.HumanPlayer;
import com.cardclash.model.MatchRecord;
import com.cardclash.model.Player;
import com.cardclash.model.PlayerRecord;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class MainApp extends Application {

    private Game game;
    private Difficulty selectedDifficulty = Difficulty.EASY;
    private boolean humanWonCoinFlip;
    private boolean matchSaved;
    private Scene scene;
    private Stage primaryStage;

    private HBox handCards;
    private TextArea combatLog;
    private Button endTurnButton;
    private Label turnTitle;
    private Label turnPhase;
    private Label battleMessage;
    private Label handCountLabel;
    private Label playerField;
    private Label computerField;

    private Label humanIntegrityText;
    private Label humanFirewallText;
    private Label humanBandwidthText;
    private Label humanHandText;
    private ProgressBar humanIntegrityBar;
    private ProgressBar humanBandwidthBar;

    private Label computerIntegrityText;
    private Label computerFirewallText;
    private Label computerBandwidthText;
    private Label computerHandText;
    private ProgressBar computerIntegrityBar;
    private ProgressBar computerBandwidthBar;

    // The visible log is cleared when you press End Turn; messages from the
    // computer and your following actions stay together until then.
    private final List<String> visibleTurnLog = new ArrayList<>();
    private int lastGameLogIndex;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        scene = new Scene(createMainMenu(), 1360, 768);
        scene.getStylesheets().add(
                getClass().getResource("/com/cardclash/cardclash.css").toExternalForm());

        stage.setTitle("Card Clash");
        stage.setMinWidth(1050);
        stage.setMinHeight(700);
        stage.setScene(scene);
        stage.show();
    }

    private Parent createMainMenu() {
        Label kicker = new Label("NEURAL CARD BATTLEGROUND  //  SYSTEM ONLINE");
        kicker.getStyleClass().add("menu-kicker");

        Label cardTitle = new Label("CARD");
        cardTitle.getStyleClass().add("menu-title-cyan");
        Label clashTitle = new Label("CLASH");
        clashTitle.getStyleClass().add("menu-title-pink");

        Label subtitle = new Label("BUILD YOUR TOOLKIT. BREAK THEIR FIREWALL.");
        subtitle.getStyleClass().add("menu-subtitle");

        Button startButton = new Button("▶    START MATCH");
        startButton.getStyleClass().add("menu-button");
        startButton.setOnAction(event -> showMatchSetup());

        Button instructionsButton = new Button("HOW TO PLAY");
        instructionsButton.getStyleClass().addAll("menu-button", "menu-button-secondary");
        instructionsButton.setOnAction(event -> scene.setRoot(createInstructionsScreen()));

        Button leaderboardButton = new Button("♜    LEADERBOARD");
        leaderboardButton.getStyleClass().addAll("menu-button", "menu-button-secondary");
        leaderboardButton.setOnAction(event -> showLeaderboard());

        Button exitButton = new Button("⏻    EXIT");
        exitButton.getStyleClass().addAll("menu-button", "menu-button-secondary");
        exitButton.setOnAction(event -> Platform.exit());

        VBox buttons = new VBox(14, startButton, instructionsButton,
                leaderboardButton, exitButton);
        buttons.setAlignment(Pos.CENTER);

        VBox menu = new VBox(8, kicker, cardTitle, clashTitle, subtitle, buttons);
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(42));
        menu.getStyleClass().add("menu-screen");
        return menu;
    }

    private Parent createInstructionsScreen() {
        Label heading = new Label("HOW TO PLAY");
        heading.getStyleClass().add("setup-heading");
        Label subtitle = new Label("QUICK FIELD GUIDE // READ BEFORE DEPLOYMENT");
        subtitle.getStyleClass().add("menu-subtitle");

        HBox basics = new HBox(14,
                instructionCard("OBJECTIVE & SETUP",
                        "Reduce the opponent's System Integrity to 0. Both players begin "
                                + "with 30 Integrity and 0 Firewall. Each draws 5 cards. "
                                + "A coin flip decides who takes the first turn."),
                instructionCard("YOUR TURN",
                        "1. Bandwidth increases by 1 (up to 10) and refills. Draw 1 card.\n"
                                + "2. Play any affordable cards in any order.\n"
                                + "3. Press END TURN. Unused Bandwidth is lost."));

        HBox rules = new HBox(14,
                instructionCard("DAMAGE & DEFENSE",
                        "Damage removes Firewall first; leftover damage reduces Integrity. "
                                + "Firewall clears at the start of its owner's next turn. "
                                + "Healing cannot raise Integrity above 30."),
                instructionCard("BANDWIDTH & DECK",
                        "You cannot play a card if its cost exceeds your current Bandwidth. "
                                + "Your hand holds up to 7 cards; extra draws are discarded. "
                                + "When your Toolkit runs out, your used cards are reshuffled."));

        VBox firstCardColumn = new VBox(7);
        firstCardColumn.getChildren().addAll(
                cardReference("PING FLOOD", "1 BW  |  Deal 3 damage"),
                cardReference("SQL INJECTION", "3 BW  |  Deal 7 damage"),
                cardReference("ZERO-DAY EXPLOIT", "5 BW  |  Deal 12 damage"),
                cardReference("BASIC FIREWALL", "1 BW  |  Gain 3 Firewall"),
                cardReference("ENCRYPTION", "3 BW  |  Gain 8 Firewall"));

        VBox secondCardColumn = new VBox(7);
        secondCardColumn.getChildren().addAll(
                cardReference("HOTFIX", "2 BW  |  Heal 5 Integrity"),
                cardReference("SYSTEM RESTORE", "4 BW  |  Heal 10 Integrity"),
                cardReference("OVERCLOCK", "0 BW  |  Gain 2 Bandwidth this turn"),
                cardReference("BRUTE FORCE", "2 BW  |  Double your next Virus card's damage"),
                cardReference("DDOS ATTACK", "3 BW  |  Opponent skips their next turn"));

        HBox cardColumns = new HBox(14, firstCardColumn, secondCardColumn);
        VBox cardLibrary = new VBox(10, sectionHeading("CARD REFERENCE"), cardColumns);
        cardLibrary.getStyleClass().add("instruction-card");

        VBox content = new VBox(14, basics, rules, cardLibrary);
        content.setPadding(new Insets(8));
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.getStyleClass().add("instruction-scroll");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        Button backButton = new Button("BACK TO MENU");
        backButton.getStyleClass().add("menu-button-small");
        backButton.setOnAction(event -> scene.setRoot(createMainMenu()));
        HBox footer = new HBox(backButton);
        footer.setAlignment(Pos.CENTER_RIGHT);

        VBox page = new VBox(10, heading, subtitle, scroll, footer);
        page.setPadding(new Insets(24, 32, 20, 32));
        page.getStyleClass().add("instructions-screen");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        return page;
    }

    private VBox instructionCard(String heading, String body) {
        VBox card = new VBox(9, sectionHeading(heading));
        Label text = new Label(body);
        text.setWrapText(true);
        text.getStyleClass().add("instruction-body");
        card.getChildren().add(text);
        card.getStyleClass().add("instruction-card");
        HBox.setHgrow(card, Priority.ALWAYS);
        card.setMaxWidth(Double.MAX_VALUE);
        return card;
    }

    private Label sectionHeading(String text) {
        Label heading = new Label(text);
        heading.getStyleClass().add("instruction-heading");
        return heading;
    }

    private Label cardReference(String name, String details) {
        Label reference = new Label(name + "\n" + details);
        reference.setWrapText(true);
        reference.getStyleClass().add("card-reference");
        reference.setMaxWidth(Double.MAX_VALUE);
        return reference;
    }

    private void showMatchSetup() {
        Label heading = new Label("MATCH SETUP");
        heading.getStyleClass().add("setup-heading");
        Label instructions = new Label("SELECT AI DIFFICULTY, THEN FLIP FOR FIRST TURN");
        instructions.getStyleClass().add("menu-subtitle");

        ToggleGroup difficultyGroup = new ToggleGroup();
        ToggleButton easyButton = new ToggleButton("EASY");
        ToggleButton hardButton = new ToggleButton("HARD");
        easyButton.setToggleGroup(difficultyGroup);
        hardButton.setToggleGroup(difficultyGroup);
        easyButton.getStyleClass().add("difficulty-toggle");
        hardButton.getStyleClass().add("difficulty-toggle");
        easyButton.setSelected(true);
        selectedDifficulty = Difficulty.EASY;

        Label difficultyDescription = new Label(
                "Script Kiddie: chooses randomly from affordable cards.");
        difficultyDescription.getStyleClass().add("setup-description");
        difficultyGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                selectedDifficulty = newToggle == hardButton
                        ? Difficulty.HARD : Difficulty.EASY;
                difficultyDescription.setText(selectedDifficulty == Difficulty.EASY
                        ? "Script Kiddie: chooses randomly from affordable cards."
                        : "Rogue AI: prioritizes lethal attacks, healing, and disruption.");
            }
        });

        HBox difficultyButtons = new HBox(14, easyButton, hardButton);
        difficultyButtons.setAlignment(Pos.CENTER);

        Label coin = new Label("?");
        coin.getStyleClass().add("coin-display");
        Label flipResult = new Label("THE FIRST TURN IS UNDECIDED");
        flipResult.getStyleClass().add("flip-result");

        Button flipButton = new Button("FLIP COIN");
        flipButton.getStyleClass().add("menu-button");
        Button beginButton = new Button("START MATCH");
        beginButton.getStyleClass().add("menu-button");
        beginButton.setDisable(true);

        flipButton.setOnAction(event -> {
            humanWonCoinFlip = Math.random() < 0.5;
            coin.setText(humanWonCoinFlip ? "YOU" : "AI");
            flipResult.setText(humanWonCoinFlip
                    ? "YOU WON THE FLIP - YOU GO FIRST"
                    : "ROGUE AI WON THE FLIP - AI GOES FIRST");
            beginButton.setDisable(false);
            flipButton.setText("FLIP AGAIN");
        });
        beginButton.setOnAction(event -> startGame());

        Button backButton = new Button("BACK TO MENU");
        backButton.getStyleClass().add("menu-button-small");
        backButton.setOnAction(event -> scene.setRoot(createMainMenu()));

        VBox setup = new VBox(18, heading, instructions, difficultyButtons,
                difficultyDescription, coin, flipResult, flipButton, beginButton, backButton);
        setup.setAlignment(Pos.CENTER);
        setup.setPadding(new Insets(32));
        setup.getStyleClass().add("menu-screen");
        scene.setRoot(setup);
    }

    private void startGame() {
        try {
            DatabaseManager.initializeDatabase();
            List<CardData> cardData = new CardDAO().getAllCards();

            Deck humanDeck = new Deck(CardFactory.buildToolkit(cardData));
            Deck computerDeck = new Deck(CardFactory.buildToolkit(cardData));
            HumanPlayer human = new HumanPlayer("CYBER_OPERATOR", humanDeck);
            PlayerRecord playerRecord = new PlayerDAO().getOrCreatePlayer(human.getName());
            human.setDatabaseId(playerRecord.getPlayerId());
            ComputerPlayer computer = new ComputerPlayer(
                    "ROGUE_AI", computerDeck,
                    selectedDifficulty == Difficulty.HARD
                            ? new HardStrategy() : new EasyStrategy());

            game = new Game(human, computer, selectedDifficulty);
            matchSaved = false;
            visibleTurnLog.clear();
            lastGameLogIndex = 0;
            game.addListener(new GameEventListener() {
                @Override
                public void onTurnStarted(Player player) {
                }

                @Override
                public void onCardPlayed(Player player, Card card, CardResult result) {
                    showPlayedCard(player, card, result);
                }

                @Override
                public void onLogMessage(String message) {
                }

                @Override
                public void onGameOver(Player winner) {
                }
            });
            game.startGame(humanWonCoinFlip ? human : computer);
            captureNewGameMessages();

            Parent gameScreen = createGameScreen();
            scene.setRoot(gameScreen);
            renderHand();
            refreshGameDisplay();

            if (game.getCurrentPlayer() == game.getComputer()) {
                endTurnButton.setDisable(true);
                startComputerTurnStep();
            }
        } catch (DatabaseException e) {
            showError("Could not start the game", e.getMessage());
        }
    }

    private Parent createGameScreen() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("game-screen");
        root.setPadding(new Insets(12));

        VBox humanPanel = createPlayerPanel(true);
        VBox computerPanel = createPlayerPanel(false);

        turnTitle = new Label("ROUND 1  |  YOUR TURN");
        turnTitle.getStyleClass().add("turn-title");
        turnPhase = new Label("ACTION PHASE");
        turnPhase.getStyleClass().add("turn-phase");
        Label systemStatus = new Label("●  CONNECTION SECURE");
        systemStatus.getStyleClass().add("system-status");
        VBox turnPanel = new VBox(10, turnTitle, turnPhase, systemStatus);
        turnPanel.setAlignment(Pos.CENTER);
        turnPanel.getStyleClass().add("turn-panel");

        HBox header = new HBox(16, humanPanel, turnPanel, computerPanel);
        header.setAlignment(Pos.CENTER);
        HBox.setHgrow(humanPanel, Priority.ALWAYS);
        HBox.setHgrow(turnPanel, Priority.ALWAYS);
        HBox.setHgrow(computerPanel, Priority.ALWAYS);
        root.setTop(header);

        Label clashHeading = new Label("CLASH ZONE");
        clashHeading.getStyleClass().add("section-title");
        playerField = new Label("PLAYER FIELD\nWAITING FOR CARD");
        playerField.getStyleClass().addAll("battle-slot", "player-slot");
        Label versus = new Label("VS");
        versus.getStyleClass().add("versus-label");
        computerField = new Label("ROGUE AI FIELD\nWAITING FOR CARD");
        computerField.getStyleClass().addAll("battle-slot", "computer-slot");
        battleMessage = new Label("Choose a card to begin your move.");
        battleMessage.setWrapText(true);
        battleMessage.getStyleClass().add("battle-message");

        HBox battleSlots = new HBox(18, playerField, versus, computerField);
        battleSlots.setAlignment(Pos.CENTER);
        battleSlots.setMinHeight(120);
        VBox arena = new VBox(12, clashHeading, battleSlots, battleMessage);
        arena.setAlignment(Pos.CENTER);
        arena.setPadding(new Insets(14));
        arena.setMaxWidth(Double.MAX_VALUE);
        arena.getStyleClass().add("arena-panel");
        HBox.setHgrow(arena, Priority.ALWAYS);

        Label logHeading = new Label("TERMINAL LOG");
        logHeading.getStyleClass().add("terminal-heading");
        combatLog = new TextArea();
        combatLog.setEditable(false);
        combatLog.setWrapText(true);
        combatLog.setFocusTraversable(false);
        combatLog.getStyleClass().add("terminal-log");
        VBox logPanel = new VBox(10, logHeading, combatLog);
        logPanel.setPrefWidth(330);
        logPanel.setMinWidth(280);
        logPanel.getStyleClass().add("terminal-panel");
        VBox.setVgrow(combatLog, Priority.ALWAYS);

        HBox middle = new HBox(16, arena, logPanel);
        middle.setPadding(new Insets(8, 0, 8, 0));
        HBox.setHgrow(arena, Priority.ALWAYS);
        root.setCenter(middle);

        Label handHeading = new Label("YOUR TOOLKIT");
        handHeading.getStyleClass().add("section-title");
        handCountLabel = new Label("0 / 7 CARDS");
        handCountLabel.getStyleClass().add("hand-count");
        HBox handHeader = new HBox(handHeading, handCountLabel);
        handHeader.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(handCountLabel, Priority.ALWAYS);
        handCountLabel.setAlignment(Pos.CENTER_RIGHT);

        handCards = new HBox(12);
        handCards.setAlignment(Pos.CENTER_LEFT);
        handCards.setPadding(new Insets(4, 6, 6, 6));
        ScrollPane handScroll = new ScrollPane(handCards);
        handScroll.setFitToHeight(true);
        handScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        handScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        handScroll.getStyleClass().add("hand-scroll");
        handScroll.setPrefHeight(195);

        endTurnButton = new Button("END TURN  ➜");
        endTurnButton.getStyleClass().add("end-turn-button");
        endTurnButton.setOnAction(event -> finishHumanTurn());

        Button menuButton = new Button("MAIN MENU");
        menuButton.getStyleClass().add("menu-button-small");
        menuButton.setOnAction(event -> scene.setRoot(createMainMenu()));

        VBox handSection = new VBox(6, handHeader, handScroll);
        handSection.getStyleClass().add("hand-panel");
        HBox.setHgrow(handSection, Priority.ALWAYS);

        VBox controls = new VBox(12, endTurnButton, menuButton);
        controls.setAlignment(Pos.BOTTOM_CENTER);
        controls.setPadding(new Insets(0, 4, 8, 4));
        controls.setMinWidth(180);

        HBox bottom = new HBox(12, handSection, controls);
        bottom.setAlignment(Pos.BOTTOM_CENTER);
        root.setBottom(bottom);

        return root;
    }

    private VBox createPlayerPanel(boolean human) {
        VBox panel = new VBox(7);
        panel.getStyleClass().add(human ? "player-panel" : "player-panel-opponent");

        Label heading = new Label(human ? "HUMAN PLAYER" : "COMPUTER OPPONENT");
        heading.getStyleClass().add("player-heading");
        Label name = new Label(human ? "CYBER_OPERATOR"
                : "ROGUE_AI  //  " + game.getDifficulty().getLabel().toUpperCase());
        name.getStyleClass().add("player-name");

        Label integrity = new Label("30 / 30");
        ProgressBar integrityBar = new ProgressBar(1);
        integrityBar.setMaxWidth(Double.MAX_VALUE);
        integrityBar.getStyleClass().add("integrity-bar");
        Label firewall = new Label("0");
        Label bandwidth = new Label("0 / 10");
        ProgressBar bandwidthBar = new ProgressBar(0);
        bandwidthBar.setMaxWidth(Double.MAX_VALUE);
        bandwidthBar.getStyleClass().add("bandwidth-bar");
        Label hand = new Label("5 / 7");

        panel.getChildren().addAll(
                heading, name,
                statLine("SYSTEM INTEGRITY", integrity), integrityBar,
                statLine("FIREWALL", firewall),
                statLine("BANDWIDTH", bandwidth), bandwidthBar,
                statLine("HAND SIZE", hand));

        if (human) {
            humanIntegrityText = integrity;
            humanIntegrityBar = integrityBar;
            humanFirewallText = firewall;
            humanBandwidthText = bandwidth;
            humanBandwidthBar = bandwidthBar;
            humanHandText = hand;
        } else {
            computerIntegrityText = integrity;
            computerIntegrityBar = integrityBar;
            computerFirewallText = firewall;
            computerBandwidthText = bandwidth;
            computerBandwidthBar = bandwidthBar;
            computerHandText = hand;
        }
        return panel;
    }

    private HBox statLine(String caption, Label value) {
        Label label = new Label(caption);
        label.getStyleClass().add("stat-caption");
        value.getStyleClass().add("stat-value");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox row = new HBox(8, label, spacer, value);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private void finishHumanTurn() {
        if (game == null || game.isGameOver()
                || game.getCurrentPlayer() != game.getHuman()) {
            return;
        }

        visibleTurnLog.clear();
        renderCombatLog();
        game.endTurn();
        captureNewGameMessages();
        refreshGameDisplay();
        startComputerTurnStep();
    }

    private void startComputerTurnStep() {
        if (game == null || game.isGameOver()) {
            refreshGameDisplay();
            return;
        }

        if (game.getCurrentPlayer() != game.getComputer()) {
            renderHand();
            refreshGameDisplay();
            return;
        }

        boolean playedCard = game.runComputerStep();
        captureNewGameMessages();
        refreshGameDisplay();

        if (game.isGameOver()) {
            showGameOver();
            return;
        }

        if (!playedCard) {
            game.endTurn();
            captureNewGameMessages();
        }

        PauseTransition pause = new PauseTransition(Duration.seconds(0.9));
        pause.setOnFinished(event -> startComputerTurnStep());
        pause.play();
    }

    private void refreshGameDisplay() {
        if (game == null || turnTitle == null) {
            return;
        }

        Player human = game.getHuman();
        Player computer = game.getComputer();
        updatePlayerStats(human, humanIntegrityText, humanIntegrityBar,
                humanFirewallText, humanBandwidthText, humanBandwidthBar, humanHandText);
        updatePlayerStats(computer, computerIntegrityText, computerIntegrityBar,
                computerFirewallText, computerBandwidthText,
                computerBandwidthBar, computerHandText);

        int round = (game.getTurnNumber() + 1) / 2;
        turnTitle.setText("ROUND " + round + "  |  "
                + (game.getCurrentPlayer() == game.getHuman() ? "YOUR TURN" : "AI TURN"));
        turnPhase.setText(game.isGameOver() ? "MATCH COMPLETE" : "ACTION PHASE");
        handCountLabel.setText(human.getHand().size() + " / " + Player.HAND_LIMIT + " CARDS");
        handCards.setDisable(game.isGameOver()
                || game.getCurrentPlayer() != game.getHuman());
        endTurnButton.setDisable(game.isGameOver()
                || game.getCurrentPlayer() != game.getHuman());

        if (!visibleTurnLog.isEmpty()) {
            battleMessage.setText(visibleTurnLog.get(visibleTurnLog.size() - 1));
        }
    }

    private void updatePlayerStats(
            Player player, Label integrityText, ProgressBar integrityBar,
            Label firewallText, Label bandwidthText, ProgressBar bandwidthBar,
            Label handText) {
        integrityText.setText(player.getIntegrity() + "/" + Player.MAX_INTEGRITY);
        integrityBar.setProgress(player.getIntegrity() / (double) Player.MAX_INTEGRITY);
        firewallText.setText(String.valueOf(player.getFirewall()));
        bandwidthText.setText(player.getBandwidth() + "/" + Player.MAX_BANDWIDTH);
        bandwidthBar.setProgress(player.getBandwidth() / (double) Player.MAX_BANDWIDTH);
        handText.setText(player.getHand().size() + "/" + Player.HAND_LIMIT);
    }

    private void renderHand() {
        if (game == null || handCards == null) {
            return;
        }

        handCards.getChildren().clear();
        for (Card card : game.getHuman().getHand().getCards()) {
            Button cardButton = createCardButton(card);
            handCards.getChildren().add(cardButton);
        }
        refreshGameDisplay();
    }

    private Button createCardButton(Card card) {
        String description = card.getDescription() == null
                ? "" : card.getDescription();
        Label cost = new Label("BW " + card.getCost());
        cost.getStyleClass().add("card-cost");
        HBox costRow = new HBox(cost);
        costRow.setAlignment(Pos.CENTER_LEFT);

        Label name = new Label(card.getName().toUpperCase());
        name.setWrapText(true);
        name.getStyleClass().add("playing-card-name");

        Label symbol = new Label(cardSymbol(card));
        symbol.getStyleClass().add("card-art-symbol");
        StackPane art = new StackPane(symbol);
        art.getStyleClass().add("card-art-panel");
        VBox.setVgrow(art, Priority.ALWAYS);

        Label cardType = new Label(card.getType().name());
        cardType.getStyleClass().add("playing-card-type");
        Label stat = new Label(cardStat(card));
        stat.getStyleClass().add("playing-card-stat");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox footer = new HBox(5, cardType, spacer, stat);
        footer.setAlignment(Pos.CENTER_LEFT);

        Label effect = new Label(description);
        effect.setWrapText(true);
        effect.getStyleClass().add("playing-card-description");

        VBox face = new VBox(7, costRow, name, art, effect, footer);
        face.setAlignment(Pos.TOP_CENTER);
        face.setPrefSize(144, 154);
        face.getStyleClass().add("playing-card-face");

        Button button = new Button();
        button.setGraphic(face);
        button.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        button.setPrefSize(160, 175);
        button.setMinSize(155, 170);
        button.getStyleClass().add("playing-card");
        button.getStyleClass().add("card-" + card.getType().name().toLowerCase());
        button.setOnAction(event -> playHumanCard(card));
        return button;
    }

    private String cardSymbol(Card card) {
        switch (card.getType().name()) {
            case "VIRUS":
                return "☣";
            case "FIREWALL":
                return "⬡";
            case "PATCH":
                return "+";
            default:
                return "⌘";
        }
    }

    private String cardStat(Card card) {
        switch (card.getType().name()) {
            case "VIRUS":
                return card.getPower() + " DMG";
            case "FIREWALL":
                return card.getPower() + " DEF";
            case "PATCH":
                return card.getPower() + " HEAL";
            default:
                return "SPECIAL";
        }
    }

    private void playHumanCard(Card card) {
        if (game == null || game.getCurrentPlayer() != game.getHuman()) {
            return;
        }

        try {
            game.playCard(game.getHuman(), card);
            captureNewGameMessages();
            renderHand();
            refreshGameDisplay();
            if (game.isGameOver()) {
                showGameOver();
            }
        } catch (InsufficientBandwidthException | InvalidMoveException e) {
            turnPhase.setText(e.getMessage().toUpperCase());
        }
    }

    private void showPlayedCard(Player player, Card card, CardResult result) {
        Label field = player == game.getHuman() ? playerField : computerField;
        field.setText(card.getName().toUpperCase()
                + "\n" + card.getType().name() + "  |  COST " + card.getCost()
                + " BW\n\n" + card.getDescription());
        field.getStyleClass().removeAll(
                "field-virus", "field-firewall", "field-patch", "field-exploit");
        field.getStyleClass().add(
                "field-" + card.getType().name().toLowerCase());
        battleMessage.setText(result.getMessage());
    }

    private void captureNewGameMessages() {
        if (game == null) {
            return;
        }
        List<String> messages = game.getCombatLog();
        while (lastGameLogIndex < messages.size()) {
            visibleTurnLog.add(messages.get(lastGameLogIndex));
            lastGameLogIndex++;
        }
        renderCombatLog();
    }

    private void renderCombatLog() {
        if (combatLog == null) {
            return;
        }
        combatLog.setText(String.join("\n> ", visibleTurnLog));
        combatLog.positionCaret(combatLog.getLength());
    }

    private void showGameOver() {
        refreshGameDisplay();
        turnPhase.setText("MATCH COMPLETE  //  "
                + game.getWinner().getName().toUpperCase() + " WINS");
        battleMessage.setText(game.getWinner().getName() + " wins the clash.");
        endTurnButton.setDisable(true);
        handCards.setDisable(true);
        saveMatchResult();
    }

    private void saveMatchResult() {
        if (matchSaved || game == null || game.getWinner() == null) {
            return;
        }
        matchSaved = true;

        try {
            HumanPlayer human = game.getHuman();
            GameResult result = game.getWinner() == human
                    ? GameResult.WIN : GameResult.LOSS;
            MatchRecord record = new MatchRecord(
                    0, human.getDatabaseId(), game.getDifficulty(), result,
                    game.getTurnNumber(), human.getIntegrity(), null);

            new MatchDAO().saveMatchWithUsage(record, game.getCardsPlayedByHuman());
            PlayerDAO playerDAO = new PlayerDAO();
            if (result == GameResult.WIN) {
                playerDAO.incrementWins(human.getDatabaseId());
            } else {
                playerDAO.incrementLosses(human.getDatabaseId());
            }
            visibleTurnLog.add("Match result saved to the leaderboard.");
            renderCombatLog();
        } catch (DatabaseException e) {
            visibleTurnLog.add("Could not save match result: " + e.getMessage());
            renderCombatLog();
            matchSaved = false;
        }
    }

    private void showLeaderboard() {
        try {
            DatabaseManager.initializeDatabase();
            List<PlayerRecord> players = new PlayerDAO().getTopPlayers();
            StringBuilder rows = new StringBuilder();
            for (int i = 0; i < players.size(); i++) {
                PlayerRecord player = players.get(i);
                rows.append(String.format("%2d. %-20s  W %d  /  L %d%n",
                        i + 1, player.getUsername(), player.getWins(), player.getLosses()));
            }
            if (rows.isEmpty()) {
                rows.append("No player results have been saved yet.");
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Card Clash Leaderboard");
            alert.setHeaderText("TOP OPERATORS");
            alert.setContentText(rows.toString());
            alert.showAndWait();
        } catch (DatabaseException e) {
            showError("Could not load leaderboard", e.getMessage());
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Card Clash");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
