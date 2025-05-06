import core.clauses.OrderBy;
import core.clauses.Where;
import core.queries.Query;
import org.junit.Before;
import org.junit.Test;
import sql.SQLAdapter;
import sql.SQLQueryBuilder;

import static org.junit.Assert.*;

public class SQLQueryBuilderTest {


    private SQLQueryBuilder builder;

    @Before
    public void setUp() {
        this.builder = SQLAdapter.connect("localhost", 3306, "my-db", "root").getBuilder();
    }

    @Test
    public void testBasicSelect() {
        Query query = builder.select("id", "name").from("users").build();
        assertEquals("SELECT id, name FROM users", query.getNativeQuery());
    }

    @Test
    public void testWildcardSelect() {
        Query query = builder.select("*").from("users").build();
        assertEquals("SELECT * FROM users", query.getNativeQuery());
    }

    @Test
    public void testSelectWithSingleWhere() {
        Query query = builder
                .select("*")
                .from("users")
                .where(Where.equal("country", "TN"))
                .build();

        assertEquals("SELECT * FROM users WHERE country = 'TN'", query.getNativeQuery());
    }

    @Test
    public void testSelectWithMultipleWhere() {
        Query query = builder
                .select("id")
                .from("users")
                .where(Where.lessThan("age", 30))
                .where(Where.equal("active", true))
                .build();

        assertEquals("SELECT id FROM users WHERE age < 30 AND active = true", query.getNativeQuery());
    }

    @Test
    public void testSelectWithOrderBy() {
        Query query = builder
                .select("*")
                .from("users")
                .orderBy("created_at", OrderBy.OrderDirection.DESC)
                .build();

        assertEquals("SELECT * FROM users ORDER BY created_at DESC", query.getNativeQuery());
    }

    @Test
    public void testSelectWithMultipleOrderBy() {
        Query query = builder
                .select("*")
                .from("users")
                .orderBy("score", OrderBy.OrderDirection.DESC)
                .orderBy("age", OrderBy.OrderDirection.ASC)
                .build();

        assertEquals("SELECT * FROM users ORDER BY score DESC , age ASC", query.getNativeQuery());
    }

    @Test
    public void testSelectWithLimit() {
        Query query = builder
                .select("*")
                .from("users")
                .limit(10)
                .build();

        assertEquals("SELECT * FROM users LIMIT 10", query.getNativeQuery());
    }

    @Test
    public void testSelectWithAllClauses() {
        Query query = builder
                .select("id", "name", "score")
                .from("users")
                .where(Where.greaterThan("score", 70))
                .where(Where.equal("country", "TN"))
                .orderBy("score", OrderBy.OrderDirection.DESC)
                .limit(5)
                .build();

        String expected = "SELECT id, name, score FROM users WHERE score > 70 AND country = 'TN' ORDER BY score DESC LIMIT 5";
        assertEquals(expected, query.getNativeQuery());
    }

    @Test
    public void testSelectWithQuotedFields() {
        Query query = builder
                .select("`select`", "`from`")
                .from("`table`")
                .build();

        assertEquals("SELECT `select`, `from` FROM `table`", query.getNativeQuery());
    }

    @Test
    public void testWhereWithDifferentValueTypes() {
        Query query = builder
                .select("*")
                .from("users")
                .where(Where.equal("age", 25))
                .where(Where.equal("verified", true))
                .where(Where.equal("name", "Alice"))
                .build();

        String expected = "SELECT * FROM users WHERE age = 25 AND verified = true AND name = 'Alice'";
        assertEquals(expected, query.getNativeQuery());
    }

}
