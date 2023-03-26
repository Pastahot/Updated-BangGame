public enum CardType {
    BANG,
    MISSED,
    BEER,
    CAT_BALOU,
    STAGECOACH,
    INDIANS,
    JAIL,
    DYNAMITE,
    BARREL;

    public static CardType getCardType(String card) {
        switch (card) {
            case "Bang":
                return BANG;
            case "Missed":
                return MISSED;
            case "Beer":
                return BEER;
            case "Cat Balou":
                return CAT_BALOU;
            case "Stagecoach":
                return STAGECOACH;
            case "Indians":
                return INDIANS;
            case "Jail":
                return JAIL;
            case "Dynamite":
                return DYNAMITE;
            case "Barrel":
                return BARREL;
            default:
                throw new IllegalArgumentException("Invalid card type: " + card);
        }
    }
}
