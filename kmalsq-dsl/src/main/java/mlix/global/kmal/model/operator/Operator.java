package mlix.global.kmal.model.operator;


/**
 * {@code @brief} Enumerates the operators that can be used in the conditions of the rules.
 * Enumerates the operators that can be used in the conditions of the rules.
 * <p>
 * The operators are divided into some groups:
 * <ul>
 * <li>Operators for terms: IS, IS_NOT</li>
 * <li>Operators for strings and terms: STARTS_WITH, DOES_NOT_STARTS_WITH, ENDS_WITH, DOES_NOT_END_WITH, CONTAINS, DOES_NOT_CONTAINS, EQUAL, NOT_EQUAL</li>
 * <li>Operators for numbers: GREATER_THAN, GREATER_THAN_EQUALS, LESS_THAN, LESS_THAN_EQUALS</li>
 * <li>Operators for ranges: RANGE, RANGOUT</li>
 * <li>Operators for lists: ALL, NONE, ANY</li>
 * </ul>
 * </p>
 */
public enum Operator {
    IS("IS"), IS_NOT("IS_NOT"),
    STARTS_WITH("STARTS_WITH"), DOES_NOT_STARTS_WITH("OES_NOT_STARTS_WITH"), ENDS_WITH("ENDS_WITH"), DOES_NOT_END_WITH("OES_NOT_END_WITH"), // (no) comienza/termina con un valor.
    CONTAINS("CONTAINS"), DOES_NOT_CONTAINS("DOES_NOT_CONTAINS"), // (no) contiene un valor.
    EQUAL("=="), NOT_EQUAL("!="), // (no) es igual a un valor.
    GREATER_THAN(">"), GREATER_THAN_EQUALS(">="), LESS_THAN("<"), LESS_THAN_EQUALS("<="), // (no) es mayor/menor que un valor
    RANGE("RANGE"), RANGOUT("RANGOUT"), // (no) esta en el rango.
    ALL("ALL"), NONE("NONE"), ANY("ANY"); // Contiene todos, algunos o ninguno de los valores

    /**
     * Operator name.
     */
    private final String operator;

    /**
     * Constructor.
     * @param operator Operator name.
     */
    Operator(String operator) {
        this.operator = operator;
    }

    /**
     * Get the operator name.
     * @return Operator name.
     */
    public final String getOperator() {
        return this.operator;
    }
}
