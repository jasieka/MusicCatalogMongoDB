package com.zbdihd.projectnosql.service;

import com.zbdihd.projectnosql.model.Role;
import com.zbdihd.projectnosql.model.User;

import java.util.List;

public interface UserService {

    void addRole(String role); //with save
    void addRoleToUser(String username, String role); //with save
    Role getRole(String role);
    List<Role> getAllRoles();

    void addUser(String username, String password, String role); //with save
    User getUser(String username);
    List<User> getAllUsers();
    String getEncryptedPassword(String password);
    void changeStatusEnabled(User user); //with save
    void deleteAllUsersAndRoles();

}
