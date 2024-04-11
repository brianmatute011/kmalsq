package mlix.global.kmal.model.value;

import lombok.NonNull;

/**
 * Represents a boolean value in the rules.
 */
public class BooleanValue extends Value<Boolean>{

    /**
     * Constructor.
     * @param value boolean value.
     */
    public BooleanValue(@NonNull Boolean value) {
        super("boolean", value, null, null, null);
    }
}
