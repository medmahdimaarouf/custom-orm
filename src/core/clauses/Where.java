package core.clauses;

import java.util.List;

/**
 * Represents a SQL WHERE clause used to filter query results based on conditions.
 * <p>
 * Supports standard comparison operators, logical combinations (AND, OR), and raw SQL expressions.
 */
public class Where implements Clause {

    /**
     * Enumeration of comparison operators used in WHERE clauses.
     */
    public enum Operator {
        EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL,
        LESS_THAN, LESS_THAN_OR_EQUAL, IS_NULL, IS_NOT_NULL,
        IN, NOT_IN, BETWEEN, LIKE, NOT_LIKE, RAW
    }

    /**
     * Enumeration of logical operators used to combine WHERE conditions.
     */
    public enum LogicalOperator {
        AND, OR
    }

    private String field;
    private Operator operator;
    private Object value;
    private Object secondValue;
    private List<Object> values;

    private LogicalOperator logicalOperator;
    private Where left;
    private Where right;

    private String rawExpression;

    // Private constructors used internally by factory methods

    private Where(String field, Operator operator, Object value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }

    private Where(String field, Operator operator, List<Object> values) {
        this.field = field;
        this.operator = operator;
        this.values = values;
    }

    private Where(String field, Object start, Object end) {
        this.field = field;
        this.operator = Operator.BETWEEN;
        this.value = start;
        this.secondValue = end;
    }

    private Where(LogicalOperator logicalOperator, Where left, Where right) {
        this.logicalOperator = logicalOperator;
        this.left = left;
        this.right = right;
    }

    private Where(String rawExpression) {
        this.operator = Operator.RAW;
        this.rawExpression = rawExpression;
    }

    // Static factory methods for constructing WHERE clauses

    /**
     * Creates a WHERE clause with equality condition.
     *
     * @param field the field to compare
     * @param value the value to compare to
     * @return a new Where instance
     */
    public static Where equal(String field, Object value) {
        return new Where(field, Operator.EQUAL, value);
    }

    /**
     * Creates a WHERE clause with inequality condition.
     *
     * @param field the field to compare
     * @param value the value to compare to
     * @return a new Where instance
     */
    public static Where notEqual(String field, Object value) {
        return new Where(field, Operator.NOT_EQUAL, value);
    }

    /**
     * Creates a WHERE clause with greater-than condition.
     *
     * @param field the field to compare
     * @param value the lower bound value
     * @return a new Where instance
     */
    public static Where greaterThan(String field, Object value) {
        return new Where(field, Operator.GREATER_THAN, value);
    }

    /**
     * Creates a WHERE clause with greater-than-or-equal condition.
     *
     * @param field the field to compare
     * @param value the lower bound value
     * @return a new Where instance
     */
    public static Where greaterThanOrEqual(String field, Object value) {
        return new Where(field, Operator.GREATER_THAN_OR_EQUAL, value);
    }

    /**
     * Creates a WHERE clause with less-than condition.
     *
     * @param field the field to compare
     * @param value the upper bound value
     * @return a new Where instance
     */
    public static Where lessThan(String field, Object value) {
        return new Where(field, Operator.LESS_THAN, value);
    }

    /**
     * Creates a WHERE clause with less-than-or-equal condition.
     *
     * @param field the field to compare
     * @param value the upper bound value
     * @return a new Where instance
     */
    public static Where lessThanOrEqual(String field, Object value) {
        return new Where(field, Operator.LESS_THAN_OR_EQUAL, value);
    }

    /**
     * Creates a WHERE clause that checks if a field is NULL.
     *
     * @param field the field to check
     * @return a new Where instance
     */
    public static Where isNull(String field) {
        return new Where(field, Operator.IS_NULL, null);
    }

    /**
     * Creates a WHERE clause that checks if a field is NOT NULL.
     *
     * @param field the field to check
     * @return a new Where instance
     */
    public static Where isNotNull(String field) {
        return new Where(field, Operator.IS_NOT_NULL, null);
    }

    /**
     * Creates a WHERE clause with IN condition.
     *
     * @param field  the field to compare
     * @param values the list of values to match
     * @return a new Where instance
     */
    public static Where in(String field, List<Object> values) {
        return new Where(field, Operator.IN, values);
    }

    /**
     * Creates a WHERE clause with NOT IN condition.
     *
     * @param field  the field to compare
     * @param values the list of values to exclude
     * @return a new Where instance
     */
    public static Where notIn(String field, List<Object> values) {
        return new Where(field, Operator.NOT_IN, values);
    }

    /**
     * Creates a WHERE clause with BETWEEN condition.
     *
     * @param field the field to compare
     * @param start the lower bound value
     * @param end   the upper bound value
     * @return a new Where instance
     */
    public static Where between(String field, Object start, Object end) {
        return new Where(field, start, end);
    }

    /**
     * Creates a WHERE clause with LIKE condition.
     *
     * @param field   the field to compare
     * @param pattern the pattern to match
     * @return a new Where instance
     */
    public static Where like(String field, Object pattern) {
        return new Where(field, Operator.LIKE, pattern);
    }

    /**
     * Creates a WHERE clause with NOT LIKE condition.
     *
     * @param field   the field to compare
     * @param pattern the pattern to exclude
     * @return a new Where instance
     */
    public static Where notLike(String field, Object pattern) {
        return new Where(field, Operator.NOT_LIKE, pattern);
    }

    /**
     * Creates a WHERE clause using a raw SQL expression.
     *
     * @param rawExpression the raw SQL to include in the WHERE clause
     * @return a new Where instance
     */
    public static Where raw(String rawExpression) {
        return new Where(rawExpression);
    }

    /**
     * Combines this WHERE clause with another using a logical AND.
     *
     * @param other the other Where clause
     * @return a new Where instance representing (this AND other)
     */
    public Where and(Where other) {
        return new Where(LogicalOperator.AND, this, other);
    }

    /**
     * Combines this WHERE clause with another using a logical OR.
     *
     * @param other the other Where clause
     * @return a new Where instance representing (this OR other)
     */
    public Where or(Where other) {
        return new Where(LogicalOperator.OR, this, other);
    }

    // Getters

    /**
     * Returns the field name used in this WHERE clause.
     *
     * @return the field name
     */
    public String getField() {
        return field;
    }

    /**
     * Returns the comparison operator of this WHERE clause.
     *
     * @return the operator
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Returns the primary value used in this WHERE clause.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the second value used in BETWEEN conditions.
     *
     * @return the second value
     */
    public Object getSecondValue() {
        return secondValue;
    }

    /**
     * Returns the list of values used in IN/NOT IN conditions.
     *
     * @return the list of values
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * Returns the logical operator (AND/OR) used for nested conditions.
     *
     * @return the logical operator
     */
    public LogicalOperator getLogicalOperator() {
        return logicalOperator;
    }

    /**
     * Returns the left-hand WHERE clause in a logical combination.
     *
     * @return the left WHERE clause
     */
    public Where getLeft() {
        return left;
    }

    /**
     * Returns the right-hand WHERE clause in a logical combination.
     *
     * @return the right WHERE clause
     */
    public Where getRight() {
        return right;
    }

    /**
     * Returns the raw SQL expression used in the WHERE clause.
     *
     * @return the raw expression
     */
    public String getRawExpression() {
        return rawExpression;
    }
}
