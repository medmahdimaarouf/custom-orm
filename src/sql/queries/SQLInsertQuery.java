package sql.queries;

import core.queries.AbstractInsertQuery;

/**
 * Represents a SQL INSERT query specific to the SQL adapter layer.
 * <p>
 * Extends {@link AbstractInsertQuery} to provide functionality for constructing and executing
 * INSERT operations on a target SQL table.
 * </p>
 */
public abstract class SQLInsertQuery extends AbstractInsertQuery<SQLInsertQuery> {

    /**
     * Constructs a new {@code SQLInsertQuery}.
     */
    public SQLInsertQuery() {
        super();
    }
}
