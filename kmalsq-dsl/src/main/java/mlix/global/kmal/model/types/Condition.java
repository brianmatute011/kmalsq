package mlix.global.kmal.model.types;

/**
 * Enumerates the conditions that can be used in the rules.
 * <p>
 * The conditions are:
 * <ul>
 * <li>AND</li>
 * <li>OR</li>
 * <li>NOT</li>
 * </ul>
 * </p>
 */
public enum Condition {
    AND("AND"),
    OR("OR"),
    NOT("NOT");

    /**
     * The name of the condition.
     */
    private String condition;

    /**
     * Constructor.
     * @param condition The name of the condition.
     */
    private Condition(String condition) {
        this.condition = condition;
    }

    /**
     * Get the name of the condition.
     * @return The name of the condition.
     */
    public String getCondition() {
        return this.condition;
    }
}