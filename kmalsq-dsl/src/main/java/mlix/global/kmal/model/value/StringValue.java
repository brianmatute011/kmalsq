package mlix.global.kmal.model.value;
import lombok.NonNull;

import java.util.List;

public class StringValue extends Value<String> {
    public StringValue(@NonNull String value) {
        super("string", value, null, null, null);
    }
    public StringValue(@NonNull List<String> value) {
        super("string", null, null, null, value);
    }

    @Override
    public String toString() {
        return this.getValue();
    }
}
