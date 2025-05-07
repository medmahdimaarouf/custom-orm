package core.queries;

import core.clauses.Limit;
import core.clauses.OrderBy;
import core.clauses.Where;

import java.util.*;

/**
 * AbstractSelectQuery provides a base structure for building SQL SELECT queries.
 * It supports specifying selected columns, data sources, filtering, sorting, and limiting results.
 *
 * @param <Q> The concrete query type extending this class.
 */
public abstract class AbstractSelectQuery<Q extends Query> extends Query<AbstractSelectQuery<Q>> {

    /**
     * List of column names to select in the query.
     */
    protected List<String> args = new ArrayList<>();

    /**
     * Map of tables and their optional aliases used in the FROM clause.
     */
    protected Map<String, String> targets = new HashMap<>();

    /**
     * List of WHERE clause conditions.
     */
    protected List<Where> wheres = new ArrayList<>();

    /**
     * List of ORDER BY clauses.
     */
    protected List<OrderBy> orderBy = new ArrayList<>();

    /**
     * Optional LIMIT clause.
     */
    protected Optional<Limit> limit = Optional.empty();

    /**
     * Constructs the query with the specified columns to select.
     *
     * @param args The columns to include in the SELECT clause.
     */
    public AbstractSelectQuery(String... args) {
        this.args.addAll(Arrays.asList(args));
    }

    /**
     * Adds a table to the FROM clause without an alias.
     *
     * @param table The name of the table.
     * @return The current query instance.
     */
    public final Q from(String table) {
        this.targets.put(table, null);
        return (Q) this;
    }

    /**
     * Adds a table with an alias to the FROM clause.
     *
     * @param table The name of the table.
     * @param alias The alias for the table.
     * @return The current query instance.
     */
    public final Q from(String table, String alias) {
        this.targets.put(table, alias);
        return (Q) this;
    }

    /**
     * Adds a WHERE condition to the query.
     *
     * @param where The WHERE condition.
     * @return The current query instance.
     */
    public final Q where(final Where where) {
        this.wheres.add(where);
        return (Q) this;
    }

    /**
     * Adds an ORDER BY clause using the default (ascending) direction.
     *
     * @param orderBy The column to order by.
     * @return The current query instance.
     */
    public final Q orderBy(String orderBy) {
        this.orderBy.add(new OrderBy(orderBy));
        return (Q) this;
    }

    /**
     * Adds an ORDER BY clause with a specific direction.
     *
     * @param orderBy   The column to order by.
     * @param direction The order direction (ASC or DESC).
     * @return The current query instance.
     */
    public final Q orderBy(String orderBy, OrderBy.OrderDirection direction) {
        this.orderBy.add(new OrderBy(orderBy, direction));
        return (Q) this;
    }

    /**
     * Adds a LIMIT clause to restrict the number of results.
     *
     * @param amount The maximum number of results to return.
     * @return The current query instance.
     */
    public final Q limit(final int amount) {
        this.limit = Optional.of(new Limit(amount));
        return (Q) this;
    }

}
