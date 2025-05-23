package sql.queries;

import core.queries.AbstractDeleteQuery;

import java.util.Optional;

/**
 * Represents a SQL DELETE query specific to the SQL adapter layer.
 * <p>
 * Extends {@link AbstractDeleteQuery} to provide behavior for executing DELETE operations
 * on a specified table with optional alias support.
 * </p>
 */
public abstract class SQLDeleteQuery extends AbstractDeleteQuery<SQLDeleteQuery> {
    /**
     * The target table to delete from.
     */
    private final Optional<String> table;

    /**
     * An optional alias for the table.
     */
    private final Optional<String> alias;

    /**
     * Constructs a new {@code SQLDeleteQuery} for the given table.
     *
     * @param table the name of the table to delete from.
     */
    public SQLDeleteQuery(String table) {
        super();
        this.table = Optional.of(table);
        this.alias = Optional.empty();
    }
}
