package com.germanvoc.server.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "words")
public class Word {

    @Id
    @GeneratedValue (strategy =  GenerationType.IDENTITY)
    int wId;

    private String original;
    private String translation;
    private String tag;

    @ManyToMany(mappedBy = "words")
    private Set<User> users;

    public Word(){
        isPresent = 0;
        users = new HashSet<>();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    private int isPresent;

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public int getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(int isPresent) {
        this.isPresent = isPresent;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id=" + wId +
                ", origWord='" + original + '\'' +
                ", translation='" + translation + '\'' +
                ", tag='" + tag + '\'' +
                ", user=" + users +
                ", isPresent=" + isPresent +
                '}';
    }
}
