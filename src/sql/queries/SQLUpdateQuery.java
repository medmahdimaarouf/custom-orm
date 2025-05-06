package sql.queries;

import core.clauses.Where;
import core.queries.AbstractUpdateQuery;

import java.util.Optional;

/**
 * Represents an SQL UPDATE query specific to the SQL adapter layer.
 * <p>
 * Extends {@link AbstractUpdateQuery} to provide functionality for constructing and executing
 * UPDATE operations that modify existing records in a SQL table.
 * </p>
 */
public abstract class SQLUpdateQuery extends AbstractUpdateQuery<SQLUpdateQuery> {
    private Optional<String> table = Optional.empty();

    /**
     * Constructs a new {@code SQLUpdateQuery}.
     *
     * @param table The table to update.
     */
    public SQLUpdateQuery(String table) {
        this.table = Optional.of(table);
    }

    /**
     * Adds a WHERE condition to the UPDATE query to specify which records to update.
     *
     * @param where The condition to be added to the WHERE clause.
     * @return The updated SQLUpdateQuery.
     */
    @Override
    public SQLUpdateQuery where(final Where where) {
        this.wheres.add(where);
        return this;
    }
}
