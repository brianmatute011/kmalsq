package mlix.global.kmal.model.value;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;

/**
 * Represents a value in the rules.
 * <p>
 * The value can be a single value, a range of values or a list of values.
 * </p>
 */
public abstract class Value<T> {

    /**
     * Type of the value.
     */
    @Getter
    @Setter
    String type;

    /**
     * Real value
     */
    @Getter
    @Setter
    T value;


    /**
     * from clause
     */
    @Getter
    @Setter
    T from;

    /**
     * If it is not null, the value is a range and this is its end.
     */
    @Getter
    @Setter
    T to;

    /**
     * If it is not null, the value is a list of values.
     */
    @Getter
    @Setter
    List<T> listValue;

    /**
     * Constructor.
     * @param type Type of the value.
     * @param value Real value.
     * @param from If it is not null, the value is a range and this is its start.
     * @param to If it is not null, the value is a range and this is its end.
     * @param listValue If it is not null, the value is a list of values.
     */
    public Value(@NonNull String type, T value, T from, T to, List<T> listValue) {
        this.type = type;
        this.value = value;
        this.from = from;
        this.to = to;
        this.listValue = listValue;
    }
}