package com.zbdihd.projectnosql.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Document(collection = "User")
public class User {
    @Id
    private String id;

    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String username;
    private String password;
    private boolean enabled;
    @DBRef
    private Set<Role> roles;

    public String setRolesToString() {
        List<String> list = new ArrayList<String>();
        roles.forEach(e -> list.add(e.getRole()));
        return list.toString();
    }
}
