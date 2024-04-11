package mlix.global.kmal.model.value;

import lombok.NonNull;

import java.util.List;

public class NumberValue extends Value<Double> {
    public NumberValue(Double from, Double to) {
        super("number", null, from, to, null);
    }

    public NumberValue(@NonNull Double number) {
        super("number", number, null, null, null);
    }
    public NumberValue(@NonNull List<Double> number) {
        super("number", null, null, null, number);
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
}