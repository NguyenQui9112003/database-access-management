package org.example;

import java.util.Arrays;
import java.util.List;

import org.example.Facade.DatabaseControl;
import org.example.config.DatabaseConfig;

import org.example.entity.Profile;
import org.example.entity.User;
import org.example.entity.Stories;

public class Main {
    public static void main(String[] args) throws Exception {
        // Original PostgreSQL config
         DatabaseConfig postgresConfig = new DatabaseConfig(
                 "jdbc:postgresql://localhost:5432",
                 "DAM-Framework",
                 "postgres",
                 "123456");

//        DatabaseConfig postgresConfig = new DatabaseConfig(
//            "jdbc:postgresql://localhost:5432",
//            "testDB",
//            "postgres",
//            "123");

        // Adapter to convert PostgreSQL config to MySQL config
        // PostgresToMySQLAdapter adapter = new PostgresToMySQLAdapter(postgresConfig);
        // DatabaseConfig mysqlConfig = new DatabaseConfig(
        //         "jdbc:mysql://localhost:3306",
        //         "user",
        //        "root",
        //         adapter.getPassword());


        // create service PostgresSQL with Facade
        try {
            DatabaseControl db = new DatabaseControl(postgresConfig, "postgres");

            // create table relation
            db.createTable(User.class);
//            db.createTable(Profile.class);
//            db.createTable(Stories.class);

//            db.createRelationships(User.class);
//            db.createRelationships(Profile.class);
//            db.createRelationships(Stories.class);

//            // Example usage
//            List<User> users = Arrays.asList(
//                new User(1L, "Alice", "thai123@mgial.com", 20),
//                new User(2L, "Bob", "thai222@gmail.com", 21)
//
//            );


//            db.insertBulk(users);
            //db.createTable(User.class);
            //DatabaseControl db = new DatabaseControl(mysqlConfig, "mysql");
            //db.createTable(User.class);
            // insert User
            // User user = new User(1L, "Duy", "thai@gmail.com", 21);
            //db.insert(user);
            // user.setAge(22);
            // db.update(user);
            // console log sqlstatement
            // user = new User(2L, "thai", "thai123cm@gmail.com", 22);
            // db.insert(user);
            // // insert
            // db update
            // user.setAge(23);
            // db.update(user);
            // User user_test = new User(4L, "Haha haha", "qui@gmail.com", 13);
            // db.insert(user_test);

            // update
            // user_test.setAge(21);
            // db.update(user_test);
            // db.updateFieldWithValue();

            // select having group by (DUY)
//            System.out.println("Selecting records...");
//            List<Object[]> results = db.select(
//                    User.class,
//                    List.of("id", "username", "age"),
//                    "age > 20",         // WHERE condition
//                    List.of("id", "username", "age"),     // GROUP BY
//                    "COUNT(*) >= 1"     // HAVING condition
//            );
//
//            for (Object[] row : results) {
//                System.out.println(Arrays.toString(row));
//            }

            //delete
//            System.out.println("Deleting a record...");
//            db.delete(User.class, "id = 2");

            // Re-select to verify deletion
//            System.out.println("Selecting records after deletion...");
//            List<Object[]> results = db.select(
//                    User.class,
//                    List.of("id", "username", "age"),
//                    null,               // No WHERE condition
//                    null,               // No GROUP BY
//                    null                // No HAVING condition
//            );

//            for (Object[] row : results) {
//                System.out.println(Arrays.toString(row));
//            }

            db.closeConnect();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~//

        //*** use builder to create query for controlling database
        // create table
        /*
        QueryBuilder query = new SQLQueryBuilder();
        String createTableQuery = query.createTable("test_table")
                .addPrimaryKeyColumn("id", "BIGINT")
                .addColumn("name", "VARCHAR(255)")
                .addColumn("age", "INTEGER")
                .appendComma(");")
                .build();
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(createTableQuery);
            System.out.println(createTableQuery);
        } catch (Exception e) {
            System.out.println(e);
        }
        */

        // insert
        /*
        QueryBuilder query = new SQLQueryBuilder();
        String insertQuery = query.insert("test_table", "id", "name", "age")
                .values(2, "i love design pattern", 7)
                .build();
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(insertQuery);
            System.out.println(insertQuery);
        } catch (Exception e) {
            System.out.println(e);
        }
        */

        // update
        /*
        QueryBuilder query = new SQLQueryBuilder();
        String updateQuery = query.update("test_table")
                .set("name = 'i love you'", "age = 3")
                .where("id = 2")
                .build(); //
        try (Statement stmt = con.createStatement()) {
            stmt.executeUpdate(updateQuery);
            System.out.println(updateQuery);
        } catch (Exception e) {
            System.out.println(e);
        }
        */

        // select having group by (DUY)
//        QueryBuilder query = new SQLQueryBuilder();
//        String selectQuery = query.select("id", "COUNT(*) AS total", "age")
//                .from("test_table")
//                .groupBy("age")
//                .having("total > 1")
//                .build();
//
//        try (Statement stmt = con.createStatement();
//             ResultSet rs = stmt.executeQuery(selectQuery)) {
//
//            System.out.println(selectQuery);
//
//            while (rs.next()) {
//                long id = rs.getLong("id");
//                int total = rs.getInt("total");
//                int age = rs.getInt("age");
//                System.out.printf("ID: %d, Total: %d, Age: %d%n", id, total, age);
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        // delete (DUY)
//        QueryBuilder query = new SQLQueryBuilder();
//        String deleteQuery = query.delete()
//                .from("test_table")
//                .where("id = 2")
//                .build();
//
//        try (Statement stmt = con.createStatement()) {
//            stmt.executeUpdate(deleteQuery);
//            System.out.println(deleteQuery);
//        } catch (Exception e) {
//            System.out.println(e);
//        }


        // close connect
    }
}