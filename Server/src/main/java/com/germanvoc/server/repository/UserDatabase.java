package com.germanvoc.server.repository;

import com.germanvoc.server.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserDatabase extends CrudRepository<User, Integer> {
    User findFirstByName(String name);
    User findFirstByHandle(int handle);
}
