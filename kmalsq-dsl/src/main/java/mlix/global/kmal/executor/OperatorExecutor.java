package mlix.global.kmal.executor;

import lombok.NonNull;
import mlix.global.kmal.exception.KmalsqException;
import mlix.global.kmal.model.criteria.BaseCriteria;
import mlix.global.kmal.model.criteria.BaseQuery;
import mlix.global.kmal.model.criteria.BinaryCriteria;
import mlix.global.kmal.model.criteria.UnaryCriteria;



import mlix.global.kmal.model.criteria.BaseQuery;
import mlix.global.kmal.model.types.Condition;
import mlix.global.kmal.model.value.DateValue;
import mlix.global.kmal.model.value.NumberValue;
import mlix.global.kmal.model.value.Value;
import mlix.global.kmal.parser.KmalsqParser;
import mlix.global.kmal.parser.KmalsqLexer;
import mlix.global.kmal.parser.listener.KmalsqParserErrorListener;
import mlix.global.kmal.transpiler.KamalAST;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;


import mlix.global.kmal.model.value.Value;
import mlix.global.kmal.model.value.DateValue;
import mlix.global.kmal.model.value.NumberValue;
import mlix.global.kmal.model.value.StringValue;
import mlix.global.kmal.model.value.TermValue;
import mlix.global.kmal.model.value.BooleanValue;
import org.antlr.v4.runtime.RuleContext;

import java.util.List;
//Replace all next doc comment with english version...

public abstract class OperatorExecutor<T> {
    private static final String VALUE_ERROR = "Error!! Valor no soportado para el operador ";

    /**
     * Method to form the condition (and,or) between two queries
     *
     * @param operator
     *            Condition (and,or) to use
     * @param value1
     *            First query
     * @param value2
     *            Second query
     * @return <code>[T]</code> Query for the condition (and,or)
     */
    public abstract T condition(@NonNull Condition operator, @NonNull T value1, @NonNull T value2);


    /**
     * Method to form the query with unary operators eg. (not)
     *
     * @param operator
     *            Condition (not) to use [validated]
     * @param rValue
     *            query to the right of the operator
     * @return <code>[T]</code> Query for the condition (not)
     */
    public abstract T ucondition(@NonNull Condition operator, @NonNull T rValue, T rb2Value);


    // +++++++++++++++++++++++++++++++
    //
    // Generic operators
    //
    // +++++++++++++++++++++++++++++++

    /**
     * Method to form the query for the "equal" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @param all
     *            Specifies whether it should be fulfilled in all properties or in some.
     * @return <code>[T]</code> Query for the "equal" operator
     */
    public abstract T equalTo(List<String> fields, @NonNull Value<?> value, boolean all);

    /**
     * Method to form the query for the "different" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @param all
     *            Specifies whether it should be fulfilled in all properties or in some.
     * @return <code>[T]</code> Query for the "different" operator
     */
    public abstract T different(List<String> fields, @NonNull Value<?> value, boolean all);

    // +++++++++++++++++++++++++
    //
    //  String operators
    //
    // +++++++++++++++++++++++++


    /**
     * Method to form the query for the "starts with" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "starts with" operator
     */
    public abstract T startsWith(List<String> fields, @NonNull Value<?> value, boolean all);

    /**
     * Method to form the query for the "does not start with" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "does not start with" operator
     */
    public abstract T doesNotStartsWith(List<String> fields, @NonNull Value<?> value, boolean all);


    /**
     * Method to form the query for the "ends with" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "ends with" operator
     */
    public abstract T endsWith(List<String> fields, @NonNull Value<?> value, boolean all);


    /**
     * Method to form the query for the "does not end with" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "does not end with" operator
     */
    public abstract T doesNotEndsWith(List<String> fields, @NonNull Value<?> value, boolean all);


    /**
     * Method to form the query for the "contains" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "contains" operator
     */
    public abstract T contains(List<String> fields, @NonNull Value<?> value, boolean all);


    /**
     * Method to form the query for the "does not contain" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "does not contain" operator
     */
    public abstract T doesNotContains(List<String> fields, @NonNull Value<?> value, boolean all);

    // +++++++++++++++++++++++++
    //
    //  Number operators
    //
    // +++++++++++++++++++++++++


    /**
     * Method to form the query for the "greater than" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "greater than" operator
     */
    public abstract T greaterThan(List<String> fields, @NonNull Value<?> value, boolean all);


    /**
     * Method to form the query for the "greater than or equal to" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "greater than or equal to" operator
     */
    public abstract T greaterThanEquals(List<String> fields, @NonNull Value<?> value, boolean all);

    /**
     * Method to form the query for the "less than" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "less than" operator
     */
    public abstract T lessThan(List<String> fields, @NonNull Value<?> value, boolean all);

    /**
     * Method to form the query for the "less than or equal to" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Value for the field to query
     * @return <code>[T]</code> Query for the "less than or equal to" operator
     */
    public abstract T lessThanEquals(List<String> fields, @NonNull Value<?> value, boolean all);

    /* Method to form the query for the "in the range" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Data type with the start and end values
     * @return <code>[T]</code> Query for the "in the range" operator
     */
    public abstract T range(List<String> fields, @NonNull Value<?> value, boolean all);

    /* Method to form the query for the "out of range" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Data type with the start and end values
     * @return <code>[T]</code> Query for the "out of range" operator
     */
    public abstract T rangeOut(List<String> fields, @NonNull Value<?> value, boolean all);

    /* Method to form the query for the "with all values" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param values
     *            Values for the field to query
     * @return <code>[T]</code> Query for the "with all values" operator
     */
    public abstract T all(List<String> fields, @NonNull Value<?> values, boolean all);

    /* Method to form the query for the "with any of the values" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param values
     *            Values for the field to query
     * @return <code>[T]</code> Query for the "with any of the values" operator
     */
    public abstract T any(List<String> fields, @NonNull Value<?> values, boolean all);

    /* Method to form the query for the "with none of the values" operator
     *
     * @param fields
     *            Properties of the field to query
     * @param values
     *            Values for the field to query
     * @return <code>[T]</code> Query for the "with none of the values" operator
     */
    public abstract T none(List<String> fields, @NonNull Value<?> values, boolean all);


    /* Method to form the query for the "is equal" operator for strings
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Values for the field to query
     * @return <code>[T]</code> Query for the "is equal" operator for strings
     */
    public abstract T is(List<String> fields, @NonNull Value<?> value, boolean all);

    /* Method to form the query for the "is not equal" operator for strings
     *
     * @param fields
     *            Properties of the field to query
     * @param value
     *            Values for the field to query
     * @return <code>[T]</code> Query for the "is not equal" operator for strings
     */
    public abstract T isNot(List<String> fields, @NonNull Value<?> value, boolean all);

    /* Convert the generic query to a specific one for each database.
     * @param query The generic query.
     * @return <code>[T]</code> Specific database query.
     */


    protected final T parseQuery(String query) {
        StringBuilder queryString = new StringBuilder(query);

        if (!query.endsWith(";")) {
            queryString.append(";");
        }

        KmalsqLexer lexer = new KmalsqLexer(CharStreams.fromString(queryString.toString()));
        lexer.removeErrorListeners();
        lexer.addErrorListener(new KmalsqParserErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        KmalsqParser parser = new KmalsqParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(new KmalsqParserErrorListener());

        RuleContext tree = parser.simple_filter();
        KamalAST visitor = new KamalAST();
        BaseCriteria filter = (BaseCriteria) visitor.visit(tree);

        return obtainFilter(filter);
    }


    protected List<String> getFields(BaseQuery filter) {
        return filter.getFields();
    }


    /* Obtains the filter from the generic filter. Checks if it is a simple filter or if it has conditionals
    */
    private T obtainFilter(BaseCriteria criteria) {
        if (criteria instanceof BaseQuery) {
            System.out.println("[!] Pass by Obtain filter getting translate ...");
            return translate(criteria);
        }
        else if (criteria instanceof UnaryCriteria){
            System.out.println("[!!] Temporal Bypassing (ﾟοﾟ人)) [detecting " + UnaryCriteria.class.toString() + "]");
            UnaryCriteria unarySimpleCriteria = (UnaryCriteria) criteria;
            BaseCriteria rightSimpleCriteria = unarySimpleCriteria.getRight();

            if (rightSimpleCriteria instanceof BaseQuery || rightSimpleCriteria instanceof UnaryCriteria)
                return ucondition(unarySimpleCriteria.getCondition(), obtainFilter(rightSimpleCriteria), null);
            else{
                BinaryCriteria binarySimpleCriteria = (BinaryCriteria) rightSimpleCriteria;
                return ucondition(binarySimpleCriteria.getCondition(), obtainFilter(binarySimpleCriteria.getLeft()), obtainFilter(binarySimpleCriteria.getRight()));
            }
//                return translate(rightSimpleCriteria); //Analyze rightCriterion as UnaryFilter
//            return translate(criteria);
        }
        else {
            BinaryCriteria simpleFilter = (BinaryCriteria) criteria;
            return condition(
                    simpleFilter.getCondition(),
                    obtainFilter(simpleFilter.getLeft()),
                    obtainFilter(simpleFilter.getRight()
                    ));
        }
    }


    /* Obtains the query from the generic filter. Checks if it is a simple query or if it has conditionals */
    private T translate(@NonNull BaseCriteria filter) {
        BaseQuery baseFilter = (BaseQuery) filter;
        if (baseFilter.getValue().getValue() != null) {
            return simpleValue(baseFilter);
        } else if (baseFilter.getValue().getListValue() != null) {
            return listOperators(baseFilter);
        } else {
            return rangeOperators(baseFilter);
        }
    }

    // Operator for simple values
    private T simpleValue(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();

        if (value instanceof TermValue || value instanceof StringValue) {
            return stringOperators(filter);
        } else if (value instanceof NumberValue || value instanceof DateValue) {
            return numbersOperators(filter);
        } else {
            return boolOperators(filter);
        }
    }

    // Operator for string values
    private T stringOperators(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();

        // Igualdad/diferencia
        if (value instanceof TermValue) {
            switch (filter.getOperator()) {
                case IS:
                    return is(filter.getFields(), filter.getValue(), filter.isAll());
                case IS_NOT:
                    return isNot(filter.getFields(), filter.getValue(), filter.isAll());
            }
        } else if (value instanceof StringValue) {
            switch (filter.getOperator().getOperator()) {
                case "==":
                    return equalTo(filter.getFields(), filter.getValue(), filter.isAll());
                case "!=":
                    return different(filter.getFields(), filter.getValue(), filter.isAll());
            }
        }

        return commonStringsOperators(filter);
    }

    // Operator for common string values
    private T commonStringsOperators (@NonNull BaseQuery filter) {
        switch (filter.getOperator()) {
            case STARTS_WITH:
                return startsWith(filter.getFields(), filter.getValue(), filter.isAll());
            case DOES_NOT_STARTS_WITH:
                return doesNotStartsWith(filter.getFields(), filter.getValue(), filter.isAll());
            case ENDS_WITH:
                return endsWith(filter.getFields(), filter.getValue(), filter.isAll());
            case DOES_NOT_END_WITH:
                return doesNotEndsWith(filter.getFields(), filter.getValue(), filter.isAll());
            case CONTAINS:
                return contains(getFields(filter), filter.getValue(), filter.isAll());
            case DOES_NOT_CONTAINS:
                return doesNotContains(filter.getFields(), filter.getValue(), filter.isAll());
            default:
                throw new KmalsqException("Valor no soportado para el operador " +
                        filter.getOperator().getOperator());
        }
    }

    // Operator for number values
    private T numbersOperators(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();

        switch (filter.getOperator().getOperator()) {
            case "==":
                return value instanceof DateValue ?
                        range(filter.getFields(), new DateValue(((DateValue) value).getDateStart(), ((DateValue) value).getDateEnd()),
                                filter.isAll()) :
                        equalTo(filter.getFields(), value, filter.isAll());
            case "!=":
                return value instanceof DateValue ?
                        rangeOut(filter.getFields(), new DateValue(((DateValue) value).getDateStart(), ((DateValue) value).getDateEnd()),
                                filter.isAll()) :
                        different(filter.getFields(), value, filter.isAll());
            case ">":
                return value instanceof DateValue ?
                        greaterThan(filter.getFields(),  new NumberValue(((DateValue) value).getDateEnd().doubleValue()), filter.isAll()) :
                        greaterThan(filter.getFields(), value, filter.isAll());
            case ">=":
                return value instanceof DateValue ?
                        greaterThanEquals(filter.getFields(),  new NumberValue(((DateValue) value).getDateStart().doubleValue()), filter.isAll()) :
                        greaterThanEquals(filter.getFields(), value, filter.isAll());
            case "<":
                return value instanceof DateValue ?
                        lessThan(filter.getFields(),  new NumberValue(((DateValue) value).getDateStart().doubleValue()), filter.isAll()) :
                        lessThan(filter.getFields(), value, filter.isAll());
            case "<=":
                return value instanceof DateValue ?
                        lessThanEquals(filter.getFields(),  new NumberValue(((DateValue) value).getDateEnd().doubleValue()), filter.isAll()) :
                        lessThanEquals(filter.getFields(), value, filter.isAll());
            default:
                throw new KmalsqException(VALUE_ERROR +
                        filter.getOperator().getOperator());
        }
    }

    // Operator for boolean values
    private T boolOperators(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();

        switch (filter.getOperator().getOperator()) {
            case "==":
                return equalTo(filter.getFields(), value, filter.isAll());
            case "!=":
                return different(filter.getFields(), value, filter.isAll());
            default:
                throw new KmalsqException(VALUE_ERROR +
                        filter.getOperator().getOperator());
        }
    }

    // Operators for range values
    private T rangeOperators(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();

        switch (filter.getOperator()) {
            case RANGE:
                return range(filter.getFields(), value, filter.isAll());
            case RANGOUT:
                return rangeOut(filter.getFields(), value, filter.isAll());
            default:
                throw new KmalsqException(VALUE_ERROR +
                        filter.getOperator().getOperator());
        }
    }

    // Operator for list values
    private T listOperators(@NonNull BaseQuery filter) {
        Value<?> value = filter.getValue();
        List<String> fileds = getFields(filter);

        switch (filter.getOperator()) {
            case ALL:
                return all(filter.getFields(), value, filter.isAll());
            case ANY:
                return any(filter.getFields(), value, filter.isAll());
            case NONE:
                return none(filter.getFields(), value, filter.isAll());
            default:
                throw new KmalsqException(VALUE_ERROR +
                        filter.getOperator().getOperator());
        }
    }
}
