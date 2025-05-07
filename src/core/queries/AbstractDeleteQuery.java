package core.queries;

import core.clauses.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * AbstractDeleteQuery provides the base implementation for DELETE queries.
 *
 * @param <Q> The concrete type of the query extending this abstract class.
 */
public abstract class AbstractDeleteQuery<Q extends Query> extends Query<AbstractDeleteQuery<Q>> {

    /**
     * List of WHERE clauses applied to the DELETE query.
     */
    protected List<Where> wheres = new ArrayList<>();

    /**
     * Sets the target table to delete from.
     *
     * @param table The name of the table.
     * @return The current query instance.
     */
    public final Q from(String table) {
        return null;
    }

    /**
     * Sets the target table with an alias to delete from.
     *
     * @param table The name of the table.
     * @param alias The alias for the table.
     * @return The current query instance.
     */
    public final Q from(final String table,final String alias) {
        return null;
    }

    /**
     * Adds a WHERE condition to the DELETE query.
     *
     * @param where The WHERE clause to add.
     * @return The current query instance.
     */
    public final Q where(final Where where) {
        this.wheres.add(where);
        return (Q) this;
    }
}
