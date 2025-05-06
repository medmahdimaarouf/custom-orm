package sql.queries;

import core.queries.AbstractSelectQuery;

/**
 * Represents a SQL SELECT query specific to the SQL adapter layer.
 * <p>
 * Extends {@link AbstractSelectQuery} to provide functionality for constructing and executing
 * SELECT operations, allowing the selection of specific columns from a SQL table.
 * </p>
 */
public abstract class SQLSelectQuery extends AbstractSelectQuery<SQLSelectQuery> {

    /**
     * Constructs a new {@code SQLSelectQuery}.
     *
     * @param args The columns to select in the SQL query.
     */
    public SQLSelectQuery(String... args) {
        super(args);
    }
}
