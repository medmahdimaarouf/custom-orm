package core.queries;

import java.util.Objects;
import java.util.Optional;

/**
 * The base abstract class for all ORM queries.
 * <p>
 * This class provides a generic structure to build and retrieve the native SQL string representation.
 * All specific query types (SELECT, INSERT, UPDATE, DELETE) should extend this class.
 *
 * @param <Q> The specific query subclass type extending this base class.
 */
public abstract class Query<Q extends Query<Q>> {

    /**
     * The internal representation of the final native SQL query.
     */
    protected Optional<StringBuilder> nativeQuery;

    /**
     * Builds and finalizes the query. Implementations should construct the native SQL string.
     *
     * @return the built query instance.
     */
    public abstract Query<Q> build();

    /**
     * Checks if the query has been build
     *
     * @return true if the query is built with its query builder otherwise false .
     */
    public final boolean isBuilt() {
        return this.nativeQuery.isPresent();
    }

    /**
     * Returns the native query string built by its query builder .
     *
     * @return an {@link String} present the native query string if built, or empty if not yet built.
     */
    public final String getNativeQuery() {
        return this.nativeQuery.isPresent() ? nativeQuery.get().toString() : null;
    }


}
