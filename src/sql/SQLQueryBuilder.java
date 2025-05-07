package sql;

import core.QueryBuilder;
import core.clauses.OrderBy;
import core.clauses.Where;
import sql.queries.SQLDeleteQuery;
import sql.queries.SQLInsertQuery;
import sql.queries.SQLSelectQuery;
import sql.queries.SQLUpdateQuery;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SQLQueryBuilder is an implementation of the QueryBuilder interface for building SQL queries.
 * It supports creating SELECT, UPDATE, DELETE, and INSERT queries with various clauses.
 */
public final class SQLQueryBuilder implements QueryBuilder {

    /**
     * Builds a SELECT query.
     *
     * @param args The columns to be selected.
     * @return The SQLSelectQuery object representing the SELECT query.
     */
    @Override
    public SQLSelectQuery select(String... args) {
        return new SQLSelectQuery(args) {
            @Override
            public SQLSelectQuery build() {
                StringBuilder sql = new StringBuilder();
                sql.append("SELECT ");
                if (this.args.isEmpty()) {
                    sql.append("*");
                } else {
                    sql.append(String.join(", ", args));
                }
                if (!targets.isEmpty()) {
                    sql.append(" FROM ");
                    sql.append(targets.entrySet().stream()
                            .map(entry -> entry.getValue() == null ? entry.getKey() : entry.getKey() + " AS " + entry.getValue())
                            .collect(Collectors.joining(", ")));
                }
                if (!wheres.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < wheres.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        sql.append(buildClause(wheres.get(i)));
                    }
                }
                if (!this.orderBy.isEmpty()) {
                    sql.append(" ORDER BY ").append(String.join(" , ", this.orderBy.stream().map(e -> buildClause(e)).collect(Collectors.toList())));
                }
                if (this.limit.isPresent()) {
                    sql.append(" LIMIT ").append(String.valueOf(this.limit.get().getAmount()));
                }
                this.nativeQuery = Optional.of(sql);
                return this;
            }
        };
    }

    /**
     * Builds an UPDATE query.
     *
     * @param table The table to update.
     * @return The SQLUpdateQuery object representing the UPDATE query.
     */
    @Override
    public SQLUpdateQuery update(String table) {
        return new SQLUpdateQuery(table) {
            @Override
            public SQLUpdateQuery build() {
                StringBuilder sql = new StringBuilder();
                sql.append("UPDATE ").append(table).append(" SET ");
                sql.append(this.wheres.stream()
                        .map(entry -> entry.getValue() + " = ?")
                        .collect(Collectors.joining(", ")));
                if (!wheres.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < wheres.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        sql.append(buildClause(wheres.get(i)));
                    }
                }
                this.nativeQuery = Optional.of(sql);
                return this;
            }
        };
    }

    /**
     * Builds a DELETE query.
     *
     * @param table The table to delete from.
     * @return The SQLDeleteQuery object representing the DELETE query.
     */
    @Override
    public SQLDeleteQuery delete(String table) {
        return new SQLDeleteQuery(table) {
            @Override
            public SQLDeleteQuery build() {
                StringBuilder sql = new StringBuilder();
                sql.append("DELETE FROM ").append(table);
                if (!this.wheres.isEmpty()) {
                    sql.append(" WHERE ");
                    for (int i = 0; i < this.wheres.size(); i++) {
                        if (i > 0) sql.append(" AND ");
                        sql.append(buildClause(wheres.get(i)));
                    }
                }
                this.nativeQuery = Optional.of(sql);
                return this;
            }
        };
    }

    /**
     * Builds an INSERT query.
     *
     * @return The SQLInsertQuery object representing the INSERT query.
     */
    @Override
    public SQLInsertQuery insert() {
        return new SQLInsertQuery() {
            @Override
            public SQLInsertQuery build() {
                StringBuilder sql = new StringBuilder();
                sql.append("INSERT INTO ").append(this.target).append(" (");
                sql.append(String.join(", ", this.values.keySet()));
                sql.append(") VALUES (");
                sql.append(this.values.values().stream().map(v -> "?").collect(Collectors.joining(", ")));
                sql.append(")");
                this.nativeQuery = Optional.of(sql);
                return this;
            }
        };
    }

    /**
     * Builds a WHERE clause based on the provided condition.
     *
     * @param where The Where object containing the condition.
     * @return The string representation of the WHERE clause.
     */
    private String buildClause(Where where) {
        StringBuilder sb = new StringBuilder();
        if (where.getOperator() == Where.Operator.RAW) {
            sb.append(where.getRawExpression());
        } else {
            sb.append(where.getField());
            switch (where.getOperator()) {
                case EQUAL:
                    sb.append(" = ").append(formatValue(where.getValue()));
                    break;
                case NOT_EQUAL:
                    sb.append(" != ").append(formatValue(where.getValue()));
                    break;
                case GREATER_THAN:
                    sb.append(" > ").append(formatValue(where.getValue()));
                    break;
                case GREATER_THAN_OR_EQUAL:
                    sb.append(" >= ").append(formatValue(where.getValue()));
                    break;
                case LESS_THAN:
                    sb.append(" < ").append(formatValue(where.getValue()));
                    break;
                case LESS_THAN_OR_EQUAL:
                    sb.append(" <= ").append(formatValue(where.getValue()));
                    break;
                case IS_NULL:
                    sb.append(" IS NULL");
                    break;
                case IS_NOT_NULL:
                    sb.append(" IS NOT NULL");
                    break;
                case IN:
                    sb.append(" IN (")
                            .append(where.getValues().stream()
                                    .map(this::formatValue)
                                    .collect(Collectors.joining(", ")))
                            .append(")");
                    break;
                case NOT_IN:
                    sb.append(" NOT IN (")
                            .append(where.getValues().stream()
                                    .map(this::formatValue)
                                    .collect(Collectors.joining(", ")))
                            .append(")");
                    break;
                case BETWEEN:
                    sb.append(" BETWEEN ")
                            .append(formatValue(where.getValues().get(0)))
                            .append(" AND ")
                            .append(formatValue(where.getValues().get(1)));
                    break;
                case LIKE:
                    sb.append(" LIKE ").append(formatValue(where.getValue()));
                    break;
                case NOT_LIKE:
                    sb.append(" NOT LIKE ").append(formatValue(where.getValue()));
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Builds a OrderBy clause based on the provided instance.
     *
     * @param orderBy The OrderBy object containing the sorting args.
     * @return The string representation of the OrderBy clause.
     */
    private String buildClause(OrderBy orderBy) {
        return orderBy.getColumnOrExpression().concat(" ").concat(orderBy.getDirection().orElse(OrderBy.OrderDirection.ASC).getValue());
    }

    /**
     * Formats the value for SQL queries.
     *
     * @param value The value to format.
     * @return The formatted value as a string.
     */
    private String formatValue(Object value) {
        if (value instanceof Number) {
            return value.toString();
        } else if (value instanceof Boolean) {
            return (Boolean) value ? "true" : "false";
        } else if (value == null) {
            return "NULL";
        } else {
            String str = value.toString().replace("'", "''");
            return "'" + str + "'";
        }
    }
}
