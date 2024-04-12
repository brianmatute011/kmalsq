package mlix.global;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import mlix.global.kmal.model.criteria.BaseQuery;
import mlix.global.kmal.model.criteria.BinaryCriteria;
import mlix.global.kmal.model.criteria.UnaryCriteria;
import mlix.global.kmal.model.operator.Operator;
import mlix.global.kmal.model.types.Condition;
import mlix.global.kmal.model.value.*;

import java.util.stream.*;
import java.util.ArrayList;

import static mlix.global.kmal.utils.KmalUtils.parseQuery;

/**
 * Unit test for simple App.
 */
public class KmalsqTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public KmalsqTest(String testName ) {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite( KmalsqTest.class );
    }
    public void testSimpleQueryString() {
        String query = "^[a,b] IS 'bb';";

        BaseQuery expected = new BaseQuery(Operator.IS, new StringValue("bb"), Stream.of("a", "b").collect(Collectors.toList()), true);
        Object dslParseQuery = parseQuery(query);
        checkUnaryFilter(expected, ((BaseQuery)dslParseQuery));
    }

    public void testSimpleQueryStringWithoutSymbol() {
        String query = "[a,b] IS 'bb';";

        BaseQuery expected = new BaseQuery(Operator.IS, new StringValue("bb"), Stream.of("a", "b").collect(Collectors.toList()), true);
        Object filter = parseQuery(query);
        checkUnaryFilter(expected, ((BaseQuery)filter));
    }

    public void testSimpleQueryNumber() {
        //Custom Query
        String query = "^[a,b] IS 123456;";
        //Translate to expected query
        BaseQuery expected = new BaseQuery(Operator.IS, new NumberValue(123456D), Stream.of("a", "b").collect(Collectors.toList()), true);
        Object filter = parseQuery(query);
        //Verified Query
        checkUnaryFilter(expected, ((BaseQuery)filter));
    }


    public void testSimpleQueryBoolean() {
        //Custom query
        String query = "^[a,b] IS false;";


        //Translate to expected query
        BaseQuery expected = new BaseQuery(Operator.IS, new BooleanValue(false),
                Stream.of("a", "b").collect(Collectors.toList()), true);

        Object filter = parseQuery(query);

        //Check expected query vs custom query
        checkUnaryFilter(expected, ((BaseQuery)filter));
    }

    public void testSimpleQueryDate() {
        String query = "^[a,b] IS 29/09/1994-08:00:00;";

        BaseQuery expected = new BaseQuery(Operator.IS, new DateValue("29/09/1994-08:00:00"),
                Stream.of("a", "b").collect(Collectors.toList()), true);
        Object filter = parseQuery(query);

        assertEquals(expected.getOperator(), ((BaseQuery)filter).getOperator());
        assertEquals(expected.getFields(), ((BaseQuery)filter).getFields());
        assertEquals(expected.getValue().getValue(), ((DateValue)((BaseQuery)filter).getValue()).getValue());
        assertEquals((expected.getValue()).getType(), (((BaseQuery)filter).getValue()).getType());
        assertNull(((DateValue)((BaseQuery)filter).getValue()).getFrom());
        assertNull(((DateValue)((BaseQuery)filter).getValue()).getTo());
        assertEquals(expected.isAll(), ((BaseQuery)filter).isAll());
        assertNotNull(((DateValue)((BaseQuery)filter).getValue()).getValue());
    }

    public void testSimpleQueryTerm() {
        String query = "^* IS T'bb';";

        BaseQuery expected = new BaseQuery(Operator.IS, new TermValue("bb"), new ArrayList<>(), true);
        Object filter = parseQuery(query);
        checkUnaryFilter(expected, ((BaseQuery)filter));
    }

    public void testAllFieldsQuery() {
        String query = "^* IS 'bb';";

        BaseQuery expected = new BaseQuery(Operator.IS, new StringValue("bb"), new ArrayList<>(), true);
        Object filter = parseQuery(query);
        checkUnaryFilter(expected, ((BaseQuery)filter));
    }

    public void testListOfStrings() {
        String query = "^* ALL ['bb', 222, 122121];";

        BaseQuery expected = new BaseQuery(Operator.ALL, new StringValue(Stream.of("bb", "222", "122121").
                collect(Collectors.toList())), new ArrayList<>(), true);
        Object filter = parseQuery(query);
        assertNotNull(((BaseQuery)filter).getValue().getListValue());
        assertEquals(((BaseQuery)filter).getValue().getListValue(), expected.getValue().getListValue());
    }

    public void testListOfNumbers() {
        String query = "^* ALL [222, 122121];";

        BaseQuery expected = new BaseQuery(Operator.ALL, new NumberValue(Stream.of(222d, 122121d).
                collect(Collectors.toList())), new ArrayList<>(), true);
        Object filter = parseQuery(query);
        assertNotNull(((BaseQuery)filter).getValue().getListValue());
        assertEquals(((BaseQuery)filter).getValue().getListValue(), expected.getValue().getListValue());
    }

    public void testListOfTerms() {
        String query = "^* ALL [T'aa', T'bb'];";

        BaseQuery expected = new BaseQuery(Operator.ALL, new TermValue(Stream.of("aa", "bb").
                collect(Collectors.toList())), new ArrayList<>(), true);
        Object filter = parseQuery(query);
        assertNotNull(((BaseQuery)filter).getValue().getListValue());
        assertEquals(((BaseQuery)filter).getValue().getListValue(), expected.getValue().getListValue());
    }

    public void testSimpleQueryNumberRange() {
        String query = "^[a,b] IS FROM 12 TO 14;";

        BaseQuery expected = new BaseQuery(Operator.IS, new NumberValue(12D, 14D), Stream.of("a", "b").collect(Collectors.toList()), true);
        Object filter = parseQuery(query);

        assertEquals(expected.getOperator(), ((BaseQuery)filter).getOperator());
        assertEquals(expected.getFields(), ((BaseQuery)filter).getFields());
        assertNull(((NumberValue)((BaseQuery)filter).getValue()).getValue());
        assertEquals((expected.getValue()).getType(), (((BaseQuery)filter).getValue()).getType());
        assertNotNull(((NumberValue)((BaseQuery)filter).getValue()).getFrom());
        assertNotNull(((NumberValue)((BaseQuery)filter).getValue()).getTo());
        assertEquals(((NumberValue)expected.getValue()).getFrom(), ((NumberValue)((BaseQuery)filter).getValue()).getFrom());
        assertEquals(((NumberValue)expected.getValue()).getTo(), ((NumberValue)((BaseQuery)filter).getValue()).getTo());
        assertEquals(expected.isAll(), ((BaseQuery)filter).isAll());
    }

    public void testSimpleQueryDateRange() {
        String query = "^[a,b] IS FROM 31/08/1995 TO 29/09/1995;";

        BaseQuery expected = new BaseQuery(Operator.IS, new DateValue("31/08/1995", "29/09/1995"), Stream.of("a", "b").collect(Collectors.toList()), true);
        Object filter = parseQuery(query);

        assertEquals(expected.getOperator(), ((BaseQuery)filter).getOperator());
        assertEquals(expected.getFields(), ((BaseQuery)filter).getFields());
        assertNull(((DateValue)((BaseQuery)filter).getValue()).getValue());
        assertEquals((expected.getValue()).getType(), (((BaseQuery)filter).getValue()).getType());
        assertNotNull(((DateValue)((BaseQuery)filter).getValue()).getFrom());
        assertNotNull(((DateValue)((BaseQuery)filter).getValue()).getTo());
        assertEquals(((DateValue)expected.getValue()).getFrom(), ((DateValue)((BaseQuery)filter).getValue()).getFrom());
        assertEquals(((DateValue)expected.getValue()).getTo(), ((DateValue)((BaseQuery)filter).getValue()).getTo());
        assertEquals(expected.isAll(), ((BaseQuery)filter).isAll());
    }

    public void testOneConditionQuery() {
        String query = "^[a,b] IS 'str' AND ^[c] IS 'str';";

        BinaryCriteria expected = new BinaryCriteria(Condition.AND, new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("a", "b").collect(Collectors.toList()), true),
                new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("c").collect(Collectors.toList()), true));
        Object filter = parseQuery(query);

        assertEquals(expected.getCondition(), ((BinaryCriteria)filter).getCondition());
        checkUnaryFilter((BaseQuery) expected.getLeft(), (BaseQuery) (((BinaryCriteria)filter).getLeft()));
        checkUnaryFilter((BaseQuery) expected.getRight(), (BaseQuery) ((BinaryCriteria)filter).getRight());
    }

    public void testNotOperator(){
        String query = "NOT ^[a] IS 'str';";
        String structuralTraceExpected = "(NOT BaseQuery{ operator: IS, value: str, fields: [a], all: true })".replace(" ", "");
        String structuralTraceAvailabe = parseQuery(query).toString().replace(" ", "");
        assertEquals(structuralTraceExpected, structuralTraceAvailabe);
    }

    public void testManyConditionQuery() {
        String query = "^[a,b] IS 'str' AND ^[c] IS 'str' OR |[d] IS 123 AND |[e] IS 'str';";

        String structuralTraceExpected = "" +
                "&(" +
                "&(" +
                "BaseQuery{ operator: IS, value: str, fields: [a, b], all: true } " +
                "AND " +
                "BaseQuery{ operator: IS, value: str, fields: [c], all: true }" +
                ") " +

                "OR " +

                "&(" +
                "BaseQuery{ operator: IS, value: 123.0, fields: [d], all: false } " +
                "AND " +
                "BaseQuery{ operator: IS, value: str, fields: [e], all: false }" +
                ")" +
                ")";

        //Normalize
        String structuralTraceAvailable = parseQuery(query).toString().replace(" ", "");;
        structuralTraceExpected = structuralTraceExpected.replace(" ", "");


        assertEquals(structuralTraceExpected, structuralTraceAvailable);
    }

    public void testManyConditionsWithNotOperator(){

        String query = "^[a,b] IS 'str' AND ^[c] IS 'str' OR NOT (|[d] IS 123 AND |[e] IS 'str');";
        String structuralTraceExpected =
                "&(" +
                        "&(" +
                        "BaseQuery{ operator: IS, value: str, fields: [a, b], all: true } " +
                        "AND " +
                        "BaseQuery{ operator: IS, value: str, fields: [c], all: true }" +
                        ") " +
                        "OR " +
                        "(NOT &(" +
                        "BaseQuery{ operator: IS, value: 123.0, fields: [d], all: false } " +
                        "AND " +
                        "BaseQuery{ operator: IS, value: str, fields: [e], all: false }" +
                        "))" +
                        ")";
        String structuralTraceAvailabe = parseQuery(query).toString().replace(" ", "");
        structuralTraceExpected = structuralTraceExpected.replace(" ", "");
        assertEquals(structuralTraceExpected, structuralTraceAvailabe);
    }

    public void testLeftParenthesisQuery() {
        String query = "(^[a,b] IS 'str' AND ^[c] IS 'str') OR |[d] IS 123;";

        BaseQuery val1 = new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("a", "b").collect(Collectors.toList()), true);
        BaseQuery val2 =  new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("c").collect(Collectors.toList()), true);
        BaseQuery val3 = new BaseQuery(Operator.IS, new NumberValue(123D), Stream.of("d").collect(Collectors.toList()), false);

        BinaryCriteria expectedLeft = new BinaryCriteria(Condition.AND, val1, val2);

        BinaryCriteria expected = new BinaryCriteria(Condition.OR, expectedLeft, val3);

        BinaryCriteria filter = (BinaryCriteria) parseQuery(query);
        BinaryCriteria filterLeft = (BinaryCriteria) filter.getLeft();
        BaseQuery filterRight = (BaseQuery) filter.getRight();

        assertEquals(expected.getCondition(), filter.getCondition());
        assertEquals(expectedLeft.getCondition(), filterLeft.getCondition());
        checkUnaryFilter(val3, filterRight);
    }

    public void testRightParenthesisQuery() {
        //Custom Query
        String query = "^[a,b] IS 'str' AND (^[c] IS 'str' OR |[d] IS 123);";

        BaseQuery val1 = new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("a", "b").collect(Collectors.toList()), true);
        BaseQuery val2 =  new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("c").collect(Collectors.toList()), true);
        BaseQuery val3 = new BaseQuery(Operator.IS, new NumberValue(123D), Stream.of("d").collect(Collectors.toList()), false);

        BinaryCriteria expectedRight = new BinaryCriteria(Condition.OR, val2, val3);

        BinaryCriteria expected = new BinaryCriteria(Condition.AND, val1, expectedRight);

        BinaryCriteria filter = (BinaryCriteria) parseQuery(query);
        BinaryCriteria filterRight = (BinaryCriteria) filter.getRight();
        BaseQuery filterLeft = (BaseQuery) filter.getLeft();

        assertEquals(expected.getCondition(), filter.getCondition());
        assertEquals(expectedRight.getCondition(), filterRight.getCondition());
        checkUnaryFilter(val1, filterLeft);
    }

    public void testParenthesisQuery() {
        String query = "(^[a,b] IS 'str' AND ^[c] IS 'str') OR (|[d] IS 123 AND |[c] IS 'str');";

        BaseQuery val1 = new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("a", "b").collect(Collectors.toList()), true);
        BaseQuery val2 =  new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("c").collect(Collectors.toList()), true);
        BaseQuery val3 = new BaseQuery(Operator.IS, new NumberValue(123D), Stream.of("d").collect(Collectors.toList()), false);
        BaseQuery val4 =  new BaseQuery(Operator.IS, new StringValue("str"), Stream.of("c").collect(Collectors.toList()), false);

        BinaryCriteria expectedLeft = new BinaryCriteria(Condition.AND, val1, val2);
        BinaryCriteria expectedRight = new BinaryCriteria(Condition.AND, val3, val4);

        BinaryCriteria expected = new BinaryCriteria(Condition.OR, expectedLeft, expectedRight);

        BinaryCriteria filter = (BinaryCriteria) parseQuery(query);
        BinaryCriteria filterLeft = (BinaryCriteria) filter.getLeft();
        BinaryCriteria filterRight = (BinaryCriteria) filter.getRight();

        assertEquals(expected.getCondition(), filter.getCondition());
        assertEquals(expectedLeft.getCondition(), filterLeft.getCondition());
        assertEquals(expectedRight.getCondition(), filterRight.getCondition());
    }



    private void checkUnaryFilter(BaseQuery expected, BaseQuery result) {
        assertEquals(expected.getOperator(), result.getOperator());
        assertEquals(expected.getValue().getValue(), result.getValue().getValue());
        assertEquals(expected.getFields(), result.getFields());
        assertEquals(expected.isAll(), result.isAll());
    }


}
