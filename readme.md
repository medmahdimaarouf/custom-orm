# SQLAdapter – ORM for any database type

My Database `Object Relational Mapping` package is a simple Java package project designed to provide a flexible adapter for different database types. The core abstraction layer allows easy integration with various databases by implementing the appropriate database-specific functionality.
The project currently includes an example for SQL-based databases, but the adapter can be easily extended to support other database types such as MongoDB, PostgreSQL, and more.
---
## 🧩 Core Abstraction Layer
The core abstraction layer in this project is designed to be flexible and can be adapted to various database systems. The SQLAdapter class is a sample implementation for SQL-based databases, but the architecture supports the creation of similar adapters for other database types.

To implement support for another database type:

- **1- Extend the `DatabaseAdapter` class:** Create a class that extends the DatabaseAdapter and implement the methods specific to your database type (e.g., connect, execute, etc.).

- **2- Implement Database-Specific Logic:** Inside your new adapter class, implement the database-specific logic to connect, execute queries, and handle errors.

- **3- Use the Adapter:** You can then use the new database adapter just like the SQL adapter in your application.

## 🎯 Goals

- Minimal setup: **No Maven, no Gradle**, just pure Java.
- Clean and fluent API for building SQL queries.
- Easily extensible for any DB backend (PostgreSQL, SQLite, Oracle, etc.).
- Core abstraction separates query building and execution logic.
- Designed for **custom adapters**, with SQL provided as a reference.

---

## 🧰 Structure
- /**src**
- ├── **core** #DB-agnostic abstractions and base interfaces
- ├── **sql** #SQL-specific query builder & adapter implementation
- / **tests** #JUnit 4 test cases

---

## ✅ Features

- ✓ Easy and fluent query builder for principal `CRUD` queries
- ✓ Support for Clauses:
    - Multiple `WHERE` conditions
    - `ORDER BY` clauses
    - `LIMIT`
- ✓ Everything is reusable .
- ✓ Executable queries with results returned as customized type
- ✓ Designed with extensibility in mind (just implement your own and go )
- X SQL package is not supporting complicated joins  ( left  , right ) yet :( inner is made by default but u can use custom expression features insted  
---

## 🚀 Getting Started
**1. Clone the Repository**
```
git clone https://github.com/medmahdimaarouf/custom-orm.git
cd custom-orm
```
**2. Compile the Project**

Make sure you have the necessary dependencies (JUnit and Hamcrest) in the `bin/` folder. Then, use the provided `build.sh` script to compile and run the project.
```
chmod +x build.sh
./build.sh
```
**3. Creating a New Adapter**

To create a new adapter for a different database, follow these steps:

**1. Create a New Adapter Class:**

Create a new class that extends `DatabaseAdapter`.

Implement the database connection and query execution logic specific to the database you want to support.

Example:

```java
public class MongoAdapter extends DatabaseAdapter<MongoConnection,JsonObject> {
    // Implement MongoDB-specific methods

    @Override
    public void connect(String host, int port, String dbName, String user) {
        // MongoDB-specific connection logic
    }

    @Override
    public JsonObject execute(Query query) {
        // MongoDB query execution logic
        return XXXX;
    }
}
```

After implementing your adapter, write tests to ensure it functions as expected. You can follow the same structure as the existing SQL tests.


**2. Test Your Adapter:**

```
@Test
public void testMongoAdapterConnection() {
    MongoAdapter adapter = new MongoAdapter();
    adapter.connect("localhost", 27017, "mydb", "root");
    // Test MongoDB-specific functionality
}
```

**3. Integrate the New Adapter:**

Use your new adapter in the same way as the SQLAdapter in the main application.

```java
public static void main(String[] args) {
    try (SQLAdapter adapter = SQLAdapter.connect("localhost", 3306, "my-db", "root")) {
        SQLQueryBuilder builder = adapter.getBuilder();

        Query query = builder.select("id", "first_name", "last_name", "score")
            .from("users")
            .where(Where.lessThanOrEqual("age", 27))
            .where(Where.equal("country_iso", "TN"))
            .where(Where.greaterThan("score", 50))
            .orderBy("age", OrderBy.OrderDirection.DESC)
            .orderBy("score", OrderBy.OrderDirection.ASC)
            .limit(25)
            .build();

        System.out.println(query.getNativeQuery());
        Map<String, Object> result = adapter.execute(query);
        System.out.println(result);
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
```
## 🧩 How to Add Support for a New DB
To add support for a new database type:

Implement your own adapter extending the core interfaces.

Provide a query builder that matches your DB’s syntax.

Reuse or extend Query, Where, and OrderBy if needed.

The provided SQL implementation (sql package) serves as a reference.

## 🧪 Testing
JUnit 4 test classes are provided for both:

- SQLAdapter

- SQLQueryBuilder

Run them with your favorite IDE :))

## 📄 License
This project is released under the MIT License.

Designed to be simple, hackable, and extensible. Built with clean Java and no dependencies.

No restrictions free for everything , it made with minimal tests even xd , just take it and fuck off :xd


