package org.example;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.example.builder.*;
import org.example.config.DatabaseConfig;
import org.example.connection.PostgresConnectionFactory;
import org.example.repository.DatabaseQueryAbstractFactory;
import org.example.repository.PostgresQueryConcrete;
import org.example.connectionManager.ConnectionManagerSingleton;
import org.example.service.DatabaseService;
import org.example.service.DatabaseServiceAbstractFactory;
import org.example.service.PostgresConcrete;

import org.example.annotations.*;
import org.example.repository.PostgresQueryGenerator;
import org.example.repository.QueryGenerator;
import org.example.repository.builder.QueryBuilder;

@Entity
@Table(name = "users")
class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "age")
    private Integer age;

    // Constructor
    public User(Long id, String username, String email, Integer age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    // Getters and setters
    public void setUsername(String username) {
        this.username = username;
    }
}

public class Main {
    public static void main(String[] args) throws SQLException {
        // Tạo một cái connection manager
        ConnectionManagerSingleton connectionManagerSingleton = ConnectionManagerSingleton.getInstance(10);

        // DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
        //         "DAM-Framework","postgres","123456");
        DatabaseConfig dbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5432",
                "testDB","postgres","123");
        PostgresConnectionFactory pgConnection = new PostgresConnectionFactory();

        // Từ factory này sẽ gọi một connection mới và kết nối vào trong database
        Connection con = connectionManagerSingleton.addConnection(pgConnection.createConnection(dbConfig));
        // Connection con = pgConnection.createConnection(dbConfig);

        // Tạo factory và service cho PostgresSQL
        DatabaseServiceAbstractFactory db = new PostgresConcrete();
        DatabaseQueryAbstractFactory query = new PostgresQueryConcrete();
        DatabaseService dbService = db.createDatabaseService(con, query);

        Statement statement = null;
        try {
            QueryGenerator queryGenerator = new PostgresQueryGenerator();
            statement = con.createStatement();

            // Create table
            // try {
            //     String createTableQuery = queryGenerator.createTableQuery(User.class);
            //     System.out.println("Executing Create Table Query:");
            //     System.out.println(createTableQuery);
            //     statement.executeUpdate(createTableQuery);
            //     System.out.println("Table created successfully\n");
            // } catch (SQLException e) {
            //     System.out.println("Create table failed: " + e.getMessage());
            // }

            // // Insert first user
            // try {
            //     User user1 = new User(1L, "john_doe", "john@example.com", 25);
            //     String insertQuery1 = queryGenerator.insertQuery(user1);
            //     System.out.println("Executing Insert Query for user1:");
            //     System.out.println(insertQuery1);
            //     statement.executeUpdate(insertQuery1);
            //     System.out.println("First user inserted successfully\n");
            // } catch (SQLException e) {
            //     System.out.println("Insert user1 failed: " + e.getMessage());
            // }

            // // Insert second user
            // try {
            //     User user2 = new User(2L, "jane_doe", "jane@example.com", 30);
            //     String insertQuery2 = queryGenerator.insertQuery(user2);
            //     System.out.println("Executing Insert Query for user2:");
            //     System.out.println(insertQuery2);
            //     statement.executeUpdate(insertQuery2);
            //     System.out.println("Second user inserted successfully\n");
            // } catch (SQLException e) {
            //     System.out.println("Insert user2 failed: " + e.getMessage());
            // }

            // // Example 4: Update first user's information
            // try {
            //     User user1 = new User(1L, "john_doe", "john@example.com", 25);
            //     user1.setUsername("john_updated");
            //     String updateQuery = queryGenerator.updateQuery(user1);
            //     System.out.println("Executing Update Query:");
            //     System.out.println(updateQuery);
            //     statement.executeUpdate(updateQuery);
            //     System.out.println("User updated successfully\n");
            // } catch (SQLException e) {
            //     System.out.println("Update user1 failed: " + e.getMessage());
            // }

            // Example 5: Update specific field
            try {
                String updateFieldQuery = queryGenerator.updateFieldWithValue(
                    User.class, "username", "john_final", "id", 1L
                );
                System.out.println("Executing Update Field Query:");
                System.out.println(updateFieldQuery);
                statement.executeUpdate(updateFieldQuery);
                System.out.println("Field updated successfully\n");
            } catch (SQLException e) {
                System.out.println("Update field failed: " + e.getMessage());
            }

          
           
            // Test Create Table query
            // TableDefinitionBuilder createTableBuilder = new CreateTableBuilder("users")
            //         .addPrimaryKeyColumn("id", "SERIAL")
            //         .addColumn("name", "VARCHAR(100)")
            //         .addColumn("age", "INT");
            // System.out.println("Create Table Query:");
            // System.out.println(createTableBuilder.build());

            // // Test Select query
            // ConditionQueryBuilder selectBuilder = new SelectQueryBuilder()
            //         .from("users")
            //         .where("age > 18")
            //         .groupBy("age")
            //         .having("COUNT(*) > 1");
            // System.out.println("\nSelect Query:");
            // System.out.println(selectBuilder.build());

            // // Test Delete query
            // ConditionQueryBuilder deleteBuilder = new DeleteQueryBuilder()
            //         .from("users")
            //         .where("age < 18");
            // System.out.println("\nDelete Query:");
            // System.out.println(deleteBuilder.build());

            // // Test Update query
            // UpdateQueryBuilder updateBuilder = new UpdateQueryBuilderImpl()
            //         .from("users")
            //         .set("name", "'John'")
            //         .set("age", "30")
            //         .where("id = 1");
            // System.out.println("\nUpdate Query:");
            // System.out.println(updateBuilder.build());

            // // Create users with different IDs - make sure ID and phone are different values
            // User user1 = new User(4L, "John Doe", 555111); // ID=1, phone=555111
            // //User user2 = new User(5L, "Jane Smith", 555222); // ID=2, phone=555222
            
            // // Insert users
            // // dbService.insert(user1);
            // // dbService.insert(user2);

  
            // user1.setPhone(555666);
            // dbService.update(user1);
          

            // // Update name to "Jane Doe" where phone = 555666
            // dbService.updateFieldWithValue(
            //     User.class,        // entity class
            //     "name",           // field to update
            //     "Jane Doe",       // new value
            //     "phone",          // where field
            //     555666           // where value
            // );
            
            // // Update phone to 999999 where id = 4
            // dbService.updateFieldWithValue(
            //     User.class,        // entity class
            //     "phone",          // field to update
            //     999999,           // new value
            //     "id",             // where field
            //     4L                // where value
            // );

        } catch (SQLException e) {
            System.out.println("Database operation failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
            if (con != null) {
                con.close();
            }
        }
    }
}