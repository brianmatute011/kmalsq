package mlix.global.executor;
import java.util.*;
import java.util.stream.Collectors;
import mlix.global.exception.ElasticSearchKmalException;
import mlix.global.kmal.exception.KmalsqException;
import mlix.global.kmal.executor.OperatorExecutor;
import mlix.global.kmal.model.types.Condition;
import mlix.global.kmal.model.value.TermValue;
import mlix.global.kmal.model.value.Value;
import org.elasticsearch.index.query.QueryBuilder;
import lombok.NonNull;
import org.elasticsearch.index.query.*;

public class ElasticSearchKmalOperatorExecutor extends OperatorExecutor<QueryBuilder> {
    @Override
    public QueryBuilder condition(Condition condition, QueryBuilder left, QueryBuilder right) {
        System.out.println("Current info... pass by 'condition' method");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

        if (condition.equals(Condition.AND)) {
            queryBuilder.must(left);
            queryBuilder.must(right);
            System.out.println("Current info... pass by 'condition' method AND");
        }
        else{
            System.out.println("Current info... pass by 'condition' method OR");
            queryBuilder.should(left);
            queryBuilder.should(right);
        }
        System.out.println("[!] (~‾▿‾)~ Return queryBuilder from 'condition' method");
        return queryBuilder;
    }

    @Override
    public QueryBuilder ucondition(@NonNull Condition condition, @NonNull QueryBuilder rexpr, QueryBuilder lr_expr) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (condition.equals(Condition.NOT) && lr_expr == null){
            System.out.println("[!] (~‾▿‾)~ Get Simple NOT Conditions from ElasticOPExec");
            queryBuilder.should(rexpr);
        }
        else {
            System.out.println("[!] ╰(･ ᗜ ･)-> Get NOT BinaryConditions from ElasticOPExec");
            System.out.println("[!]〈(•ˇ‿ˇ•)-→ Current info... pass by 'condition' method <<" + condition.getCondition() + ">> on uncondition");
            queryBuilder = (BoolQueryBuilder) condition(Condition.valueOf(condition.getCondition()), rexpr, lr_expr);
        }
        return QueryBuilders.boolQuery().mustNot(queryBuilder);
    }

    @Override    public QueryBuilder contains(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'contains' method");
        if (fields.isEmpty()) {
            String query = Arrays.stream(((String) value.getValue()).split(" ")).map(s -> "*" + s + "*").collect(Collectors.joining(" "));
            return QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(query));
        } else {
            return wildcardQuery("*" + value.toString() + "*", fields, all);
        }
    }

    @Override
    public QueryBuilder different(List<String> fields, Value<?> value, boolean all) {

        System.out.println("Current info... pass by 'different' method");
        return QueryBuilders.boolQuery().mustNot(equalTo(fields, value, all));
    }

    @Override
    public QueryBuilder doesNotContains(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'doesNotContains' method");
        return QueryBuilders.boolQuery().mustNot(contains(fields, value, all));
    }

    @Override
    public QueryBuilder doesNotEndsWith(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'doesNotEndWith' method");
        return QueryBuilders.boolQuery().mustNot(endsWith(fields, value, all));
    }

    @Override
    public QueryBuilder doesNotStartsWith(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'doesNotStartWith' method");


        return QueryBuilders.boolQuery().mustNot(startsWith(fields, value, all));
    }

    @Override
    public QueryBuilder endsWith(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'endsWith' method");
        if (fields.isEmpty()) {
            String query = Arrays.stream(((String) value.getValue()).split(" ")).map(s -> "*" + s).collect(Collectors.joining(" "));
            return QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(query));
        }
        return wildcardQuery("*" + value.toString(), fields, all);
    }

    @Override
    public QueryBuilder equalTo(List<String> fields, Value<?> value, boolean all) {
        Object val = value.getValue();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.matchQuery(f, val)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        } else {
            return all
                    ? QueryBuilders.multiMatchQuery(val).operator(Operator.AND)
                    : QueryBuilders.multiMatchQuery(val);
        }
    }

    @Override
    public QueryBuilder greaterThan(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'greaterThan' method");
        Object val = value.getValue();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.rangeQuery(f).gt(val)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe todos los campos para el operador GREATER_THAN.");
        }
    }

    @Override
    public QueryBuilder greaterThanEquals(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'greaterThanEquals' method");
        Object val = value.getValue();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.rangeQuery(f).gte(val)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe especificar todos los campos para el operador GREATER_THAN_EQUALS.");
        }
    }

    @Override
    public QueryBuilder is(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'is' method");
        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.regexpQuery(f, value.toString())).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe todos los campos para el operador IS.");
        }
    }

    @Override
    public QueryBuilder isNot(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'isNot' method");
        if (fields.isEmpty()) {
            throw new ElasticSearchKmalException("Error. Debe especificar todos los campos a filtrar para el operador IS_NOT");
        }
        return QueryBuilders.boolQuery().mustNot(is(fields, value, all));
    }

    @Override
    public QueryBuilder lessThan(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'lessThan' method");
        Object val = value.getValue();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.rangeQuery(f).lt(val)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe todos los campos para el operador LESS_THAN.");
        }
    }

    @Override
    public QueryBuilder lessThanEquals(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'lessThaneEquals' method");
        Object val = value.getValue();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.rangeQuery(f).lte(val)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe todos los campos para el operador LESS_THAN_EQUALS.");
        }
    }

    @Override
    public QueryBuilder range(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'range' method");
        Object fromVal = value.getFrom();
        Object toVal = value.getTo();

        if (!fields.isEmpty()) {
            return fields.stream().map(f -> (QueryBuilder) QueryBuilders.rangeQuery(f).from(fromVal).includeLower(true).to(toVal).includeUpper(true)).
                    reduce(QueryBuilders.boolQuery(), (acc, element) -> all ? ((BoolQueryBuilder)acc).must(element): ((BoolQueryBuilder)acc).should(element));
        }
        else {
            throw new ElasticSearchKmalException("Error. Debe todos los campos a consultar para el operador RANGE.");
        }
    }

    @Override
    public QueryBuilder rangeOut(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'rangeOut' method");

        if (fields.isEmpty()) {
            throw new ElasticSearchKmalException("Error. Debe especificar todos los campos a filtrar para el operador RANGE_OUT");
        }
        return QueryBuilders.boolQuery().mustNot(range(fields, value, all));
    }

    @Override
    public QueryBuilder all(List<String> fields, @NonNull Value<?> values, boolean all) {
        System.out.println("Current info... pass by 'all' method");
        BoolQueryBuilder queryBuilderFields = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilderValues = QueryBuilders.boolQuery();
        List<?> listValues = values.getListValue();

        if (!listValues.isEmpty()) {
            if (all) {
                for (String field : fields) {
                    for (Object value : listValues) {
                        if (value instanceof TermValue) {
                            queryBuilderValues = queryBuilderValues
                                    .must(QueryBuilders.termQuery(field, value.toString()));
                        } else {
                            queryBuilderValues = queryBuilderValues
                                    .must(QueryBuilders.matchQuery(field, value.toString()));
                        }
                    }

                    queryBuilderFields = queryBuilderFields.must(queryBuilderValues);
                }

                return queryBuilderFields;
            } else {
                for (Object value : listValues)
                    if (value instanceof TermValue) {
                        queryBuilderValues = queryBuilderValues.must(QueryBuilders.termQuery("*", value.toString()));
                    } else {
                        queryBuilderValues = queryBuilderValues.should(QueryBuilders.multiMatchQuery(value.toString()));
                    }

                return queryBuilderValues;
            }
        } else {
            throw new KmalsqException("Error! Debe especificar todos los campos para el operador ALL");
        }
    }

    @Override
    public QueryBuilder any(List<String> fields, @NonNull Value<?> values, boolean all) {
        System.out.println("Current info... pass by 'any' method");
        BoolQueryBuilder queryBuilderFields = QueryBuilders.boolQuery();
        BoolQueryBuilder queryBuilderValues = QueryBuilders.boolQuery();
        List<?> listValues = values.getListValue();

        if (!listValues.isEmpty()) {
            if (all) {
                for (String field : fields) {
                    for (Object value : listValues)
                        queryBuilderValues = queryBuilderValues
                                .should(QueryBuilders.matchQuery(field, value.toString()));

                    queryBuilderFields = queryBuilderFields.must(queryBuilderValues);
                }

                return queryBuilderFields;
            } else {
                for (String field : fields) {
                    for (Object value : listValues)
                        queryBuilderValues = queryBuilderValues
                                .should(QueryBuilders.matchQuery(field, value.toString()));

                    queryBuilderFields = queryBuilderFields.should(queryBuilderValues);
                }

                return queryBuilderFields;
            }
        } else {
            throw new KmalsqException("Error! Debe especificar todos los campos para el operador ANY");
        }
    }

    @Override
    public QueryBuilder none(List<String> fields, @NonNull Value<?> values, boolean all) {
        System.out.println("Current info... pass by 'none' method");
        if (!fields.isEmpty()) {
            return QueryBuilders.boolQuery().mustNot(any(fields, values, all));
        } else {
            throw new KmalsqException("Error! Debe especificar todos los campos para el operador NONE");
        }

    }

    @Override
    public QueryBuilder startsWith(List<String> fields, Value<?> value, boolean all) {
        System.out.println("Current info... pass by 'startWith' method");
        if (fields.isEmpty()) {
            String query = Arrays.stream(((String) value.getValue()).split(" ")).map(s -> "*" + s + "*").collect(Collectors.joining(" "));
            return QueryBuilders.boolQuery().must(QueryBuilders.queryStringQuery(query));
        } else {
            return wildcardQuery(value.toString() + "*", fields, all);
        }
    }

    protected QueryBuilder wildcardQuery(String query, List<String> fields, boolean all) {
        System.out.println("Current info... pass by 'wildcardQuery' method");
        if (all) {
            return fields.stream().map(f ->
                    (QueryBuilder) QueryBuilders.wildcardQuery(f, query)).reduce(QueryBuilders.boolQuery(), (acc, element) -> ((BoolQueryBuilder)acc).must(element));
        } else {
            return fields.stream().map(f ->
                    (QueryBuilder) QueryBuilders.wildcardQuery(f, query)).reduce(QueryBuilders.boolQuery(), (acc, element) -> ((BoolQueryBuilder)acc).should(element));
        }
    }
}
