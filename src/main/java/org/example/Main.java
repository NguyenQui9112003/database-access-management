package org.example;

import org.example.facade.DatabaseControl;
import org.example.config.dbconfig.PostgresConfig;

import org.example.entity.User;

public class Main {
    public static void main(String[] args) throws Exception {
        // Original PostgreSQL config
        //  DatabaseConfig postgresConfig = new DatabaseConfig(
        //          "jdbc:postgresql://localhost:5432",
        //          "DAM-Framework",
        //          "postgres",
        //          "123456");

       PostgresConfig postgresConfig = new PostgresConfig(
           "jdbc:postgresql://localhost:5432",
           "testDB",
           "postgres",
           "123");

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
            //db.createTable(User.class);
            User user = db.get(User.class, 1L);
            if (user != null) {
                System.out.println(user);
                db.set(User.class, user.getId(), "age", 25);
                user = db.get(User.class, 1L);
                System.out.println(user);
                user.setAge(26);
                db.save(user);
                user = db.get(User.class, 1L);
                System.out.println(user);
                db.delete(user);
                user = db.get(User.class, 1L);
                System.out.println(user);
            }
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
    }
}