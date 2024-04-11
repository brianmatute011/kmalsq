package mlix.global.kmal.model.criteria;
import  mlix.global.kmal.model.types.Condition;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a unary criteria in the rules.
 */
@Getter
@Setter
@NonNull
public class UnaryCriteria extends BaseCriteria{
    /**
     * NOT Operator
     */
    @Setter
    @NonNull
    private Condition condition;

    /**
     * The {@link BaseCriteria} of the right in the query.
     */
    @Setter
    @NonNull
    private BaseCriteria right;

    /**
     * Returns a string representation of the unary criteria.
     * @return string representation of the unary criteria.
     */
    public String toString (){
        return toStringRecursiveUnary(this);
    }

    /**
     * Returns a string representation of the unary criteria.
     * @param node the node to be converted to string.
     * @return string representation of the unary criteria.
     */
    private String toStringRecursiveUnary(BaseCriteria node){
        if (node instanceof BaseQuery)
            return node.toString();
        if (node instanceof BinaryCriteria){
            BinaryCriteria binaryNode = (BinaryCriteria) node;
            return binaryNode.toString();
        }
        if (node instanceof UnaryCriteria){
            UnaryCriteria unaryNode = (UnaryCriteria) node;
            String rightString = toStringRecursiveUnary(unaryNode.getRight());
            return "(" + unaryNode.getCondition() + " " + rightString + ")";
        }

        return "(?・_・) Unexpected node type";
    }
}
