package core.queries;

import core.clauses.Where;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AbstractUpdateQuery provides the foundational logic for building SQL UPDATE queries.
 * It allows defining target tables, conditions (WHERE clauses), and column-value pairs to update.
 *
 * @param <Q> The concrete query type extending this class.
 */
public abstract class AbstractUpdateQuery<Q extends Query> extends Query<AbstractUpdateQuery<Q>> {

    /**
     * A map of table names and their optional aliases used in the FROM clause.
     */
    protected Map<String, String> targets = new HashMap<>();

    /**
     * A list of WHERE clause conditions.
     */
    protected List<Where> wheres = new ArrayList<>();

    /**
     * A map of column names to their new values, used in the SET clause.
     */
    protected Map<String, Object> setters = new HashMap<>();

    /**
     * Specifies the table to update without an alias.
     *
     * @param table The name of the target table.
     * @return The current query instance.
     */
    public final Q from(String table) {
        this.targets.put(table, null);
        return (Q) this;
    }

    /**
     * Specifies the table to update with an alias.
     *
     * @param table The name of the target table.
     * @param alias The alias for the table.
     * @return The current query instance.
     */
    public final Q from(String table, String alias) {
        this.targets.put(table, alias);
        return (Q) this;
    }

    /**
     * Adds a condition to the WHERE clause.
     *
     * @param where The WHERE condition.
     * @return The current query instance.
     */
    public final Q where(final Where where) {
        this.wheres.add(where);
        return (Q) this;
    }

    /**
     * Adds a column-value pair to be updated in the SET clause.
     *
     * @param column The name of the column to update.
     * @param value  The new value to set.
     * @return The current query instance.
     */
    public final Q set(String column, Object value) {
        this.setters.put(column, value);
        return (Q) this;
    }
}
