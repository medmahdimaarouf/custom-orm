package core;

import core.queries.AbstractDeleteQuery;
import core.queries.AbstractInsertQuery;
import core.queries.AbstractSelectQuery;
import core.queries.AbstractUpdateQuery;

/**
 * A generic interface for all query builders.
 *
 * Provides methods to create different types of DML query builders such as SELECT, INSERT, UPDATE, and DELETE ( basic DML queries ).
 */
public interface QueryBuilder {

    /**
     * Creates a SELECT query with the given projection columns.
     *
     * @param args the column names to select.
     * @param <Q>  the type of the resulting {@link AbstractSelectQuery}.
     * @return an instance of {@code Q} representing the SELECT query.
     */
    <Q extends AbstractSelectQuery> Q select(String... args);

    /**
     * Creates an UPDATE query targeting the specified table.
     *
     * @param table the table to update.
     * @param <Q>   the type of the resulting {@link AbstractUpdateQuery}.
     * @return an instance of {@code Q} representing the UPDATE query.
     */
    <Q extends AbstractUpdateQuery> Q update(String table);

    /**
     * Creates a DELETE query targeting the specified table.
     *
     * @param table the table to delete from.
     * @param <Q>   the type of the resulting {@link AbstractDeleteQuery}.
     * @return an instance of {@code Q} representing the DELETE query.
     */
    <Q extends AbstractDeleteQuery> Q delete(String table);

    /**
     * Creates an INSERT query.
     *
     * @param <Q> the type of the resulting {@link AbstractInsertQuery}.
     * @return an instance of {@code Q} representing the INSERT query.
     */
    <Q extends AbstractInsertQuery> Q insert();
}
