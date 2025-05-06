package core.clauses;

import java.util.Optional;

/**
 * Represents an SQL "ORDER BY" clause used to sort query results
 * by a given column or expression, optionally with a specified direction (ASC or DESC).
 */
public class OrderBy implements Clause {

    /**
     * Enumeration for specifying the sort direction in an ORDER BY clause.
     */
    public enum OrderDirection {
        ASC("ASC"),
        DESC("DESC");

        private final String value;

        /**
         * Constructs a new OrderDirection enum with the given SQL keyword value.
         *
         * @param value the SQL keyword representing the direction (e.g., "ASC" or "DESC")
         */
        OrderDirection(String value) {
            this.value = value;
        }

        /**
         * Returns the SQL keyword value associated with this direction.
         *
         * @return the direction keyword ("ASC" or "DESC")
         */
        public String getValue() {
            return value;
        }
    }

    private final String columnOrExpression;
    private Optional<OrderDirection> direction = Optional.empty();

    /**
     * Constructs an OrderBy clause with only the column or expression to sort by.
     *
     * @param columnOrExpression the column name or SQL expression used for sorting
     */
    public OrderBy(String columnOrExpression) {
        this.columnOrExpression = columnOrExpression;
    }

    /**
     * Constructs an OrderBy clause with both a column/expression and a sort direction.
     *
     * @param columnOrExpression the column name or SQL expression used for sorting
     * @param direction          the direction of sorting (ASC or DESC)
     */
    public OrderBy(String columnOrExpression, OrderDirection direction) {
        this.columnOrExpression = columnOrExpression;
        this.direction = Optional.of(direction);
    }

    /**
     * Returns the column or SQL expression used in the ORDER BY clause.
     *
     * @return the column name or expression
     */
    public String getColumnOrExpression() {
        return columnOrExpression;
    }

    /**
     * Returns an {@link Optional} containing the sorting direction, if set.
     *
     * @return an Optional of {@link OrderDirection}
     */
    public Optional<OrderDirection> getDirection() {
        return direction;
    }
}
