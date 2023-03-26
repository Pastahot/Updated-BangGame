import java.util.Map;
import java.util.HashMap;

public class Card {

    private static final Map<String, CardType> cardTypes = new HashMap<>();
    private static final Map<String, CardEffect> cardEffects = new HashMap<>();

    static {
        cardTypes.put("Bang", CardType.BANG);
        cardTypes.put("Missed", CardType.MISSED);
        cardTypes.put("Beer", CardType.BEER);
        cardTypes.put("Cat Balou", CardType.CAT_BALOU);
        cardTypes.put("Stagecoach", CardType.STAGECOACH);
        cardTypes.put("Indians", CardType.INDIANS);
        cardTypes.put("Jail", CardType.JAIL);
        cardTypes.put("Dynamite", CardType.DYNAMITE);
        cardTypes.put("Barrel", CardType.BARREL);

        cardEffects.put("Bang", (game, player) -> {
            game.attackPlayer(player);
        });

        cardEffects.put("Missed", (game, player) -> {
            // Missed cards are automatically played when attacked
        });

        cardEffects.put("Beer", (game, player) -> {
            game.increasePlayerLives(player, 1);
        });

        cardEffects.put("Cat Balou", (game, player) -> {
            game.removeRandomCardFromOpponent(player);
        });

        cardEffects.put("Stagecoach", (game, player) -> {
            game.drawStagecoachCards(player);
        });

        cardEffects.put("Indians", (game, player) -> {
            game.indiansAttack(player);
        });

        cardEffects.put("Jail", (game, player) -> {
            game.putPlayerInJail(player);
        });

        cardEffects.put("Dynamite", (game, player) -> {
            game.placeDynamite(player);
        });

        cardEffects.put("Barrel", (game, player) -> {
            game.addBarrel(player);
        });
    }

    public static CardType getCardType(String card) {
        return cardTypes.get(card);
    }

    public static CardEffect getCardEffect(String card) {
        return cardEffects.get(card);
    }

    public interface CardEffect {
        void execute(BangGame game, String player);
    }

}
