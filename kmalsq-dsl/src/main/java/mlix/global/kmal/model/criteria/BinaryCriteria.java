package mlix.global.kmal.model.criteria;
import  mlix.global.kmal.model.types.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a binary criteria in the rules.
 */
@Getter
@AllArgsConstructor
public class BinaryCriteria extends BaseCriteria {
    /**
     * The logical operator (AND or OR) that it represents.
     */
    @Setter
    @NonNull
    private Condition condition;

    /**
     * The left {@link BaseCriteria} in the query.
     */
    @Setter
    @NonNull
    private BaseCriteria left;

    /**
     * The right {@link BaseCriteria} in the query.
     */
    @Setter
    @NonNull
    private BaseCriteria right;

    /**
     * Returns a string representation of the binary criteria.
     * @return string representation of the binary criteria.
     */
    public String toString (){
        return toStringRecursive(this);
    }

    /**
     * Returns a string representation of the binary criteria.
     * @param node the node to be converted to string.
     * @return string representation of the binary criteria.
     */
    private String toStringRecursive(BaseCriteria node){
        if (node instanceof BaseQuery)
            return node.toString();
        if (node instanceof BinaryCriteria){
            BinaryCriteria binaryNode = (BinaryCriteria) node;
            String leftString = toStringRecursive(binaryNode.getLeft());
            String rightString = toStringRecursive(binaryNode.getRight());
            return "&(" + leftString + " " + binaryNode.getCondition() + " " + rightString + ")";
        }
        if (node instanceof UnaryCriteria)
            return node.toString();
        return "(?・_・) Unexpected node type";
    }
}
