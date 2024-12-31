package org.example.entity;

import java.util.List;
import org.example.annotations.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(name = "id", primaryKey = true)
    private Long id; // Make sure this matches the column name in database

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "age")
    private Integer age;

    @OneToOne(referencedTable = "profiles", foreignKey = "id")
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private List<Stories> stories;

    // Add no-argument constructor
    public User() {
        // Default constructor required for reflection
    }

    // Constructor
    public User(Long id, String username, String email, Integer age) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.age = age;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) { this.age = age; }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Stories> getStories() {
        return stories;
    }

    public void setStories(List<Stories> stories) {
        this.stories = stories;
    }

    @Override
    public String toString() {
        return "User{" +
               "id=" + id +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               ", age=" + age +
               '}';
    }
}
