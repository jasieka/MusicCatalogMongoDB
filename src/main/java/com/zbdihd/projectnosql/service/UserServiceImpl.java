package com.zbdihd.projectnosql.service;

import com.zbdihd.projectnosql.model.Role;
import com.zbdihd.projectnosql.model.User;
import com.zbdihd.projectnosql.repository.RoleRepository;
import com.zbdihd.projectnosql.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //ROLES

    public void addRole(String role){
        roleRepository.save(new Role(role.toUpperCase()));
    }

    public void addRoleToUser(String username, String role){
        Role r = roleRepository.findByRole(role.toUpperCase());
        User u = findUserByUsername(username);
        u.getRoles().add(r);
        userRepository.save(u);
    }

    public Role getRole(String role) {
        return roleRepository.findByRole(role.toUpperCase());
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    //USERS

    public void addUser(String username, String password, String role){
        User user = new User();
        user.setUsername(username);
        user.setPassword(getEncryptedPassword(password));
        user.setEnabled(true);

        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole(role.toUpperCase()))));
        userRepository.save(user);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findUserByUsername(String username){
        return userRepository.findByUsername(username);
    }

    public List<User> findCustomByRegExUsername(String username){
        return userRepository.findCustomByRegExUsername(username);
    }

    public void deleteUserByUsername(String username){
        userRepository.deleteByUsername(username);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public String getEncryptedPassword(String password){
        return bCryptPasswordEncoder.encode(password);
    }

    public void changeStatusEnabled(User user) {
        if(user.isEnabled())
            user.setEnabled(false);
        else
            user.setEnabled(true);
        userRepository.save(user);
    }

    public void deleteAllUsersAndRoles(){
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}