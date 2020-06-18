package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);
    List<User> findByUsernameStartsWithIgnoreCase(String username);
    void deleteByUsername(String username);

    @Query("{username : { $regex: ?0 } }")
    List<User> findCustomByRegExUsername(String username);

}