package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(String username);

}