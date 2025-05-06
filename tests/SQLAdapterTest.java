import core.exceptions.ConnectionClosedException;
import core.exceptions.QueryNotBuiltException;
import core.queries.Query;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sql.SQLAdapter;
import sql.queries.SQLSelectQuery;

import java.sql.*;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SQLAdapterTest {

    private Connection mockConnection;
    private Statement mockStatement;
    private ResultSet mockResultSet;
    private ResultSetMetaData mockMetaData;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(Statement.class);
        mockResultSet = mock(ResultSet.class);
        mockMetaData = mock(ResultSetMetaData.class);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
    }

    @After
    public void tearDown() {
        mockConnection = null;
        mockStatement = null;
        mockResultSet = null;
        mockMetaData = null;
    }

    // Use reflection to access the private constructor
    private SQLAdapter createAdapterWithMockConnection() throws Exception {
        java.lang.reflect.Constructor<SQLAdapter> constructor = SQLAdapter.class.getDeclaredConstructor(Connection.class);
        constructor.setAccessible(true);
        return constructor.newInstance(mockConnection);
    }

    @Test
    public void testGetBuilderReturnsNonNull() throws Exception {
        SQLAdapter adapter = createAdapterWithMockConnection();
        assertNotNull(adapter.getBuilder());
    }

    @Test
    public void testCloseClosesOpenConnection() throws Exception {
        when(mockConnection.isClosed()).thenReturn(false);
        SQLAdapter adapter = createAdapterWithMockConnection();
        adapter.close();
        verify(mockConnection, times(1)).close();
    }

    @Test
    public void testCloseDoesNothingIfAlreadyClosed() throws Exception {
        when(mockConnection.isClosed()).thenReturn(true);
        SQLAdapter adapter = createAdapterWithMockConnection();
        adapter.close();
        verify(mockConnection, never()).close();
    }

    @Test(expected = QueryNotBuiltException.class)
    public void testExecuteThrowsIfQueryNotBuilt() throws Exception {
        SQLAdapter adapter = createAdapterWithMockConnection();
        Query mockQuery = mock(Query.class);
        when(mockQuery.isBuilt()).thenReturn(false);
        adapter.execute(mockQuery);
    }

    @Test(expected = ConnectionClosedException.class)
    public void testExecuteThrowsIfConnectionClosed() throws Exception {
        SQLAdapter adapter = createAdapterWithMockConnection();
        Query mockQuery = mock(Query.class);
        when(mockQuery.isBuilt()).thenReturn(true);
        when(mockConnection.isClosed()).thenReturn(true);
        adapter.execute(mockQuery);
    }

    @Test
    public void testExecuteSelectQueryReturnsMap() throws Exception {
        SQLAdapter adapter = createAdapterWithMockConnection();

        Query mockQuery = mock(SQLSelectQuery.class);
        when(mockQuery.isBuilt()).thenReturn(true);
        when(mockQuery.getNativeQuery()).thenReturn("SELECT * FROM users");

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockStatement.executeQuery("SELECT * FROM users")).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getMetaData()).thenReturn(mockMetaData);
        when(mockMetaData.getColumnCount()).thenReturn(1);
        when(mockMetaData.getColumnLabel(1)).thenReturn("id");
        when(mockResultSet.getObject(1)).thenReturn(123);

        Map<String, Object> result = adapter.execute(mockQuery);
        assertEquals(1, result.size());
        assertEquals(123, result.get("id"));
    }

    @Test
    public void testExecuteUpdateQueryReturnsAffectedRows() throws Exception {
        SQLAdapter adapter = createAdapterWithMockConnection();

        Query mockQuery = mock(Query.class);

        when(mockQuery.isBuilt()).thenReturn(true);
        when(mockQuery.getNativeQuery()).thenReturn("DELETE FROM users");

        when(mockConnection.isClosed()).thenReturn(false);
        when(mockStatement.executeUpdate("DELETE FROM users")).thenReturn(3);

        Map<String, Object> result = adapter.execute(mockQuery);
        assertEquals(1, result.size());
        assertEquals(3, result.get("affectedRows"));
    }
}
