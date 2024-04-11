package mlix.global.kmal.model.types;

/**
 * Enumerates the symbols that can be used in the fields of the rules.
 * <p>
 * The symbols are:
 * <ul>
 * <li>ALL</li>
 * <li>ANY</li>
 * </ul>
 * </p>
 */
public enum FieldSymbol {
    ALL("^"),
    ANY("|");

    /**
     * Symbol of the quantifier.
     */
    private String symbol;

    /**
     * Constructor.
     * @param symbol Symbol of the quantifier.
     */
    private FieldSymbol(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Get the symbol of the quantifier.
     * @return Symbol of the quantifier.
     */
    public String getSymbol() {
        return this.symbol;
    }
}