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

    public void addRole(String role){
        roleRepository.save(new Role(role.toUpperCase()));
    }

    public void addUser(String username, String password, String role){
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setEnabled(true);

        user.setRoles(new HashSet<>(Arrays.asList(roleRepository.findByRole(role.toUpperCase()))));
        userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    public void deleteAllUsersAndRoles(){
        roleRepository.deleteAll();
        userRepository.deleteAll();
    }

}