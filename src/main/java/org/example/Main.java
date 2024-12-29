package org.example;

import org.example.Facade.DatabaseControl;
import org.example.config.DatabaseConfig;
import org.example.entity.User;
import org.example.repository.builder.QueryBuilder;
import org.example.repository.builder.SQLQueryBuilder;

import java.beans.Statement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        DatabaseConfig dbConfig = new DatabaseConfig (
                "jdbc:postgresql://localhost:5432",
                "DAM-Framework",
                "postgres",
                "123456");

        // create service PostgresSQL with Facade
        try {
            DatabaseControl db = new DatabaseControl(dbConfig, "postgres");
            db.createTable(User.class);

            // insert
            // User user_test = new User(4L, "Haha haha", "qui@gmail.com", 13);
            // db.insert(user_test);

            // update
            // user_test.setAge(21);
            // db.update(user_test);
            // db.updateFieldWithValue();

            // select having group by (DUY)
            System.out.println("Selecting records...");
            List<Object[]> results = db.select(
                    User.class,
                    List.of("id", "name", "age"),
                    "age > 20",         // WHERE condition
                    List.of("age"),     // GROUP BY
                    "COUNT(*) >= 1"     // HAVING condition
            );

            for (Object[] row : results) {
                System.out.println(Arrays.toString(row));
            }

            //delete
            System.out.println("Deleting a record...");
            db.delete(User.class, "id = 2");

            // Re-select to verify deletion
            System.out.println("Selecting records after deletion...");
            results = db.select(
                    User.class,
                    List.of("id", "name", "age"),
                    null,               // No WHERE condition
                    null,               // No GROUP BY
                    null                // No HAVING condition
            );

            for (Object[] row : results) {
                System.out.println(Arrays.toString(row));
            }

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