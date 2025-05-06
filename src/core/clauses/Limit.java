package core.clauses;

/**
 * Represents an SQL "LIMIT" clause that restricts the number of rows returned by a query.
 */
public class Limit implements Clause {
    private final int amount;

    /**
     * Constructs a Limit clause with a specified number of rows.
     * <p>
     * If the provided amount is less than or equal to 0, the limit defaults to 0 (no rows).
     *
     * @param amount the maximum number of rows to return; must be greater than 0 to take effect
     */
    public Limit(final int amount) {
        this.amount = amount > 0 ? amount : 0;
    }

    /**
     * Returns the number of rows specified in the LIMIT clause.
     *
     * @return the row limit amount
     */
    public int getAmount() {
        return amount;
    }
}
