# Generic Domain Specific Language (DSL) for query generation [KmalSQ]


This DSL allows you to perform a set of simple queries and
generics through their syntax to various database engines.


## Precedence

The order of precedence was included in the new KmalSQ implementation, the following comparison of structural traces checks the status of these precedences before the new version and the old version (This section is for descriptive purposes only)
### Entry made in both cases:
```sql
[a, b] == 1 OR [z, c] == 2 AND [w, x] == 3;
```

#### PRECEDENCE INCLUDED IN THE NEW DSL (NEW_DSL)
```
&(
BaseQuery{ operator: EQUAL, value: a, fields: [a, b], all: true }

	OR 
	
	&(
		BaseQuery{ operator: EQUAL, value: c, fields: [z, c], all: true }
		
		AND
		
		BaseQuery{ operator: EQUAL, value: w, fields: [w, w], all: true }
	)
) 
```

## NOT Model
The NOT operator was included in the new KmalSQ implementation, providing the possibility of denying both simple filters and a set of them (BinaryFilters).
```shell
# Rango de aceptacion del modelo NOT
NOT (<BaseQuery> | <BinaryCriteria> | <UnaryCriteria>)
```

### Example of entry made:
```sql
>> NOT [a] == 5;    
```
#### Structural trace obtained
```
(NOT UnaryCriteria{ operator: EQUAL, value: 5.0, fields: [a], all: true })
```



### Valid prompts
```sql
# Denial of a base query: NOT <BaseQuery>
>> NOT [a, b] == 1;
>> NOT ([a, b] == 1)    
    
# Negation of a binary query: NOT <BinaryCriteria>
>> NOT ([a, b] == 1 OR [c, d] == 2);
    
# Denial of a unary query: NOT <UnaryCriteria>
>> NOT (NOT ([a, b] == 1 OR [c, d] == 2));    
```

### NOT model consistency
```shell
# Two equivalent expressions
>> NOT ([asin.keyword] == '0786926503' AND NOT ([title.keyword] STARTS_WITH 'The' OR [title.keyword] STARTS_WITH 'B')) 

>> NOT [asin.keyword] == '0786926503' OR  NOT (NOT [title.keyword] STARTS_WITH 'The' AND NOT [title.keyword] STARTS_WITH 'B')
```



Syntax
--------

The syntax of the language is defined by the following rules:

filter ([logical_operator](#logical_operator-link) filter)

filter = [quantifier](#quantifier-link)
[field_list](#field_list-link) [operator](#operator-link)
worth

fields = [ [list\_of\_fields](#list_of_fields-link) ] | *

value = [list\_of\_values](#list_of_values-link) |
[simple_value](#simple_value-link) | [range](#range-link)

`(The query must be written on a single line, otherwise the filter throws an exception.)`

### Example:

Valid queries:

```sh
[field, field2] == 32
```
```sh
^ [name, name.second_name] CONTAINS *brian*
```

```sh
( [name] IS T'brian' or [name] IS T'jhon' ) and [age] GREATER_THAN 20;
```
Consultas no permitidas:
```sh
# (No tiene un valor asociado al operador)`
^ [nombre1, nombre2] CONTAINS
```

```sh
# (No tiene la lista de campos)`
| [] == 32 
```

### Where:

<table>
<colgroup>
<col style="width: 33%" />
<col style="width: 66%" />
</colgroup>
<thead>
<tr class="header">
<th>Rule</th>
<th>Value</th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><p><strong>quantifier</strong></p></td>
<td><p><strong><sup></sup></strong> ^ : Whether the filter should be met for all fields.</p>
<p><strong>|</strong> : If the filter must be met in some of the fields.</p>
<p>After this operator, you can place a blank space, or continue with the rest of the query. If omitted, it is assumed to be <em>^</em>.</p></td>
</tr>
<tr class="even">
<td><p><strong>field_list</strong></p></td>
<td><p>The names, separated by commas and enclosed in square brackets, of the fields where the filter will be applied. These fields must begin with a letter or underscore, and can then contain letters, underscore, or period. A "*" indicates that you want to filter on all fields.</p>
<p>Ex: [ name, age, CI, user.nick ] <code>(Perform the query in all these fields)</code> [ size ] <code>(Perform the query in the _size_ field)</code> </p>
<p>* <code>(Perform the query in all available fields)</code></p>
<p>Not valid field names: 123, 1AAAA, ?uuuu</p></td>
</tr>
<tr class="odd">
<td><p><strong>operator</strong></p></td>
<td><p>Generic filter operator:</p>
<p><strong>IS</strong>, <strong>IS_NOT</strong>: "Equal to"/"Not equal to" operators. Valid only for term type.</p>
<p>Ex:</p>
<p>Assuming that emails with the from and subject fields are stored.</p>
<p>If you want to obtain the emails where your from field is equal to "mlix":</p>
<p>| [from] IS T’mlix'</p>
<p>If you want to obtain emails where some of their from or subject fields are not equal to "mlix":</p>
<p>| [from, subject] IS_NOT T’alex'</p>
<p><strong>STARTS_WITH</strong>, <strong>DOES_NOT_STARTS_WITH</strong>: "starts with"/"does not start with" operators. Valid only for string and term types.</p>
<p>Ex:</p>
<p>If you want to obtain emails where some of their from or subject fields begin with "lara":</p>
<p>| [from, subject] STARTS_WITH <em>lara</em></p>
<p>If you want to obtain emails where some of their from or subject fields do not begin with "lara":</p>
<p>| [from, subject] DOES_NOT_STARTS_WITH <em>lara</em></p>
<p><strong>ENDS_WITH</strong>, <strong>DOES_NOT_END_WITH</strong>: "ends with"/"does not end with" operators. Valid only for string and term types.</p>
<p>Ex:</p>
<p>If you want to obtain emails where some of their from or subject fields end with "lara":</p>
<p>| [from, subject] ENDS_WITH <em>lara</em></p>
<p>If you want to obtain emails where some of their from or subject fields do not end with "lara":</p>
<p>| [from, subject] DOES_NOT_END_WITH <em>lara</em></p>
<p><strong>CONTAINS</strong>, <strong>DOES_NOT_CONTAINS</strong>: "contains"/"does not contain" operators. Valid only for string and term types.</p>
<p>Ex:</p>
<p>If you want to obtain the emails whose from and subject fields contain "mlix":</p>
<p><sup>[from, subject] CONTAINS <em>mlix</em></sup></p>
<p>If you want to obtain the emails whose from and subject fields do not contain "mlix":</p>
<p> [from, subject] DOES_NOT_CONTAINS <em>mlix</em></p>
<p><strong> > </strong>, <strong> >= </strong>: "greater than"/ "greater equal to" operators. Valid only for numbers and dates.</p>
<p>Ex:</p>
<p>Assuming that there are emails with the size and length fields.</p>
<p>If you want to obtain emails whose size or length fields are greater than 30.</p>
<p>| [size, length] > 30</p>
<p>If you want to obtain the emails whose size or length fields are greater than 30.</p>
<p>| [size, length] >= 30</p>
<p><strong> < </strong>, <strong> <= </strong>: "less than"/"less equal to" operators. Valid only for numbers and dates.</p>
<p>Ex:</p>
<p>If you want to obtain emails whose size or length fields are less than 30.</p>
<p>| [size, length] < 30</p>
<p>If you want to obtain the emails whose size or length fields are less than 30.</p>
<p>| [size, length] <= 30</p>
<p><strong>RANGE</strong>, <strong>RANGOUT</strong>: "in range"/"out of range" operators. Valid only for ranks.</p>
<p>Ex:</p>
<p>Assuming that there are emails with the date_send and date_rec fields.</p>
<p>If you want to obtain the emails that were sent between 1/1/2020 and 1/1/2021, and that were received in that same interval.</p>
<p><sup>[date_send, date_rec] RANGE FROM 1/1/2020 TO 1/1/2021</sup></p>
<p>If you want to obtain the emails that were not sent between 1/1/2020 and 1/1/2021, and that were not received in that same interval.</p>
<p>[date_send, date_rec] RANGOUT FROM 1/1/2020 TO 1/1/2021</p>
<p><strong>ALL</strong>, <strong>ANY</strong>, <strong>NONE</strong>: Operators "contains all values"/"contains any of the values"/"contains none of values". Valid only for lists of values.</p>
<p>Ex:</p>
<p>If you want to obtain the emails where the from and subject fields have all these values: "mlix" and "tareas":</p>
<p><sup>[from, subject] ALL [<em>mlix</em>, <em>tasks</em>]</sup></p>
<p>If you want to obtain the emails where the from and subject fields have some of these values: "mlix" and "tasks":</p>
<p> [from, subject] ANY [<em>mlix</em>, <em>tasks</em>]</p>
<p>If you want to obtain emails where the from or subject fields do not have any of these values: "mlix" and "tasks":</p>
<p>| [from, subject] NONE [<em>mlix</em>, <em>tasks</em>]</p>
<p><strong> == </strong>, <strong> != </strong>: "Equal to"/"Not equal to" operators. Valid only for strings, numbers, dates and booleans.</p>
<p>Ex: Assuming that people with the name and nick_name fields are stored.</p>
<p>If you want to obtain people where all their fields are equal to "brian":</p>
<p><sup>* == "brian";</sup></p>
<p>If you want to obtain people where all their fields are different from "brian":</p>
<p> * != "brian";</p></td>
</tr>
<tr class="even">
<td><p><strong>simple_value</strong></p></td>
<td><p>The value by which you want to filter. The filter supports the following values:</p>
<p>- <strong>Numbers</strong>: They can be integers or decimals, positive or negative. Ex: 0, 1234, -567, 9.8, -5.6</p>
<p>- <strong>Dates</strong>: They have the format day/month/year-hour:minutes:seconds. Ex: 9/29/1994-12:00:00</p>
<p>- <strong>Booleans</strong>: true or false.</p>
<p>- <strong>Character strings</strong>: List of characters enclosed in single quotes. Ex: <em>mlix</em></p>
<p>- <strong>Term</strong>: A string of characters with the letter T before the leading quote. (<strong>NOTE</strong>: This is an Elasticsearch type. It is used to perform a search for the exact term in this database engine. The rest of the engines treat it as a string of characters.) Ex. : T'mlix'.</p></td>
</tr>
<tr class="odd">
<td><p><strong>list_of_values</strong></p></td>
<td><p>List of simple values separated by comma and enclosed in square brackets. Valid only for the operators ALL, ANY, NONE.</p>
<p>Ex: [123, 456]</p></td>
</tr>
<tr class="even">
<td><p><strong>range</strong></p></td>
<td><p>It is defined as <strong>FROM</strong> start_value_of_range <strong>TO</strong> end_value_of_range. Valid only for the RANGE and RANGEOUT operators.</p>
<p>Ex:</p>
<p>FROM 1/1/2021 TO 12/31/2021.</p>
<p>FROM 34 TO 50.</p></td>
</tr>
<tr class="odd">
<td><p><strong>logical_operator</strong></p></td>
<td><p>Applies a logical operator with another filter. <strong>AND</strong>, <strong>OR</strong> values. It can be empty.</p>
<p>Ex:</p>
<p>Assuming that there are emails stored with the fields Date_Sent, Date_Received, From and To:</p>
<p>If you want to search for emails whose Date_Sent and Date_Received fields are between 1/1/2020 and 1/1/2021 or some of their From or To fields are the term "lara".</p>
<p><sup>[Sent_Date, Received_Date] RANGE FROM 1/1/2020 TO 1/1/2021 OR | [Of, For] IS T'lara'</sup></p>
<p>If you want to search for emails whose Date_Sent or Date_Received fields are between 1/1/2020 and 1/1/2021 and whose From and To fields begin with "mlix".</p>
<p>| [ Sent_Date, Received_Date] RANGE FROM 1/1/2020 TO 1/1/2021 AND [From, To] STARTS_WITH <em>mlix</em>.</p></td>
</tr>
</tbody>
</table>