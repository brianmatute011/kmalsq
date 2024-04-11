package mlix.global.kmal.model.value;
import java.util.List;

public class TermValue extends Value<String>{
    public TermValue(String value) {
        super("term", value, null, null, null);
    }

    public TermValue(List<String> listValue) {
        super("term", null, null, null, listValue);
    }

    @Override
    public String toString(){
        return this.value;
    }
}
