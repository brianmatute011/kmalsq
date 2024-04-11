grammar Kmalsq;

@header {
package mlix.global.kmal.parser;
}

simple_filter: parenthesis_expression ';';

parenthesis_expression: recursive_expression | general_expression;

recursive_expression: or_expression;

and_expression: general_expression (AND_OPERATOR general_expression)*;

or_expression: and_expression (OR_OPERATOR and_expression)*;


general_expression:
        filter_expr
        | '(' parenthesis_expression ')'
        | DENAIL_OPERATOR filter_expr
        | DENAIL_OPERATOR '(' parenthesis_expression ')';


filter_expr : FIELD_SYMBOL? fields OPERATOR value;

fields : '*' | '[' field_list ']';

value: range_val | list_val | simple_val;

field_list : IDENT (',' IDENT) *;

range_val  : 'FROM' (DATE_VALUE | NUMBER_VALUE) 'TO' (DATE_VALUE | NUMBER_VALUE);


list_val : '[' (term_value | NUMBER_VALUE | STRING_VALUE) (',' (term_value | NUMBER_VALUE | STRING_VALUE)) * ']';
simple_val: term_value | DATE_VALUE | NUMBER_VALUE | BOOLEAN_VALUE | STRING_VALUE;


term_value: 'T' STRING_VALUE;


FIELD_SYMBOL: ('^' | '|');

AND_OPERATOR: 'AND';

OR_OPERATOR: 'OR';

DENAIL_OPERATOR: 'NOT';


OPERATOR: 'IS' | 'IS_NOT' | 'STARTS_WITH' | 'NO_STARTS_WITH' | 'ENDS_WITH' | 'NO_END_WITH' | 'CONTAINS'
            | 'NO_CONTAINS' | '==' | '!=' | '>' | '>=' | '<'
            | '<=' | 'RANGE' | 'RANGOUT' | 'ALL' | 'ANY' | 'NONE';



fragment INT: [0-9] [0-9]* ;

BOOLEAN_VALUE: 'true'| 'false';

DATE_VALUE: INT '/' INT '/' INT | INT '/' INT '/' INT '-' INT ':' INT ':' INT ;

NUMBER_VALUE: '-'? INT '.' INT | '-'? INT;
STRING_VALUE: '\'' .+? '\'';

IDENT: [_a-zA-Z] [a-zA-Z0-9_.] *;

WHITESPACE : (' ' | '\t') -> skip ;