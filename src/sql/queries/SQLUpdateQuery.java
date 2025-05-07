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


}
