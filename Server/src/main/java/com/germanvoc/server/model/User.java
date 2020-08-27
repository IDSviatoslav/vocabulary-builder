package com.germanvoc.server.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uId;

    private String name;
    private String password;
    private String type;
    int handle;
    //private String Email;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "user_words", joinColumns = @JoinColumn(name ="user_id"), inverseJoinColumns = @JoinColumn(name ="words_id"))
    private Set<Word> words;

    public User(){
        words = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String uName) {
        this.name = uName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String uPassword) {
        this.password = uPassword;
    }

    public Set<Word> getWords() {
        return words;
    }

    public void setWords(Set<Word> words) {
        this.words = words;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHandle() {
        return handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    /* public String getEmail() {
        return Email;
    }

    public void setEmail(String uEmail) {
        this.Email = uEmail;
    }*/
}

