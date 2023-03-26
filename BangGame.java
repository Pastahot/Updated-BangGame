import java.util.*;


public class BangGame {
    private static final int STARTING_LIVES = 4;

    private final List<String> deck = new ArrayList<>();
    private final List<String> discardedCards = new ArrayList<>();
    private final Map<String, Integer> playerLives = new LinkedHashMap<>();
    private final Map<String, List<String>> playerCards = new LinkedHashMap<>();
    private boolean gameIsOver = false; // initialize gameIsOver to false

    public BangGame() {
        initializeDeck();
        shuffleDeck();
        dealCards();
    }

    public void play() {
        System.out.println("Welcome to the simplified BANG card game!");

        while (playerLives.size() > 1) {
            for (String playerName : playerLives.keySet()) {
                playTurn(playerName);
                if (playerLives.size() == 1) {
                    break;
                }
            }
        }

        String winner = playerLives.keySet().iterator().next();
        System.out.println("Game over! " + winner + " is the winner!");
    }

    private void playTurn(String playerName) {
        System.out.println("It's " + playerName + "'s turn.");
        System.out.println(playerName + " has " + playerLives.get(playerName) + " lives.");
        showPlayerCards(playerName);

        boolean hasDynamite = playerCards.get(playerName).contains("Dynamite");
        boolean hasJail = playerCards.get(playerName).contains("Jail");

        if (hasJail) {
            int chance = new Random().nextInt(4) + 1;
            if (chance == 1) {
                System.out.println("You escaped from jail!");
            } else {
                System.out.println("You are still in jail and lose your turn.");
                playerCards.get(playerName).remove("Jail");
                discardedCards.add("Jail");
                return;
            }
            playerCards.get(playerName).remove("Jail");
            discardedCards.add("Jail");
        }

        if (hasDynamite) {
            int chance = new Random().nextInt(8) + 1;
            if (chance == 1) {
                System.out.println("Dynamite exploded and you lose 3 lives!");
                playerLives.put(playerName, playerLives.get(playerName) - 3);
                discardedCards.add("Dynamite");
                playerCards.get(playerName).remove("Dynamite");
                return;
            } else {
                System.out.println("Dynamite didn't explode.");
                int index = (new ArrayList<>(playerLives.keySet()).indexOf(playerName) + 1) % playerLives.keySet().size(); //FIX
                String nextPlayer = new ArrayList<>(playerLives.keySet()).get(index);
                playerCards.get(playerName).remove("Dynamite");
                playerCards.get(nextPlayer).add("Dynamite");
                System.out.println("Dynamite has been passed to " + nextPlayer + ".");
            }
        }

        drawCards(playerName, 2);
        playCards(playerName);
        discardExcessCards(playerName);
    }

    private void showPlayerCards(String playerName) {
        List<String> cards = playerCards.get(playerName);
        for (int i = 0; i < cards.size(); i++) {
            System.out.println((i + 1) + ". " + cards.get(i));
        }
    }

    private void initializeDeck() {
        deck.addAll(Collections.nCopies(2, "Barrel"));
        deck.add("Dynamite");
        deck.addAll(Collections.nCopies(3, "Jail"));
        deck.addAll(Collections.nCopies(30, "Bang"));
        deck.addAll(Collections.nCopies(15, "Missed"));
        deck.addAll(Collections.nCopies(5, "Beer"));
        deck.addAll(Collections.nCopies(3, "Panic"));
        deck.addAll(Collections.nCopies(3, "CatBalou"));
        deck.addAll(Collections.nCopies(3, "Duel"));
        deck.addAll(Collections.nCopies(2, "GeneralStore"));
    }
    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private void dealCards() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine();

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Enter the name of player " + i + ": ");
            String playerName = scanner.nextLine();

            playerLives.put(playerName, STARTING_LIVES);
            playerCards.put(playerName, new ArrayList<>());

            drawCards(playerName, 5);
        }
    }
    private void drawCards(String playerName, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            if (deck.isEmpty()) {
                deck.addAll(discardedCards);
                discardedCards.clear();
                shuffleDeck();
            }

            String drawnCard = deck.remove(deck.size() - 1);
            CardType cardType = Card.getCardType(drawnCard);
            if (cardType != null) {
                playerCards.get(playerName).add(drawnCard);
            }
        }
    }
    private void playCards(String playerName) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;

        while (!done && !playerCards.get(playerName).isEmpty()) {
            System.out.println(playerName + ", choose a card to play or enter 0 to pass:");
            int choice = scanner.nextInt();

            if (choice == 0) {
                done = true;
            } else {
                String cardName = playerCards.get(playerName).get(choice - 1);
                Card.CardEffect cardEffect = Card.getCardEffect(cardName);

                if (cardEffect != null) {
                    cardEffect.execute(this, playerName);
                    playerCards.get(playerName).remove(cardName);
                    discardedCards.add(cardName);
                } else {
                    System.out.println("Invalid card choice.");
                }
            }
        }
    }
    public void attackPlayer(String attacker) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(attacker + ", choose a player to attack:");
        List<String> otherPlayers = new ArrayList<>(playerLives.keySet());
        otherPlayers.remove(attacker);
        for (int i = 0; i < otherPlayers.size(); i++) {
            System.out.println((i + 1) + ". " + otherPlayers.get(i));
        }
        int targetIndex = scanner.nextInt() - 1;
        String target = otherPlayers.get(targetIndex);

        if (playerCards.get(target).contains("Missed")) {
            System.out.println(target + " plays a Missed!");
            playerCards.get(target).remove("Missed");
            discardedCards.add("Missed");
        } else {
            playerLives.put(target, playerLives.get(target) - 1);
            System.out.println(target + " has been hit!");
            if (playerLives.get(target) <= 0) {
                playerLives.remove(target);
                System.out.println(target + " has been eliminated!");
            }
        }
    }
    public void removeRandomCardFromOpponent(String playerName) {
        // Choose a random opponent
        List<String> otherPlayers = new ArrayList<>(playerLives.keySet());
        otherPlayers.remove(playerName);
        String target = otherPlayers.get(new Random().nextInt(otherPlayers.size()));

        // Remove a random card from the opponent
        if (!playerCards.get(target).isEmpty()) {
            String card = playerCards.get(target).remove(new Random().nextInt(playerCards.get(target).size()));
            discardedCards.add(card);
            System.out.println(playerName + " plays Cat Balou! Removed " + card + " from " + target);
        } else {
            System.out.println(playerName + " plays Cat Balou! But " + target + " has no cards to remove.");
        }
    }
    public void increasePlayerLives(String playerName, int amount) {
        playerLives.put(playerName, Math.min(playerLives.get(playerName) + amount, STARTING_LIVES));
    }
    public void indiansAttack(String attacker) {
        for (String playerName : playerLives.keySet()) {
            if (!playerName.equals(attacker)) {
                if (playerCards.get(playerName).contains("Bang")) {
                    System.out.println(playerName + " defends against Indians with a Bang!");
                    playerCards.get(playerName).remove("Bang");
                    discardedCards.add("Bang");
                } else {
                    playerLives.put(playerName, playerLives.get(playerName) - 1);
                    System.out.println(playerName + " is hit by Indians!");
                    if (playerLives.get(playerName) <= 0) {
                        playerLives.remove(playerName);
                        System.out.println(playerName + " has been eliminated!");
                    }
                }
            }
        }
    }
    public void drawStagecoachCards(String player) {
        drawCards(player, 2);
    }
    public void putPlayerInJail(String playerName) {
        playerCards.get(playerName).add("Jail");
    }
    public void placeDynamite(String playerName) {
        playerCards.get(playerName).add("Dynamite");
    }

    public void addBarrel(String playerName) {
        playerCards.get(playerName).add("Barrel");
    }
    private void discardExcessCards(String playerName) {
        List<String> cards = playerCards.get(playerName);
        while (cards.size() > playerLives.get(playerName)) {
            System.out.println("You have too many cards. Choose a card to discard:");
            showPlayerCards(playerName);

            Scanner scanner = new Scanner(System.in);
            int cardIndex = scanner.nextInt() - 1;

            discardedCards.add(cards.remove(cardIndex));
        }
    }

    public static void main(String[] args) {
        BangGame bangGame = new BangGame();
        bangGame.play();
    }
}
