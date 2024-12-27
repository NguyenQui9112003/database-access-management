package org.example;

import org.example.Facade.DatabaseControl;
import org.example.config.DatabaseConfig;
import org.example.entity.User;

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

            // delete (DUY)

            // select having group by (DUY)

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

        // delete (DUY)

        // close connect
    }
}