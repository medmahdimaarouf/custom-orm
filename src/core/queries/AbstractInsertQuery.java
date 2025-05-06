package core.queries;

import java.util.*;

/**
 * AbstractInsertQuery provides a base structure for building SQL INSERT queries.
 * It supports setting the target table and inserting single or multiple column-value pairs.
 *
 * @param <Q> The concrete query type extending this class.
 */
public abstract class AbstractInsertQuery<Q extends Query> extends Query<AbstractInsertQuery<Q>> {

    /**
     * A map holding the column-value pairs to be inserted.
     */
    protected Map<String, Object> values = new HashMap<>();

    /**
     * The name of the target table into which data will be inserted.
     */
    protected Optional<String> target = Optional.empty();

    /**
     * Specifies the target table for the INSERT operation.
     *
     * @param target The name of the table.
     * @return The current query instance.
     */
    public Q into(String target) {
        this.target = Optional.of(target);
        return (Q) this;
    }

    /**
     * Adds a single column-value pair to the INSERT query.
     *
     * @param column The name of the column.
     * @param value  The value to insert into the column.
     * @return The current query instance.
     */
    public Q value(String column, Object value) {
        this.values.put(column, value);
        return (Q) this;
    }

    /**
     * Adds multiple column-value pairs to the INSERT query.
     *
     * @param values A map of column names to their corresponding values.
     * @return The current query instance.
     */
    public Q values(Map<String, Object> values) {
        this.values.putAll(values);
        return (Q) this;
    }

    /**
     * Builds and finalizes the INSERT query.
     *
     * @return The built query instance.
     */
    @Override
    public abstract Q build();
}
