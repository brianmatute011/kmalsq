package mlix.global.kmal.model.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import mlix.global.kmal.model.operator.Operator;
import mlix.global.kmal.model.value.Value;
import java.util.List;

@AllArgsConstructor
@Builder
public class BaseQuery extends BaseCriteria{
    /**
     * The filter operator to apply. See {@link Operator}
     *
     */
    @Setter
    private Operator operator;

    /**
     * The value to filter. See {@link Value}
     */
    @Setter
    private Value<?> value;

    /**
     * The fields to filter.
     */
    @Setter
    private List<String> fields;

    /**
     * If the filter must be met for all fields, or any of them.
     */
    @Setter
    private boolean all;


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("BaseQuery{ ");
        builder.append("operator: ").append(operator).append(", ");
        builder.append("value: ").append(value).append(", ");
        builder.append("fields: [");
        builder.append(fields.get(0));

        for (int fieldIdx = 1; fieldIdx < fields.size(); fieldIdx++)
            builder.append(", ").append(fields.get(fieldIdx));

        builder.append("], ");
        builder.append("all: ").append(all);
        builder.append(" }");

        return builder.toString();
    }
}
