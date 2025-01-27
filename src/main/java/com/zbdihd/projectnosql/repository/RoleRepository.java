package com.zbdihd.projectnosql.repository;

import com.zbdihd.projectnosql.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

    Role findByRole(String role);
}
