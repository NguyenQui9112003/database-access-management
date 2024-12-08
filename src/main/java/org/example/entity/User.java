package org.example.entity;
import org.example.annotations.*;

@Entity
@Table(name = "users_test")
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "phone")
    private int phone;

    // constructor
    public User(Long id, String name, int phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }
    
}
