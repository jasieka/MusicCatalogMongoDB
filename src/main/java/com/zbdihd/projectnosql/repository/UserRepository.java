package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    List<User> findByUsernameStartsWithIgnoreCase(String username);

}