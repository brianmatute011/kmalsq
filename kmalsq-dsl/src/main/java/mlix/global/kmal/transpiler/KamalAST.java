package mlix.global.kmal.transpiler;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.var;

import mlix.global.kmal.exception.KmalsqException;

import mlix.global.kmal.model.criteria.BaseCriteria;
import mlix.global.kmal.model.criteria.BaseQuery;
import mlix.global.kmal.model.criteria.BinaryCriteria;
import mlix.global.kmal.model.criteria.UnaryCriteria;
import mlix.global.kmal.model.operator.Operator;

import mlix.global.kmal.parser.KmalsqBaseVisitor;
import mlix.global.kmal.parser.KmalsqParser;

import mlix.global.kmal.model.types.Condition;
import mlix.global.kmal.model.types.FieldSymbol;

import mlix.global.kmal.model.value.Value;
import mlix.global.kmal.model.value.DateValue;
import mlix.global.kmal.model.value.NumberValue;
import mlix.global.kmal.model.value.StringValue;
import mlix.global.kmal.model.value.TermValue;
import mlix.global.kmal.model.value.BooleanValue;

public class KamalAST extends KmalsqBaseVisitor<Object> {

    private static final String NUMERIC_ERROR = "Value not allowed for numeric type.";
    private static final String DATE_ERROR = "Value not allowed for date type.";

    @Override
    public Object visitSimple_filter(KmalsqParser.Simple_filterContext ctx) {
        var parenthesisExpression = ctx.parenthesis_expression();
        System.out.println("[!] Pass by simple_expr production");
        return visitParenthesis_expression(parenthesisExpression);
    }

    @Override
    public Object visitParenthesis_expression(KmalsqParser.Parenthesis_expressionContext ctx) {
        var recursiveExpression = ctx.recursive_expression();
        var generalExpression = ctx.general_expression();
        return (recursiveExpression != null)?
                visitRecursive_expression(recursiveExpression):
                visitGeneral_expression(generalExpression);
    }

    @Override
    public Object visitOr_expression(KmalsqParser.Or_expressionContext ctx) {
        System.out.println("[!] Pass by or_expr production");
        var andExpressions = ctx.and_expression();
        if (andExpressions.size() == 1){
            System.out.println("[!] Visit and_expresion");
            return visitAnd_expression(andExpressions.get(0));
        }
        else{
            Condition condition  = Condition.valueOf(ctx.OR_OPERATOR().get(0).getText());
            System.out.println("[!] getting logical operator (or ) => " + condition);
            var andExpressionsLeft = visitAnd_expression(andExpressions.get(0));
            var andExpressionRight = visitAnd_expression(andExpressions.get(1));

            //BynarySimpleCriteria OR Operators
            var bscOR = new BinaryCriteria(
                    condition,
                    (andExpressionsLeft instanceof BaseCriteria)?
                            (BaseCriteria) andExpressionsLeft : (UnaryCriteria) andExpressionRight,

                    (andExpressionRight instanceof BaseCriteria)?
                            (BaseCriteria) andExpressionRight : (UnaryCriteria) andExpressionRight
            );

            for (var indexExprAnd = 2;  indexExprAnd < andExpressions.size(); ++indexExprAnd){
                var tailAndExpresionRight = visitAnd_expression(andExpressions.get(indexExprAnd));
                bscOR = new BinaryCriteria(
                        condition,
                        bscOR,
                        (tailAndExpresionRight instanceof BaseCriteria)?
                                (BaseCriteria) tailAndExpresionRight : (UnaryCriteria) tailAndExpresionRight
                );
            }

            return bscOR;

        }
    }

    @Override
    public Object visitAnd_expression(KmalsqParser.And_expressionContext ctx) {
        System.out.println("[!] Pass by and_expr production");
        var generalExpressions = ctx.general_expression();

        if (generalExpressions.size() == 1){
            System.out.println("General Expression Getting on ANDEXPR: " + generalExpressions.get(0).getText());
            return visitGeneral_expression(generalExpressions.get(0));

        }
        else {
            Condition condition = Condition.valueOf(ctx.AND_OPERATOR().get(0).getText());
            System.out.println("[!] getting logical operator (and) => " + condition);
            Object generalExpressionLeft = visitGeneral_expression(generalExpressions.get(0));
            Object generalExpressionRight = visitGeneral_expression(generalExpressions.get(1));
            System.out.println("[!] generalExpressionLeft => " + generalExpressions + "| GESIZE => " + generalExpressions.size());
            //Print Type of genralExpresion
            System.out.println("[!] generalExpressionLeft => " + generalExpressions.getClass());
            //Print each elemetn of generalExpresions as ArrayList
            for (KmalsqParser.General_expressionContext generalExpression : generalExpressions) {
                System.out.println("[!] generalExpressionLeft => " + generalExpression);
            }



            //Returnig BynarySimpleCriteria AND Operators
            var bscAND =  new BinaryCriteria(condition,
                    (generalExpressionLeft instanceof BaseCriteria) ?
                            (BaseCriteria) generalExpressionLeft : (UnaryCriteria) generalExpressionLeft,

                    (generalExpressionRight instanceof BaseCriteria) ?
                            (BaseCriteria) generalExpressionRight : (UnaryCriteria) generalExpressionRight
            );


            for (var indexExprGen = 2;  indexExprGen < generalExpressions.size(); ++indexExprGen){
                var tailGenExpresionRight = visitGeneral_expression(generalExpressions.get(indexExprGen));
                bscAND = new BinaryCriteria(
                        condition,
                        bscAND,
                        (tailGenExpresionRight instanceof BaseCriteria)?
                                (BaseCriteria) tailGenExpresionRight : (UnaryCriteria) tailGenExpresionRight
                );
            }

            return bscAND;
        }

    }

    @Override
    public Object visitGeneral_expression(KmalsqParser.General_expressionContext ctx) {
        var filterExprContext = ctx.filter_expr();
        var parenthesisExpression = ctx.parenthesis_expression();

        if (ctx.DENAIL_OPERATOR() != null && ctx.parenthesis_expression() != null){
            Condition condition = Condition.valueOf(ctx.DENAIL_OPERATOR().getText());
            System.out.println("[!] Pass by general_expr production (DENAILOPERATOR && parenthesis_expr)");
            return new UnaryCriteria(condition, (BaseCriteria) visitParenthesis_expression(parenthesisExpression));
        }
        else if (ctx.DENAIL_OPERATOR() != null) {
            Condition condition = Condition.valueOf(ctx.DENAIL_OPERATOR().getText());
            System.out.println("[!] getting logical operator (denail) => " + condition);
            return new UnaryCriteria(condition, visitFilter_expr(filterExprContext));
        }
        else if (ctx.parenthesis_expression() != null){
            return  visitParenthesis_expression(parenthesisExpression);
        }

        return visitFilter_expr(filterExprContext);
    }

    @Override
    public BaseCriteria visitFilter_expr(KmalsqParser.Filter_exprContext ctx) {
        String fieldsString = ctx.fields().getText();

        List<String> fields = new ArrayList<>();
        String fieldSymbol = ctx.FIELD_SYMBOL() != null ? ctx.FIELD_SYMBOL().getText(): FieldSymbol.ALL.getSymbol();

        if (!"*".equals(fieldsString))
            fields = Stream.of(fieldsString.substring(1, fieldsString.length() - 1).split(",")).collect(Collectors.toList());

        Operator operator;

        switch (ctx.OPERATOR().getText()) {
            case "==":
                operator = Operator.EQUAL;
                break;
            case "!=":
                operator = Operator.NOT_EQUAL;
                break;
            case ">":
                operator = Operator.GREATER_THAN;
                break;
            case ">=":
                operator = Operator.GREATER_THAN_EQUALS;
                break;
            case "<":
                operator = Operator.LESS_THAN;
                break;
            case "<=":
                operator = Operator.LESS_THAN_EQUALS;
                break;
            default:
                operator = Operator.valueOf(ctx.OPERATOR().getText());
        }

        Value<?> value = visitValue(ctx.value());
        return new BaseQuery(operator, value, fields, fieldSymbol.equals(FieldSymbol.ALL.getSymbol()));
    }

    @Override
    public Value<?> visitValue(KmalsqParser.ValueContext ctx) {
        String text = ctx.getText();
        System.out.println("[!] getting value => " + text);

        if (text.startsWith("[")) { // Es una lista de valores
            List<String> valuesList = Stream.of(text.substring(1, text.length() - 1).replace("\"","").split(",")).
                    collect(Collectors.toList());

            return obtainValue(valuesList);
        } else if (text.startsWith("FROM")) { // Es un rango
            String[] value = text.replace("FROM", "").replace("TO", " ").split(" ");
            String from = value[0];
            String to = value[1];

            return obtainValue(from, to);
        }
        else { // Es un valor simple
            return obtainValue(text);
        }
    }

    private Value<?> obtainValue(String valueString) {
        if (isNumber(valueString)) {
            try {
                Double numberValue = Double.parseDouble(valueString);
                return new NumberValue(numberValue);
            } catch (Exception e) {
                throw new KmalsqException(NUMERIC_ERROR);
            }
        } else if (isDate(valueString)) {
            try {
                return new DateValue(valueString);
            } catch (Exception e) {
                throw new KmalsqException(e.getMessage());
            }
        } else if (isBoolean(valueString)){
            return new BooleanValue(Boolean.parseBoolean(valueString));
        } else {
            return isTerm(valueString) ? new TermValue(valueString.substring(1)
                    .replace("'", "")) : new StringValue(valueString.replace("'", ""));
        }
    }

    private Value<?> obtainValue(String from, String to) {
        if (isNumber(from) && isNumber(to)) {
            try {
                Double numberFrom = Double.parseDouble(from);
                Double numberTo = Double.parseDouble(to);
                return new NumberValue(numberFrom, numberTo);
            } catch (Exception e) {
                throw new KmalsqException(NUMERIC_ERROR);
            }
        } else if (isDate(from) && isDate(to)) {
            try {
                return new DateValue(from, to);
            } catch (Exception e) {
                throw new KmalsqException(DATE_ERROR);
            }
        } else {
            throw new KmalsqException("Error en el formato de los valores del rango.");
        }
    }

    private Value<?> obtainValue(List<String> valuesList) {
        if (valuesList.stream().allMatch(this::isNumber)) {
            try {
                return new NumberValue(valuesList.stream().map(Double::parseDouble).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new KmalsqException(NUMERIC_ERROR);
            }
        } else if (valuesList.stream().allMatch(this::isDate)) {
            try {
                return new DateValue(valuesList.stream().map(Long::parseLong).collect(Collectors.toList()));
            } catch (Exception e) {
                throw new KmalsqException(DATE_ERROR);
            }
        } else if (valuesList.stream().allMatch(this::isTerm)) {
            return new TermValue(valuesList.stream().map(value -> value.substring(1).replace("'", "")).
                    collect(Collectors.toList()));
        } else { // Todos los elementos son tratados como strings
            return new StringValue(valuesList.stream().map(value -> value.replace("'", "")).
                    collect(Collectors.toList()));
        }
    }
    private boolean isNumber(String valueString) {
        final Pattern numberPattern = Pattern.compile("-?\\d+(\\.\\d+)?"); // Expresión regular para matchear números.
        return numberPattern.matcher(valueString).matches();
    }

    private boolean isDate(String valueString) {
        return !(isTerm(valueString) || valueString.startsWith("'")) && valueString.contains("/");
    }

    private boolean isTerm(String valueString) {
        return valueString.startsWith("T");
    }

    private boolean isBoolean(String valueString) {
        return "true".equals(valueString) || "false".equals(valueString);
    }
}
