package org.example.entity;

import org.example.annotations.*;

@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @Column(name = "id", primaryKey = true)
    private Long id;

    @Column(name = "bio")
    private String bio;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToOne(referencedTable = "users", foreignKey = "id")
    private User user;

    // Constructor
    public Profile(Long id, String bio, String avatarUrl) {
        this.id = id;
        this.bio = bio;
        this.avatarUrl = avatarUrl;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
