package com.zbdihd.projectnosql.service;

import com.zbdihd.projectnosql.model.Role;
import com.zbdihd.projectnosql.model.User;

import java.util.List;

public interface UserService {

    void addRole(String role);
    void addUser(String username, String password, String role);
    List<User> getAllUsers();
    List<Role> getAllRoles();
    void deleteAllUsersAndRoles();

}
