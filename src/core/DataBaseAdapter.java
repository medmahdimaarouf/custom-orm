package core;

import core.queries.Query;

/**
 * A generic interface for database adapters that execute DML queries.
 *
 * @param <C> The type of the database connection handled by the adapter.
 * @param <R> The type of the result returned after executing the query.
 */
public interface DataBaseAdapter<C, R> extends AutoCloseable {

    /**
     * Returns the underlying database connection instance.
     *
     * @return the connection object of type {@code C}.
     */
    C getConnection();

    /**
     * Executes a given built {@link Query} and returns the result as an object of type {@code R}.
     *
     * @param query the query to execute.
     * @return the result of the query execution.
     * @throws Throwable if any error occurs during execution.
     */
    R execute(Query query) throws Throwable;
}
