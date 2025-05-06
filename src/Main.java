import core.clauses.OrderBy;
import core.queries.Query;
import sql.SQLAdapter;
import sql.SQLQueryBuilder;
import core.clauses.Where;

import java.sql.SQLException;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try (SQLAdapter sqlAdapter = SQLAdapter.connect("localhost", 3306, "my-db", "root")) {
            SQLQueryBuilder queryBuilder = sqlAdapter.getBuilder();
            Query query = queryBuilder.select("id", "first_name", "last_name", "score")
                    .from("users")
                    .where(Where.lessThanOrEqual("age", 27))
                    .where(Where.equal("country_iso", "TN"))
                    .where(Where.greaterThan("score", 50))
                    .orderBy("age", OrderBy.OrderDirection.DESC)
                    .orderBy("score", OrderBy.OrderDirection.ASC)
                    .limit(25)
                    .build();
            System.out.println(query.getNativeQuery().toString());
            Map result = sqlAdapter.execute(query);
            System.out.println(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}