package sql;

import core.DataBaseAdapter;
import core.exceptions.ConnectionClosedException;
import core.exceptions.QueryNotBuiltException;
import core.queries.AbstractSelectQuery;
import core.queries.Query;
import sql.queries.SQLSelectQuery;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A SQL Adapter for connecting and interacting with a MySQL database.
 * <p>
 * This class provides methods for establishing a connection to a MySQL database and executing queries using
 * SQL statements. It implements the {@link DataBaseAdapter} interface and can be used to perform various
 * database operations such as SELECT, INSERT, UPDATE, and DELETE.
 * </p>
 */
public class SQLAdapter implements DataBaseAdapter<Connection, Map<String, Object>> {
    private final Optional<Connection> connection;

    /**
     * Constructs an {@code SQLAdapter} with an existing database {@link Connection}.
     *
     * @param connection The connection to the database.
     */
    private SQLAdapter(final Connection connection) {
        this.connection = Optional.of(connection);
    }

    /**
     * Returns a new {@link SQLQueryBuilder} instance to construct SQL queries.
     *
     * @return A new {@link SQLQueryBuilder} instance.
     */
    public SQLQueryBuilder getBuilder() {
        return new SQLQueryBuilder();
    }

    /**
     * Establishes a connection to the database using the provided connection details.
     *
     * @param host     The database host.
     * @param port     The database port.
     * @param database The name of the database.
     * @param username The username to authenticate with.
     * @param password The password for the username.
     * @return A new {@link SQLAdapter} connected to the specified database.
     */
    public static SQLAdapter connect(String host, int port, String database, String username, String password) {
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false", host, port, database);
        return connect(url, username, password);
    }

    /**
     * Establishes a connection to the database with a default empty password.
     *
     * @param host     The database host.
     * @param port     The database port.
     * @param database The name of the database.
     * @param username The username to authenticate with.
     * @return A new {@link SQLAdapter} connected to the specified database.
     */
    public static SQLAdapter connect(String host, int port, String database, String username) {
        return connect(host, port, database, username, "");
    }

    /**
     * Establishes a connection to the database with default localhost as the host.
     *
     * @param port     The database port.
     * @param database The name of the database.
     * @param username The username to authenticate with.
     * @param password The password for the username.
     * @return A new {@link SQLAdapter} connected to the specified database.
     */
    public static SQLAdapter connect(int port, String database, String username, String password) {
        return connect("localhost", port, database, username, password);
    }

    /**
     * Establishes a connection to the database with default localhost as the host,
     * port 3306, and the root user.
     *
     * @param database The name of the database.
     * @return A new {@link SQLAdapter} connected to the specified database.
     */
    public static SQLAdapter connect(String database) {
        return connect("localhost", 3306, database, "root", "");
    }

    /**
     * Establishes a connection to the database using the provided JDBC URL.
     *
     * @param url      The JDBC URL of the database.
     * @param username The username to authenticate with.
     * @param password The password for the username.
     * @return A new {@link SQLAdapter} connected to the specified database.
     */
    public static SQLAdapter connect(String url, String username, String password) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            return new SQLAdapter(conn);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found.", e);
        } catch (SQLException e) {
            throw new RuntimeException("Connection failed.", e);
        }
    }

    // === Executing query with parameters ===

    /**
     * Closes the connection to the database if it is not already closed.
     */
    @Override
    public void close() {
        try {
            if (connection.isPresent() && !connection.get().isClosed()) {
                connection.get().close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the underlying database connection.
     *
     * @return The database connection instance.
     */
    @Override
    public Connection getConnection() {
        return this.connection.get();
    }

    /**
     * Executes a given {@link Query} and returns the result as a {@link Map}.
     * <p>
     * The map returned contains the column names as keys and the corresponding column values as values.
     * </p>
     *
     * @param query The query to execute.
     * @return A map representing the result of the query execution.
     * @throws SQLException If an SQL exception occurs during execution.
     */
    @Override
    public Map<String, Object> execute(Query query) throws SQLException {
        if (!query.isBuilt()) {
            throw new QueryNotBuiltException("The query has not been built yet.");
        }

        if (this.connection.isPresent() && this.connection.get().isClosed()) {
            throw new ConnectionClosedException("The database connection is closed.");
        }

        // Initialize the result data map
        Map<String, Object> resultData = new HashMap<>();

        // Handle different types of queries based on whether they return a result set
        try (Statement statement = this.connection.get().createStatement()) {
            // Check if it's a SELECT query
            if (query instanceof SQLSelectQuery) {
                try (ResultSet resultSet = statement.executeQuery(query.getNativeQuery())) {
                    ResultSetMetaData metaData = resultSet.getMetaData();
                    int columnCount = metaData.getColumnCount();

                    if (resultSet.next()) { // If there is at least one row
                        for (int i = 1; i <= columnCount; i++) {
                            String columnName = metaData.getColumnLabel(i); // Prefer getColumnLabel() for alias support
                            Object columnValue = resultSet.getObject(i);
                            resultData.put(columnName, columnValue);
                        }
                    }
                }
            } else {
                // For non-SELECT queries (INSERT, UPDATE, DELETE), use executeUpdate
                int affectedRows = statement.executeUpdate(query.getNativeQuery());
                resultData.put("affectedRows", affectedRows);  // You can return the number of affected rows
            }
        }

        return resultData;
    }


}
